import React, { useState } from 'react';
import { View, Text, TextInput, Button, StyleSheet, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { StackNavigationProp } from '@react-navigation/stack';
import { RouteProp } from '@react-navigation/native';
import Toast from 'react-native-toast-message';

type RootStackParamList = {
  Home: undefined;
  Settings: undefined;
};

type SettingsScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Settings'>;
type SettingsScreenRouteProp = RouteProp<RootStackParamList, 'Settings'>;

type Props = {
  navigation: SettingsScreenNavigationProp;
  route: SettingsScreenRouteProp;
};

const SettingsScreen: React.FC<Props> = ({ navigation }) => {
  const [ipAddress, setIpAddress] = useState('');

  const handleSave = async () => {
    if (!ipAddress.trim()) { // Check if IP is empty
        // Alert.alert('Please enter a valid IP address.');
        Toast.show({ type: 'info', text1: 'Info', text2: 'Please Enter a Valid IP Address.' });
        return;
    }

    console.log("Saving IP Address:", ipAddress);  // Debug log
    await AsyncStorage.setItem('apiBaseUrl', ipAddress);
    // Alert.alert(`IP address saved: ${ipAddress}`);
    Toast.show({ type: 'success', text1: 'Assigned', text2: 'Ip address has Assigned Successfully' });
    navigation.goBack(); // Go back after saving
  };



  return (
    <View style={styles.container}>
      <Text style={styles.label}>Enter Backend IP:</Text>
      <TextInput
        style={styles.input}
        value={ipAddress}
        onChangeText={setIpAddress}
        placeholder="192.168.X.X"
        placeholderTextColor="#888"
      />
      <View style={styles.buttonContainer}>
        <Button title="Save" onPress={handleSave} />
        <Button title="Back" onPress={() => navigation.goBack()} color="red" />
      </View>
      <Toast />
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: '#121212' },
  label: { fontSize: 18, color: 'white', marginBottom: 10 },
  input: { width: 200, height: 40, borderColor: 'gray', borderWidth: 1, paddingHorizontal: 10, color: 'white', backgroundColor: '#333', borderRadius: 5, marginBottom: 10 },
  buttonContainer: { flexDirection: 'row', gap: 20, marginTop: 10 },
});

export default SettingsScreen;