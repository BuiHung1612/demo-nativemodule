import React, {useEffect} from 'react';
import {
  NativeModules,
  StyleSheet,
  SafeAreaView,
  NativeEventEmitter,
  Platform,
  BackHandler,
} from 'react-native';
import Video from 'react-native-video';
const {PictureInPictureModule} = NativeModules;
const ShakeDetectorModule = NativeModules.ShakeDetectorModule;
const shakeDetectorEmitter = new NativeEventEmitter(
  Platform.OS === 'android' ? null : ShakeDetectorModule,
);
const PiPScreen = () => {
  useEffect(() => {
    const subscription = shakeDetectorEmitter.addListener(
      'RCTShowDevMenuNotification',
      () => {
        PictureInPictureModule.enterPictureInPicture();
        // Alert.alert('Thông báo khẩn cấp', '', [{text: 'OK'}]);
      },
    );

    return () => {
      subscription.remove();
    };
  }, []);

  useEffect(() => {
    const backAction = () => {
      if (Platform.OS === 'android') {
        PictureInPictureModule.enterPictureInPicture();
      }

      return true;
    };

    const backHandler = BackHandler.addEventListener(
      'hardwareBackPress',
      backAction,
    );

    return () => backHandler.remove();
  }, []);

  return (
    <SafeAreaView style={styles.container}>
      <Video
        source={{
          uri: 'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4',
        }}
        controls
        style={{width: '100%', height: '100%'}}
        playInBackground={true}
      />
    </SafeAreaView>
  );
};

export default PiPScreen;
const styles = StyleSheet.create({
  container: {flex: 1, backgroundColor: 'rgba(0,0,0,0.5)'},
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
});
