package com.example.capstone2024;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

import com.example.capstone2024.models.UserSetup;

public class UserSetupTest {

    private UserSetup userSetup;

    @Before
    public void setUp() {
        // Initialize the UserSetup object before each test
        userSetup = new UserSetup();
    }

    @Test
    public void testSetAndGetId() {
        // Test setting and getting the ID
        userSetup.setId(1);
        assertEquals(1, userSetup.getId());
    }

    @Test
    public void testSetAndGetName() {
        // Test setting and getting the name
        userSetup.setName("John Doe");
        assertEquals("John Doe", userSetup.getName());
    }

    @Test
    public void testSetAndGetAge() {
        // Test setting and getting the age
        userSetup.setAge("30");
        assertEquals("30", userSetup.getAge());
    }

    @Test
    public void testSetAndGetCurrentWeight() {
        // Test setting and getting current weight
        userSetup.setCurrentWeight("180");
        assertEquals("180", userSetup.getCurrentWeight());
    }

    @Test
    public void testSetAndGetTargetWeight() {
        // Test setting and getting target weight
        userSetup.setTargetWeight("160");
        assertEquals("160", userSetup.getTargetWeight());
    }

    @Test
    public void testSetAndGetWorkoutLevel() {
        // Test setting and getting workout level
        userSetup.setWorkoutLevel("Intermediate");
        assertEquals("Intermediate", userSetup.getWorkoutLevel());
    }

    @Test
    public void testSetAndGetTargetBodyParts() {
        // Test setting and getting target body parts (List)
        List<String> bodyParts = Arrays.asList("Arms", "Legs", "Chest");
        userSetup.setTargetBodyParts(bodyParts);
        assertEquals(bodyParts, userSetup.getTargetBodyParts());
    }

    @Test
    public void testSetAndGetEquipment() {
        // Test setting and getting equipment
        userSetup.setEquipment("Dumbbells");
        assertEquals("Dumbbells", userSetup.getEquipment());
    }

    @Test
    public void testToString() {
        // Set values
        userSetup.setId(1);
        userSetup.setName("John Doe");
        userSetup.setAge("30");
        userSetup.setCurrentWeight("180");
        userSetup.setTargetWeight("160");
        userSetup.setWorkoutLevel("Intermediate");
        userSetup.setTargetBodyParts(Arrays.asList("Arms", "Legs"));
        userSetup.setEquipment("Dumbbells");

        // Expected output from toString method
        String expectedString = "UserSetup{id=1, name='John Doe', age='30', currentWeight='180', targetWeight='160', workoutLevel='Intermediate', targetBodyParts=[Arms, Legs], equipment='Dumbbells'}";
        assertEquals(expectedString, userSetup.toString());
    }
}
