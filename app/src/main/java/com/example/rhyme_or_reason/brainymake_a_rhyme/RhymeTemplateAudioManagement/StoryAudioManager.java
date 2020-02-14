package com.example.rhyme_or_reason.brainymake_a_rhyme.RhymeTemplateAudioManagement;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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

    public void setWordList(ArrayList<String> wordList) {
        this.wordList = wordList;
    }


    public MediaPlayer createMediaplayer(String s) {
        return MediaPlayer.create(context.getApplicationContext(), context.getResources().getIdentifier(s, "raw", context.getPackageName()));
    }

    private void setMediaPlayerFile(String s) {
        AssetFileDescriptor afd = context.getResources().openRawResourceFd(context.getResources().getIdentifier(s, "raw",context.getPackageName()));
        try
        {
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


    public void playStoryThread(final String storyName) {
        setContinueAudioFlag(true);
        Thread stopThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    play_story(storyName);
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
     * that have filled in the blank. Currently, it does not have a parameter for passing in a list
     *
     * @param storyName
     */
    private void play_story(String storyName) {
        ArrayList<String> fileNames = new ArrayList<>();
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
            setMediaPlayerFile(fileNames.get(i));
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                @Override
                public void onCompletion(MediaPlayer mp) {
                    lock[0] = false;
                }
            });

            //mediaPlayer.start();
            Log.d("YEET", "Play successful");
            while (lock[0] && mediaPlayer.isPlaying()) {
                if (!continueAudioFlag) {
                    break;
                }
                continue;
            }
            mediaPlayer.stop();
            if (!continueAudioFlag) {
                return;
            }


            lock[0] = true;

            if (isThereARealBlankBetweenFiles.get(i)) {
                if (wordList == null || wordList.get(traversedBlanks).equals("")) {
                    traversedBlanks++;
                } else {
                    //mpBlank = MediaPlayer.create(context.getApplicationContext(), context.getResources().getIdentifier(wordList.get(i), "raw", context.getPackageName()));
                    setMediaPlayerFile(wordList.get(traversedBlanks));
                    traversedBlanks++;
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        lock[0] = false;
                    }
                });
                mediaPlayer.start();

                /*
                while (lock[0] && mediaPlayer.isPlaying()) {
                    if (!continueAudioFlag) {
                        break;
                    }
                    //continue;
                }
                */

                while ( mediaPlayer.isPlaying()) {

                }

                mediaPlayer.stop();
            }
        }

    }

}
