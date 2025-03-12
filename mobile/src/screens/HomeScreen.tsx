import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import FacultyFinder from '../components/FacultyFinder';
import Toast from 'react-native-toast-message';

const HomeScreen = ({ navigation }: { navigation: any }) => {
  return (
    <View style={styles.container}>
      {/* Long Press on Title to Open Settings */}
      <TouchableOpacity onLongPress={() => navigation.navigate('Settings')}>
        <Text style={styles.title}>Faculty Finder</Text>
      </TouchableOpacity>

      <FacultyFinder />
      <Toast />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 10,
    backgroundColor: '#111',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    textAlign: 'center',
    color: '#fff',
    marginBottom: 10,
  },
});

export default HomeScreen;
