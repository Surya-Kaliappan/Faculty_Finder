import AsyncStorage from '@react-native-async-storage/async-storage';

const API_STORAGE_KEY = 'backend_api_ip';

export const setApiIP = async (ip: string) => {
  try {
    await AsyncStorage.setItem(API_STORAGE_KEY, ip);
  } catch (error) {
    console.error('Error saving API IP:', error);
  }
};

export const getApiIP = async (): Promise<string | null> => {
  try {
    return await AsyncStorage.getItem(API_STORAGE_KEY);
  } catch (error) {
    console.error('Error retrieving API IP:', error);
    return null;
  }
};
