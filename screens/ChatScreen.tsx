import React, {useState, useCallback, useEffect} from 'react';
import {
  NativeModules,
  View,
  StyleSheet,
  SafeAreaView,
  TouchableOpacity,
  Image,
  Button,
  Platform,
} from 'react-native';
import Ionicons from 'react-native-vector-icons/Ionicons';
import {
  Bubble,
  GiftedChat,
  IMessage,
  InputToolbar,
} from 'react-native-gifted-chat';
import {useNavigation} from '@react-navigation/native';

const {CameraModule} = NativeModules;

const ChatScreen = () => {
  const [image, setImage] = useState<string | undefined>(undefined);
  const [messages, setMessages] = useState<IMessage[]>([]);
  const navigation = useNavigation<any>();
  const [text, setText] = useState('');

  const headerRightComponent = useCallback(() => {
    return (
      <Button
        onPress={() => navigation.navigate('PiPScreen')}
        title="PiPScreen"
      />
    );
  }, [navigation]);
  useEffect(() => {
    navigation.setOptions({
      headerRight: headerRightComponent,
    });
  }, [headerRightComponent, navigation]);

  useEffect(() => {
    setMessages([
      {
        _id: 2,
        text: 'Tôi là D',
        createdAt: 1675925110369,
        user: {
          _id: 2,
          name: 'React Native',
          avatar: 'https://placeimg.com/140/140/any',
        },
      },
      {
        _id: 1,
        text: 'Xin chào!',
        createdAt: 1675925052375,
        user: {
          _id: 2,
          name: 'React Native',
          avatar: 'https://placeimg.com/140/140/any',
        },
      },
    ]);
  }, []);

  const onSend = useCallback((messages: IMessage[]) => {
    setMessages(previousMessages =>
      GiftedChat.append(previousMessages, messages),
    );
  }, []);
  const sendImage = useCallback(() => {
    const message = {
      _id: Date.now(),
      text: '',
      createdAt: new Date(),
      image: image,
      user: {
        _id: 'hungdz',
        name: 'HDZ',
      },
    };
    setMessages(previousMessages =>
      GiftedChat.append(previousMessages, [message]),
    );
    setImage(undefined);
  }, [image]);

  const takeAShot = async () => {
    if (Platform.OS === 'ios') {
      const req = await CameraModule.requestCameraPermission();
      if (req === 'authorized') {
        CameraModule.openCamera(
          (
            error: any,
            imageData: React.SetStateAction<string | undefined> | null,
          ) => {
            if (error) {
              console.log(error);
            } else {
              if (imageData != null) {
                setImage(imageData);
                console.log(imageData);
              }
            }
          },
        );
      }
    } else {
      const img = await CameraModule.openCamera();
      if (img != null) {
        setImage(img);
        console.log('[takeAShot]:', img);
      }
    }
  };

  const takeAPicture = async () => {
    if (Platform.OS === 'android') {
      const picked = await CameraModule.pickImage();
      if (picked != null) {
        setImage(picked);
        console.log('[takeAPicture]:', picked);
      }
    } else {
      CameraModule.pickImage(
        (
          error: any,
          imageData: React.SetStateAction<string | undefined> | null,
        ) => {
          if (error) {
            console.log('[TakeAPicture] error:', error);
          } else {
            if (imageData != null) {
              setImage(imageData);
              console.log(imageData);
            }
          }
        },
      );
    }
  };

  const renderBubble = (props: any) => (
    <Bubble
      {...props}
      wrapperStyle={{
        left: {backgroundColor: '#F2F2F2', padding: 4},
        right: {padding: 4},
      }}
    />
  );

  const btnIcon = () => {
    return (
      <View style={styles.btn}>
        <TouchableOpacity>
          <Ionicons
            name="camera"
            size={24}
            color={'black'}
            onPress={takeAShot}
          />
        </TouchableOpacity>
        <TouchableOpacity onPress={takeAPicture}>
          <Ionicons
            name="image"
            style={styles.iconSize}
            size={24}
            color={'black'}
          />
        </TouchableOpacity>
        <View style={styles.divider} />
      </View>
    );
  };

  const renderInputToolbar = (props: any) => (
    <InputToolbar
      {...props}
      primaryStyle={styles.inputContainer}
      optionTintColor="red"
    />
  );

  return (
    <SafeAreaView style={styles.container}>
      {image != null && (
        <View style={styles.imageContainer}>
          <TouchableOpacity
            style={styles.close}
            onPress={() => setImage(undefined)}>
            <Ionicons name="close-circle" size={24} color="gray" />
          </TouchableOpacity>
          {Platform.OS === 'android' ? (
            <Image
              source={{
                uri: image,
              }}
              style={styles.image}
            />
          ) : (
            <Image source={{uri: image}} style={styles.image} />
          )}

          <Button onPress={sendImage} title="Gửi" />
        </View>
      )}

      <GiftedChat
        messages={messages}
        text={text}
        // isCustomViewBottom={true}
        onInputTextChanged={setText}
        renderBubble={renderBubble}
        renderActions={btnIcon}
        renderInputToolbar={renderInputToolbar}
        onSend={(mess: IMessage[]) => onSend(mess)}
        textInputStyle={styles.textInputColor}
        alwaysShowSend
        user={{
          _id: 'hungdz',
        }}
      />
    </SafeAreaView>
  );
};

export default ChatScreen;
const styles = StyleSheet.create({
  container: {flex: 1},
  btn: {
    flexDirection: 'row',
    justifyContent: 'center',
    width: 110,
  },
  composer: {
    backgroundColor: 'gray',
    paddingVertical: 6,
    flex: 1,
    borderRadius: 12,
  },
  inputContainer: {
    alignItems: 'center',
    borderTopWidth: 0,
  },
  sendBtn: {
    justifyContent: 'center',
    marginHorizontal: 8,
  },
  imageContainer: {
    position: 'absolute',
    zIndex: 2,
    bottom: 50,
    right: 20,
  },
  close: {
    position: 'absolute',
    right: -14,
    zIndex: 2,
    top: -14,
  },
  image: {
    width: 80,
    height: 110,
    borderRadius: 10,
    marginBottom: 6,
  },
  divider: {
    borderRightWidth: 1,
    borderColor: 'rgba(0,0,0,0.3)',
    paddingLeft: 4,
  },
  iconSize: {
    marginHorizontal: 16,
  },
  textInputColor: {color: 'black'},
});
