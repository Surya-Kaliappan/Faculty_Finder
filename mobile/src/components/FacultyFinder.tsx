import React, { useRef, useState, useEffect } from 'react';
import {
  View,
  Text,
  TextInput,
  FlatList,
  ActivityIndicator,
  StyleSheet,
  TouchableOpacity,
  Animated,
  KeyboardAvoidingView,
  Platform,
  Linking,
} from 'react-native';
import DropDownPicker from 'react-native-dropdown-picker';
import Clipboard from '@react-native-clipboard/clipboard';
import Toast from 'react-native-toast-message';
import { searchFaculty, getFacultySchedule, getSpecification } from '../services/facultyService';

const FacultyFinder = () => {
  const [name, setName] = useState('');
  const [spec, setSpec] = useState('');
  const [facultyList, setFacultyList] = useState([]);
  const [facultyVisible, setFacultyVisible] = useState(true);
  const [searchbarVisible, setSearchbarVisible] = useState(true);
  const [loading, setLoading] = useState(false);
  const [bigCardLoading, setBigCardLoading] = useState(false);
  const [selectedFaculty, setSelectedFaculty] = useState(null);
  const [facultySchedule, setFacultySchedule] = useState([]);
  const [typing, setTyping] = useState(false);
  const bigCardOpacity = useRef(new Animated.Value(0)).current;
  const searchBarBorder = useRef(new Animated.Value(0)).current;
  const cardScale = useRef(new Animated.Value(1)).current;
  const dropDownBorder = useRef(new Animated.Value(0)).current;
  const [selectedIndex, setSelectedIndex] = useState<number | null>(null);
  const typingTimeout = useRef<ReturnType<typeof setTimeout> | null>(null);

  const [open, setOpen] = useState(false);
  const [loadingDropdown, setLoadingDropdown] = useState(true);
  const [specializations, setSpecialization] = useState([]);

  // Function to highlight matched text
  const highlightMatch = (text: string) => {
    if (!name) return <Text style={styles.cardText}>{text}</Text>;

    const regex = new RegExp(`(${name})`, 'gi');
    const parts = text.split(regex);

    return (
      <Text style={styles.cardText}>
        {parts.map((part, index) =>
          part.toLowerCase() === name.toLowerCase() ? (
            <Text key={index} style={styles.highlightText}>
              {part}
            </Text>
          ) : (
            part
          )
        )}
      </Text>
    );
  };

  // Trigger handleChange() when dropdown value changes
  useEffect(() => {
    if (spec !== null) {
      setSelectedFaculty(null);
      setFacultySchedule([]);
      setSelectedIndex(null);
      setLoadingDropdown(true);
      handleChange(); // Call the search function immediately
      setLoadingDropdown(false);
    }
  }, [spec]);

  useEffect(() => {
    const fetchFilters = async () => {
      try {
        const data = await getSpecification();
        if (data && data.length > 0) {
          setSpecialization(data); // Set data if available
        } else {
          setTimeout(fetchFilters, 3000); // Keep retrying every 3 seconds
        }
      } catch (error) {
        console.error('Error fetching specializations:', error);
        setTimeout(fetchFilters, 3000); // Retry again if there's an error
      }
    };
  
    fetchFilters();
  }, []);
  

  // Animate search bar when typing 
  useEffect(() => {
    Animated.timing(searchBarBorder, {
      toValue: typing ? 1 : 0,
      duration: 300,
      useNativeDriver: false,
    }).start();
  }, [typing]);

  useEffect(() => {
    Animated.timing(dropDownBorder, {
      toValue: open ? 1 : spec ? 0 : 0, // Reset border after selection
      duration: 300,
      useNativeDriver: false,
    }).start();
  }, [spec, open]);

  // Fetch Faculty Data with Debouncing & Typing Indicator
  useEffect(() => {
    if (typingTimeout.current) {
      clearTimeout(typingTimeout.current); 
    }
    setTyping(true);
    setOpen(false);
    setLoadingDropdown(false);
    typingTimeout.current = setTimeout(async () => {
      if (name.trim() === '') {
        setFacultyList([]);
        setTyping(false);
        return; // Exit early if the input is empty
      }

      setLoading(true);
      setSelectedFaculty(null);
      setFacultySchedule([]);
      setSelectedIndex(null);
      setName(name.trim());
      await handleChange();
      setLoading(false);
      setTyping(false);
    }, 500);
  
    return () => clearTimeout(typingTimeout.current!); // Ensure cleanup on unmount
  }, [name]);
  

  const handleChange = async () =>{
    setFacultyList([]);
    const result = await searchFaculty({ name: name, department: '', specialization:spec });
    setFacultyList(result);
  }

  // Function to handle dialing a phone number
  const handleDial = (phoneNumber : any) => {
    const url = `tel:${phoneNumber}`;
    Linking.openURL(url).catch(err => console.error("Failed to open dialer", err)); // **Added**
  };

  // Function to handle sending an email
  const handleEmail = (email : any) => {
    const url = `mailto:${email}`;
    Linking.openURL(url).catch(err => console.error("Failed to open email app", err)); // **Added**
  };

  // Fetch Faculty Schedule with Animation
  const fetchSchedule = async (faculty : any, index : any) => {
    setSelectedFaculty(faculty);
    setSelectedIndex(index);
    setBigCardLoading(true);
    setFacultyVisible(false);  // Hide faculty list
  
    Animated.sequence([
      Animated.timing(cardScale, { toValue: 1.05, duration: 150, useNativeDriver: true }),
      Animated.timing(cardScale, { toValue: 1, duration: 150, useNativeDriver: true }),
    ]).start();
  
    const schedule = await getFacultySchedule(faculty.emp_id);
    setFacultySchedule(schedule || []);
    setBigCardLoading(false);
  
    Animated.timing(bigCardOpacity, {
      toValue: 1,
      duration: 300,
      useNativeDriver: true,
    }).start();
  };
  

  return (
    <KeyboardAvoidingView
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
      style={styles.container}
    >
    { facultyVisible && (
      <Animated.View
        style={[
          styles.inputContainer,
          { borderColor: searchBarBorder.interpolate({ inputRange: [0, 1], outputRange: ['#555', '#1ff'] }) },
        ]}
      >
        <TextInput
          style={styles.input}
          placeholder="Search by faculty name"
          placeholderTextColor="#888"
          value={name}
          onChangeText={(text) => {
            // if(!text){setFacultyVisible(false); setName('')}else{setFacultyVisible(true)}
            setName(text);
            setSelectedFaculty(null);
            setSelectedIndex(null);
            setFacultyVisible(true);
          }}
          onSubmitEditing={handleChange}
        />
        
        {(typing || loading) && <ActivityIndicator size="small" color="#1ff" style={styles.typingIndicator} />}
      </Animated.View>
    )}
      {/* Dropdown for Department */}
    { facultyVisible && (
      <Animated.View
        style={[
          styles.animatedDropDownContainer, 
          { borderColor: dropDownBorder.interpolate({ inputRange: [0, 1], outputRange: ['#555', '#1ff'] }) }
        ]}
      >
        <DropDownPicker
          open={open}
          value={spec}
          items={[
            { label: 'No Designation', value: '' }, // Always the first option inside the dropdown
            ...(Array.isArray(specializations) ? specializations.map((spec: any) => ({ label: spec.name, value: spec.name })) : [])
          ]}
          setOpen={setOpen}
          setValue={setSpec}
          // setItems={setSpec}
          placeholder="Select Department"
          placeholderStyle={{ color: '#888' }}
          style={styles.dropDown}
          textStyle={{ color: '#fff', fontSize: 18 }}
          dropDownContainerStyle={styles.dropDownContainer}
          onOpen={() => spec!== "" ? setLoadingDropdown(true) : setLoadingDropdown(false)}
          listItemLabelStyle={{ color: '#fff' }}
          tickIconStyle={{ tintColor: '#1ff' }}
          // onClose={() => setLoadingDropdown(true)}
        />

        {/* Show loader only when dropdown is open */}
        {loadingDropdown && <ActivityIndicator size="small" color="#1ff" style={styles.typingIndicator1} />}
      </Animated.View>
    )}

      <View style={styles.separator} />

      {/* {loading && <ActivityIndicator size="large" color="#fff" />} */}

      <FlatList
        data={facultyList}
        keyExtractor={(item : any) => item.emp_id.toString()}
        renderItem={({ item, index }) => (
          <Animated.View style={{ transform: [{ scale: selectedIndex === index ? cardScale : 1 }] }}>
            {facultyVisible && ( // <-- Fixed this to use facultyVisible
              <TouchableOpacity style={styles.card} onPress={() => fetchSchedule(item, index)}>
                {highlightMatch(item.emp_name)}
                <Text style={styles.cardText1}>{item.emp_spec}</Text>
              </TouchableOpacity>
            )}

            {selectedIndex === index && selectedFaculty && (
              <Animated.View style={[styles.bigCard, { opacity: bigCardOpacity }]}>    
                <TouchableOpacity
                  style={styles.backArrow}
                  onPress={() => {
                    setFacultyVisible(true);
                    setSelectedFaculty(null);
                  }}
                >
                  <Text style={styles.arrow}>{'< Back'}</Text>
                </TouchableOpacity>

                {bigCardLoading ? (
                  <ActivityIndicator size="large" color="#fff" style={{ marginVertical: 20 }} />
                ) : (
                  <>
                  <TouchableOpacity
                  onLongPress={() => {
                    Toast.show({ type: 'info', text1: selectedFaculty.emp_id, text2: "Faculty ID" });
                  }}>
                    <Text style={styles.bigCardTitle}>Faculty Details</Text>
                    </TouchableOpacity>
                    <View style={styles.bigCardRow}>
                      <Text style={styles.leftText}>Name</Text>
                      <Text style={styles.rightText}>{selectedFaculty.emp_name}</Text>
                    </View>
                    <View style={styles.bigCardRow}>
                      <Text style={styles.leftText}>Designation</Text>
                      <Text style={styles.rightText}>{selectedFaculty.emp_spec}</Text>
                    </View>
                    <View style={styles.bigCardRow}>
                      <Text style={styles.leftText}>Mobile No</Text>
                      <View style={{ flex: 1 }}>
                        <TouchableOpacity
                          onPress={() => handleDial(selectedFaculty.emp_ph)}
                          onLongPress={() => {
                            Clipboard.setString(selectedFaculty.emp_ph);
                            Toast.show({ type: 'success', text1: 'Copied!', text2: 'Phone number copied!' });
                          }}>
                          <Text style={styles.rightText}>{selectedFaculty.emp_ph}</Text>
                        </TouchableOpacity>
                      </View>
                    </View>
                    <View style={styles.bigCardRow}>
                      <Text style={styles.leftText}>E-mail</Text>
                      <View style={{ flex: 1 }}>
                        <TouchableOpacity 
                          onPress={() => handleEmail(selectedFaculty.emp_email)}
                          onLongPress={() => {
                            Clipboard.setString(selectedFaculty.emp_email);
                            Toast.show({ type: 'success', text1: 'Copied!', text2: 'E-mail copied!' });
                          }}>
                          <Text style={styles.rightText}>{selectedFaculty.emp_email}</Text>
                        </TouchableOpacity>
                      </View>
                    </View>
                    <View style={styles.bigCardRow}>
                      <Text style={styles.leftText}>Cabin</Text>
                      <Text style={styles.rightText}>{selectedFaculty.emp_cabin}</Text>
                    </View>
                    <View style={styles.schedule}>
                      <Text style={styles.scheduleText}>Schedule</Text>
                      <Text style={styles.scheduleResult}>
                        {facultySchedule?.message 
                          ? facultySchedule.message 
                          : `${facultySchedule?.time} in ${facultySchedule?.venue}`}
                      </Text>
                    </View> 
                  </>
                )}
              </Animated.View>
            )}
          </Animated.View>
        )}
      />

    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: { padding: 10, flex: 1 },  //backgroundColor: '#222'
  inputContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderRadius: 8,
    paddingRight: 12,
    paddingLeft: 5,
  },
  animatedDropDownContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderRadius: 8,
    marginTop: 10,
    zIndex: 3,
  },
  input: {
    flex: 1,
    padding: 12,
    color: '#fff',
    fontSize: 18,
  },
  typingIndicator: { marginLeft: 20 },
  typingIndicator1: {
    position: 'absolute',
    right: 15, 
    top: '50%', 
    transform: [{ translateY: -10 }],
  },
  card: { backgroundColor: '#444', padding: 15, marginVertical: 8, borderRadius: 10, alignItems: 'center', width: '90%', alignSelf: 'center' },
  cardText: { color: '#fff', fontSize: 20, textAlign: 'center', marginVertical: 5, fontWeight: 700 },
  cardText1: { color: '#fff', fontSize: 18, textAlign: 'center', marginVertical: 5 },
  highlightText: { fontWeight: 'bold', color: '#ff0' },
  separator: { height: 3, backgroundColor: '#777', marginVertical: 15, alignSelf: 'stretch', zIndex: 2 },
  bigCard: { backgroundColor: '#333', padding: 20, borderRadius: 12, marginBottom: 10 },
  bigCardTitle: { fontSize: 24, color: '#fff', textAlign: 'center', marginBottom: 20, marginTop: 18, fontWeight: 700 },
  bigCardRow: { flexDirection: 'column', justifyContent: 'space-between', marginVertical: 8 },
  leftText: { color: '#ffcc00', fontSize: 20, flex: 1, textAlign: 'center', marginBottom: 5 },
  rightText: { color: '#66ff99', fontSize: 18, flex: 1, textAlign: 'center', fontWeight: 500 },
  schedule: { marginTop: 20 },
  scheduleText: { fontSize: 20, color: '#fff', textAlign: 'center', marginBottom: 5 },
  scheduleResult: { fontSize: 26, color: '#fff', textAlign: 'center', marginBottom: 25},
  backArrow: {
    position: 'absolute',
    backgroundColor: '#fff',
    top: 5,
    left: 5,
    borderRadius: 50,
  },
  arrow: {
    position: 'relative',
    fontSize: 18,
    backgroundColor: '#333',
    color: '#fff',
    fontWeight: 500,
    borderRadius: 50,
    paddingVertical: 2,
    paddingHorizontal: 10,
  },
  dropDown: {
    backgroundColor: '#00000000',
    borderColor: '#555',
    borderRadius: 8,
    paddingHorizontal: 10,
    color: '#fff',
  },
  dropDownContainer: {
    backgroundColor: '#333',
    borderColor: '#555',
  },
});

export default FacultyFinder;
