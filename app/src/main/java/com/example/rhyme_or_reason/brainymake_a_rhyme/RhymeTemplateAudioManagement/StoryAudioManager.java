package com.example.rhyme_or_reason.brainymake_a_rhyme.RhymeTemplateAudioManagement;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageButton;

import com.example.rhyme_or_reason.brainymake_a_rhyme.R;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;

public class StoryAudioManager {

    private Context context;

    //contains constants defined for how the mp3 files will be sequentially played
    private StoryAudioConstants mp3Data;

    //toggling this to false prevents any audio in play_story from running
    private boolean continueAudioFlag = true;

    private ArrayList<String> wordList;

    private MediaPlayer mediaPlayer;

    public StoryAudioManager(Context context) {
        this.context = context;
        mp3Data = new StoryAudioConstants();
        mediaPlayer = new MediaPlayer();
    }

    /**
     * Changes whether the audio manager can play files. It is extremely important to set this to false
     * when navigating from the rhyme screen to another screen.
     * @param flag set this to true to enable audio playing, set this to false to stop any audio.
     */
    public void setContinueAudioFlag(boolean flag) {
        continueAudioFlag = flag;
        if (!flag) {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
        }
    }

    /**
     * Setter function for the list of words that the user has selected
     * @param wordList
     */
    public void setWordList(ArrayList<String> wordList) {
        this.wordList = wordList;
    }


    /**
     * This function does its best to reset the mediaPlayer finite state machine without
     * causing the app to crash
     */
    public void clearMediaPlayer() {
        try {
            mediaPlayer.stop();
        } catch (Exception e) {
        }
        try {
            mediaPlayer.release();
        } catch (Exception e) {
        }
        try {
            mediaPlayer.reset();
        } catch (Exception e) {
            return;
        }
        mediaPlayer = new MediaPlayer();
    }

    /**
     * Tries its best to reset the mediaPlayer instance var and give it the next mp3 file to play
     * @param s the name of the file without any file extension
     */
    private void setMediaPlayerFile(String s) {
        AssetFileDescriptor afd = context.getResources().openRawResourceFd(context.getResources().getIdentifier(s, "raw",context.getPackageName()));
        try
        {
            clearMediaPlayer();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mediaPlayer.prepare();
            afd.close();
        }
        catch (IllegalArgumentException e)
        {
            Log.d("setMediaPlayerFile", e.toString());
        }
        catch (IllegalStateException e)
        {
            Log.d("setMediaPlayerFile", e.toString());
        }
        catch (IOException e)
        {
            Log.d("setMediaPlayerFile", e.toString());
        }
    }


    /**
     * Plays the audio files associated with the given story on a separate thread and resets the image button
     * when the audio is done playing
     * @param storyName the name of the story to play
     * @param iB the ImageButton that triggered this function
     */
    public void playStoryThread(final String storyName, final ImageButton iB) {
        setContinueAudioFlag(true);
        Thread stopThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    play_story(storyName);
                    iB.setImageResource(R.drawable.ic_play);
                } catch (InterruptedException e) {
                    Log.d("playStoryThread",e.toString());
                    e.printStackTrace();
                }
            }
        };
        stopThread.start();
    }

    /**
     * Syncs up the audio files for the story with the audio files that correspond to words
     * that have filled in the blank.
     *
     * Note, due to the user being able to pause this function any time they choose,
     * there is extensive error checking for IllegalStateExceptions with regard to the mediaPlayer
     *
     * @param storyName
     */
    private void play_story(String storyName) {
        ArrayList<String> fileNames = new ArrayList<>();
        Log.d("storyName", storyName);
        StoryAudioConstantContainer constantContainer = mp3Data.storyNameToConstantContainer.get(storyName);
        int numberOfFiles = constantContainer.numberOfFiles;
        String fileprefix = constantContainer.fileNamePrefix;
        ArrayList<Boolean> isThereARealBlankBetweenFiles = constantContainer.checkIfNeedSubstitution;
        int traversedBlanks = 0;
        for (int i = 1; i <= numberOfFiles; i++) {
            fileNames.add(fileprefix + i);
        }

        for (int i = 0; i < numberOfFiles; i++) {
            final boolean[] lock = {true};
            clearMediaPlayer();
            setMediaPlayerFile(fileNames.get(i));
            mediaPlayer.start();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Log.d("wait","broken");
            }
            try {
                while (mediaPlayer.isPlaying()) {
                    if (!continueAudioFlag) {
                        break;
                    }
                    continue;
                }
            } catch (IllegalStateException e) {
                Log.d("wait","broken");
            }

            clearMediaPlayer();
            if (!continueAudioFlag) {
                return;
            }

            lock[0] = true;


            if (isThereARealBlankBetweenFiles.get(i)) {
                if (wordList == null || traversedBlanks >= wordList.size() || wordList.get(traversedBlanks).equals("")) {
                    Log.d("Blank detected","blank");
                    traversedBlanks++;
                } else {
                    setMediaPlayerFile(wordList.get(traversedBlanks));
                    //traversedBlanks++;
                    if (!continueAudioFlag) {
                        return;
                    }
                    mediaPlayer.start();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Log.d("wait","broken");
                    }
                    try {
                        while (mediaPlayer.isPlaying()) {
                            if (!continueAudioFlag) {
                                break;
                            }
                            continue;
                        }
                    } catch (IllegalStateException e) {
                        Log.d("wait","broken");
                    }


                    try {
                        mediaPlayer.stop();
                    } catch (IllegalStateException e) {

                    }

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Log.d("wait","broken");
                    }
                    traversedBlanks++;
                }

            }
        }

    }

}