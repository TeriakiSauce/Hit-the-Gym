package com.example.capstone2024.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity(tableName = "user_setup")
@TypeConverters(Converters.class)
public class UserSetup {

    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="age")
    private String age;

    @ColumnInfo(name="currentWeight")
    private String currentWeight;

    @ColumnInfo(name="targetWeight")
    private String targetWeight;

    @ColumnInfo(name="workoutLevel")
    private String workoutLevel;

    @ColumnInfo(name="targetBodyParts")
    private List<String> targetBodyParts;
    private String equipment;

    public UserSetup() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAge(){
        return age;
    }

    public void setCurrentWeight(String currentWeight) {
        this.currentWeight = currentWeight;
    }

    public String getCurrentWeight(){
        return currentWeight;
    }

    public void setTargetWeight(String targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getTargetWeight(){
        return targetWeight;
    }

    public void setWorkoutLevel(String workoutLevel) {
        this.workoutLevel = workoutLevel;
    }

    public String getWorkoutLevel(){
        return workoutLevel;
    }

    public void setTargetBodyParts(List<String> bodyParts) {
        this.targetBodyParts = bodyParts;
    }

    public List<String> getTargetBodyParts(){
        return targetBodyParts;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getEquipment(){
        return equipment;
    }
}