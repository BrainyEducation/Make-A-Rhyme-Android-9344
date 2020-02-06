package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;

public class StoryAudioManager {

    private Context context;

    //contains constants defined for how the mp3 files will be sequentially played
    private StoryAudioConstants mp3Data;

    //toggling this to false prevents any audio in play_story from running
    private boolean continueAudioFlag = true;

    private ArrayList<String> wordList;

    public StoryAudioManager(Context context) {
        this.context = context;
        String packageName = this.context.getPackageName();
        mp3Data = new StoryAudioConstants();
    }

    public void setContinueAudioFlag(boolean flag) {
        continueAudioFlag = flag;
    }

    public void setWordList(ArrayList<String> wordList) {
        this.wordList = wordList;
    }


    public void exampleAudio() {
        final MediaPlayer mp = MediaPlayer.create(context.getApplicationContext(), context.getResources().getIdentifier("airplane","raw",context.getPackageName()));
        mp.start();
        Thread stopThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(500);
                    mp.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        stopThread.start();
    }

    public MediaPlayer createMediaplayer(String s) {
        return MediaPlayer.create(context.getApplicationContext(), context.getResources().getIdentifier(s, "raw", context.getPackageName()));
    }

    /**
     * Syncs up the audio files for the story with the audio files that correspond to words
     * that have filled in the blank. Currently, it does not have a parameter for passing in a list
     *
     * @param storyName
     */
    public void play_story(String storyName) {
        ArrayList<MediaPlayer> mediaPlayerSequence = new ArrayList<MediaPlayer>();
        StoryAudioConstantContainer constantContainer = mp3Data.storyNameToConstantContainer.get(storyName);
        int numberOfFiles = constantContainer.numberOfFiles;
        String fileprefix = constantContainer.fileNamePrefix;
        ArrayList<Boolean> isThereARealBlankBetweenFiles = constantContainer.checkIfNeedSubstitution;
        for (int i = 1; i <= numberOfFiles; i++) {
            //MediaPlayer mp = MediaPlayer.create(context.getApplicationContext(), context.getResources().getIdentifier(fileprefix + i,"raw",context.getPackageName()));
            MediaPlayer mp = createMediaplayer(fileprefix + i);
            mediaPlayerSequence.add(mp);
        }

        for (int i = 0; i < numberOfFiles; i++) {
            final boolean[] lock = {true};
            MediaPlayer mp = mediaPlayerSequence.get(i);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                @Override
                public void onCompletion(MediaPlayer mp) {
                    lock[0] = false;
                }
            });
            mp.start();
            while (lock[0]) {
                if (!continueAudioFlag) {
                    mp.stop();
                    return;
                }
                continue;
            }
            mp.stop();

            lock[0] = true;

            if (isThereARealBlankBetweenFiles.get(i)) {
                MediaPlayer mpBlank;
                if (wordList == null || i >= wordList.size()) {
                    //mpBlank = MediaPlayer.create(context.getApplicationContext(), context.getResources().getIdentifier("airplane","raw",context.getPackageName()));
                    mpBlank = createMediaplayer("airplane");
                } else {
                    //mpBlank = MediaPlayer.create(context.getApplicationContext(), context.getResources().getIdentifier(wordList.get(i), "raw", context.getPackageName()));
                    mpBlank = createMediaplayer(wordList.get(i));
                    if (mpBlank == null) {
                        mpBlank = createMediaplayer("airplane");
                    }
                }
                mpBlank.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        lock[0] = false;
                    }
                });
                mpBlank.start();
                while (lock[0]) {
                    if (!continueAudioFlag) {
                        mpBlank.stop();
                        return;
                    }
                    continue;
                }
                mpBlank.stop();
            }
        }

    }

}
