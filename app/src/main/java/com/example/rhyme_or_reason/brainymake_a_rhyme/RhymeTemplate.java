package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RhymeTemplate implements Serializable {
    private String name;
    private String imageName;
    private Boolean locked;
    private int numImages;
    private ArrayList<Word> chosenWords = new ArrayList<>();
    private static ArrayList<String> serializedUnlockedWords;
    private static Context ioContext;

    public RhymeTemplate(String name, String imageName, Boolean locked, int numBlanks, int numImages) {
        this.name = name;
        this.imageName = imageName;
        this.locked = locked;
        this.numImages = numImages;
        while(chosenWords.size() < numBlanks) chosenWords.add(null);
    }

    public void updateChosenWords(int position, Word newWord) {
        chosenWords.set(position, newWord);
    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }

    public Boolean getLocked() {
        return locked;
    }

    public int getNumImages() {
        return numImages;
    }

    public ArrayList<Word> getChosenWords() { return chosenWords; }

    public void saveRhymeTemplate(Context context)
    {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this);
        prefsEditor.putString("SavedRhyme", json);
        prefsEditor.commit();

    }

    public static RhymeTemplate retrieveRhymeTemplate(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("SavedRhyme", "");
        return gson.fromJson(json, RhymeTemplate.class);
    }

}
