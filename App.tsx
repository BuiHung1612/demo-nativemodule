import React from 'react';
import {NavigationContainer, useNavigation} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import ChatScreen from './screens/ChatScreen';
import PiPScreen from './screens/PiPScreen';

const App = () => {
  const Stack = createNativeStackNavigator();

  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="ChatScreen" component={ChatScreen} />
        <Stack.Screen name="PiPScreen" component={PiPScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default App;
