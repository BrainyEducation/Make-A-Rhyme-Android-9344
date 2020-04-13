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
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Word implements Serializable {

    private String text;
    private Boolean locked;
    private String imageName;
    private String audioName;
    private String type;

    public Word(String text, Boolean locked, String imageName, String audioName, String type)
    {
        this.text = text;
        this.locked = locked;
        this.imageName = imageName;
        this.audioName = audioName;
        this.type = type;
    }


    public void setUnlocked() {
        this.locked = false;
    }

    public Boolean getLockedStatus() {
        return this.locked;
    }

    public String getText()
    {
        return this.text;
    }

    public String getImageName()
    {
        return this.imageName;
    }

    public String getAudioName()
    {
        return this.audioName;
    }

    public String getType() { return this.type; }


    /*
     * Saves a word's status (locked/unlocked) for the particular user (from the UUID)
     */
    public void saveWord(Context context, String uuid)
    {
        // Get the number of saved rhymes
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();

        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        String wordString = this.text;

        String json = gson.toJson(this);
        prefsEditor.putString(uuid + "SavedWord" + wordString, json);
        prefsEditor.commit();
    }

    /*
     * Get the current status of a word for a particular student to see if it's locked/unlocked
     */
    public static Word retrieveWord(Context context, String wordString, String category, String uuid) {

        try {
            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = appSharedPrefs.getString(uuid + "SavedWord" + wordString, "");
            return gson.fromJson(json, Word.class);
        } catch (Exception e) {
            return null;
        }
    }
}