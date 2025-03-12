import React from "react";
import {  Text, TouchableOpacity, Animated } from "react-native";

interface FacultyCardProps {
  faculty: { id: number; name: string };
  onPress: () => void;
}

const FacultyCard: React.FC<FacultyCardProps> = ({ faculty, onPress }) => {
  const scaleAnim = new Animated.Value(0.9);

  React.useEffect(() => {
    Animated.spring(scaleAnim, {
      toValue: 1,
      useNativeDriver: true,
      speed: 5,
      bounciness: 10,
    }).start();
  }, []);

  return (
    <Animated.View
      style={{
        transform: [{ scale: scaleAnim }],
        backgroundColor: "#1e1e1e",
        padding: 15,
        borderRadius: 10,
        marginVertical: 8,
        shadowColor: "#000",
        shadowOpacity: 0.2,
        shadowOffset: { width: 0, height: 2 },
        elevation: 4,
      }}
    >
      <TouchableOpacity onPress={onPress}>
        <Text style={{ color: "#fff", fontSize: 18, fontWeight: "bold", textAlign: "center" }}>
          {faculty.name}
        </Text>
      </TouchableOpacity>
    </Animated.View>
  );
};

export default FacultyCard;
