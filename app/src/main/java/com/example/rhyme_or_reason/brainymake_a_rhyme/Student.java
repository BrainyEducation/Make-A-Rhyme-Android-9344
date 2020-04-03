package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

public class Student implements Serializable {

    private String name;
    private String uuid;
    private String colorPassword;
    private String animalPassword;
    private ArrayList<RhymeTemplate> savedRhymes;
    private ArrayList<Word> unlockedWords;
    private HashMap<String, ArrayList<int[]>> attemptsMap;
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

    public Student(String name, String colorPassword, String animalPassword, HashMap attemptsMap)
    {
        this.name = name;
        this.uuid = UUID.randomUUID().toString();
        this.colorPassword = colorPassword;
        this.animalPassword = animalPassword;
        this.savedRhymes = new ArrayList<>();
        this.unlockedWords = new ArrayList<>();
        this.attemptsMap = attemptsMap;

    }

    public Student(String name, String uuid, String colorPassword, String animalPassword, HashMap attemptsMap)
    {
        this.name = name;
        this.uuid = uuid;
        this.colorPassword = colorPassword;
        this.animalPassword = animalPassword;
        this.attemptsMap = attemptsMap;
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

    public HashMap getAttemptsMap() {return attemptsMap;}

    public void addToAttemptsMap(String word, int[] newAttempt) {
        if (!attemptsMap.containsKey(word)) {
            ArrayList<int[]> initAttempt = new ArrayList<>();
            initAttempt.add(newAttempt);
            attemptsMap.put(word, initAttempt);
        } else {
            ArrayList attempts = attemptsMap.get(word);
            attempts.add(newAttempt);
            attemptsMap.put(word, attempts);
        }
    }

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

    public void saveData(Context context) { //key - uuid, obj - attemptsMap
        SharedPreferences pref = context.getSharedPreferences("AttemptsMap", MODE_PRIVATE);
        String objToString = new Gson().toJson(attemptsMap);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ProgressMap", objToString);
        editor.apply();
    }

    public HashMap loadMap(Context context) { //key - uuid
        SharedPreferences sharedpref = context.getSharedPreferences("AttemptsMap", MODE_PRIVATE);
        String val = new Gson().toJson(new HashMap<String, ArrayList<int[]>>());
        String jsonStr = sharedpref.getString("ProgressMap", val);
        TypeToken<HashMap<String, ArrayList<int[]>>> token = new TypeToken<HashMap<String, ArrayList<int[]>>>() {};
        HashMap<String, ArrayList<int[]>> mapFromPref = new Gson().fromJson(jsonStr, token.getType());
        return mapFromPref;
    }

}