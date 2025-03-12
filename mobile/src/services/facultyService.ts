import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import Toast from 'react-native-toast-message';

// Function to get the API Base URL dynamically
export const getApiBaseUrl = async (): Promise<string | null> => {
    try {
        const storedIp = await AsyncStorage.getItem('apiBaseUrl');
        console.log("Retrieved API Base URL:", storedIp);  // Debug log
        return storedIp;
    } catch (error) {
        console.error("Error retrieving API Base URL:", error);
        return null;
    }
};


// 🔹 Search faculty based on filters
export const searchFaculty = async (filters : any) => {
    try {
        console.log("search things: ",filters);
        const apiBaseUrl = await AsyncStorage.getItem('apiBaseUrl');
        if (!apiBaseUrl) {
            console.error("API Base URL is not set");
            // Alert.alert("API Base URL is not set. Please enter it in settings.");
            Toast.show({ type: 'error', text1: "Can't Reach", text2: "API Base URL is not set. Please enter it in settings." });
            return [];
        }

        const url = `http://${apiBaseUrl}/search`;
        console.log("Fetching faculty data from:", url, "with filters:", filters); // Debugging

        const response = await axios.get(url, { params: filters });
        console.log(response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching faculty data:", error);
        // Alert.alert(`Error fetching faculty data: ${error}`);
        Toast.show({ type: 'error', text1: "Can't Reach", text2: String(error) });
        return [];
    }
};


// 🔹 Get faculty schedule based on emp_id
export const getFacultySchedule = async (emp_id: string) => {
  try {
    const API_BASE_URL = await getApiBaseUrl();
    if (!API_BASE_URL) throw new Error('API Base URL not set');

    const response = await axios.get(`http://${API_BASE_URL}/schedule`, { params: { emp_id } });
    return response.data;
  } catch (error) {
    console.error('Error fetching schedule:', error);
    return null;
  }
};

export const getSpecification = async () => {
    try {
        const API_BASE_URL = await getApiBaseUrl();
        if (!API_BASE_URL) throw new Error("API Base URL not set");

        const response = (await axios.get(`http://${API_BASE_URL}/specializations`));
        return response.data;
    } catch (error) {
        console.error('Error fetching Designation\n'+error);
        return null;
    }
}
