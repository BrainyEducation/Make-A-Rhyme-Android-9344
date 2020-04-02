package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Student implements Serializable {

    private String name;
    private String uuid;
    private String colorPassword;
    private String animalPassword;
    private ArrayList<RhymeTemplate> savedRhymes;
    private ArrayList<Word> unlockedWords;
    private ArrayList<int[]> attemptsList;
    //private static Context ioContext;

    public Student(String name, String colorPassword, String animalPassword)
    {
        this.name = name;
        this.uuid = UUID.randomUUID().toString();
        this.colorPassword = colorPassword;
        this.animalPassword = animalPassword;
        this.savedRhymes = new ArrayList<>();
        this.unlockedWords = new ArrayList<>();

    }

    public Student(String name, String uuid, String colorPassword, String animalPassword, ArrayList attemptsList)
    {
        this.name = name;
        this.uuid = uuid;
        this.colorPassword = colorPassword;
        this.animalPassword = animalPassword;
        this.attemptsList = attemptsList;
        this.savedRhymes = new ArrayList<>();
        this.unlockedWords = new ArrayList<>();

    }

    public void addRhyme(RhymeTemplate newRhyme)
    {
        savedRhymes.add(newRhyme);
    }

    public void addUnlockedWord(Word newWord)
    {
        unlockedWords.add(newWord);
    }

    public ArrayList<RhymeTemplate> getSavedRhymes()
    {
        return savedRhymes;
    }

    public ArrayList<Word> getUnlockedWords()
    {
        return unlockedWords;
    }

    public String getName()
    {
        return name;
    }

    public String getUuid()
    {
        return uuid;
    }

    public String getColorPassword() {
        return colorPassword;
    }

    public String getAnimalPassword() {
        return animalPassword;
    }

    public ArrayList getAttemptsList() {return attemptsList;}

    public void addToAttemptsList(int[] newAttempt) {attemptsList.add(newAttempt);}

    public void saveStudent(Context context)
    {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        Set<String> currUUIDs = appSharedPrefs.getStringSet("UUIDs", new HashSet<String>());

        if (!currUUIDs.contains(uuid)) {
            currUUIDs.add(uuid); // Add if a new student
        }

        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        prefsEditor.putStringSet("UUIDs", currUUIDs); // UUIDs list with the new UUID appended

        String json = gson.toJson(this);
        prefsEditor.putString(uuid, json);
        prefsEditor.commit();
    }

    public static ArrayList<Student> retrieveStudents(Context context) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();

        Set<String> currUUIDs = appSharedPrefs.getStringSet("UUIDs", new HashSet<String>());

        String[] currUUIDsArray = currUUIDs.toArray(new String[currUUIDs.size()]);

        ArrayList<Student> toReturn = new ArrayList<>();

        // Checking against the size of the set but checking against array element)
        for (int index = 0; index < currUUIDs.size(); ++index) {
            String tempStudentJson = appSharedPrefs.getString(currUUIDsArray[index], "");
            toReturn.add(gson.fromJson(tempStudentJson, Student.class));
        }

        return toReturn;

        //String json = appSharedPrefs.getString("SavedRhyme" + rhymeNumString, "");
        //return gson.fromJson(json, RhymeTemplate.class);
    }

}