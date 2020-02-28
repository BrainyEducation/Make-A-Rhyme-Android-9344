package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Context;

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
    private static ArrayList<String> serializedUnlockedWords;
    private static Context ioContext;

    public Word(String text, Boolean locked, String imageName, String audioName, String type)
    {
        this.text = text;
        this.locked = locked && !serializedUnlockedWords.contains(text);
        this.imageName = imageName;
        this.audioName = audioName;
        this.type = type;
    }

    public void setUnlocked()
    {
        this.locked = false;
        try{
            FileOutputStream fos = ioContext.openFileOutput("UnlockedWords", Context.MODE_APPEND);
            PrintStream printstream = new PrintStream(fos);
            printstream.println(text);
            fos.close();

        } catch (FileNotFoundException fnfe) {
            /* shouldn't happen while writing to a file */
            throw new IllegalArgumentException(fnfe);
        } catch (IOException ioe) {
        /* shouldn't happen while writing to a file */
        throw new IllegalArgumentException(ioe);
        }
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

    public static void initialize(Context context)
    {
        serializedUnlockedWords = new ArrayList<>();
        try {
            ioContext = context;
            FileInputStream fis = context.openFileInput("UnlockedWords");
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            try(BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    line = reader.readLine();
                    serializedUnlockedWords.add(line);
                }
                fis.close();
            }
            catch(IOException e)
            {
                //shouldn't happen, if it does, something went wrong
                throw new IllegalArgumentException(e);
            }
        } catch (FileNotFoundException fnfe) {/*If file doesn't exist, it's new and should stay empty*/}

    }
}