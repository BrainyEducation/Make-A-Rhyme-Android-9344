package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class StoryAudioManager {

    private Context context;
    private String resourcePath;
    private StoryAudioConstants mp3Data;
    private boolean continueAudioFlag = true;

    public StoryAudioManager(Context context) {
        this.context = context;
        String packageName = this.context.getPackageName();
        resourcePath = "android.resource://" + packageName + "/raw/";
        mp3Data = new StoryAudioConstants();
    }

    public void setContinueAudioFlag(boolean flag) {
        continueAudioFlag = flag;
    }


    public void exampleAudio() {

        String filename = resourcePath + "airplane.mp3";

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

    public void play_story(String storyName) {
        ArrayList<MediaPlayer> mediaPlayerSequence = new ArrayList<MediaPlayer>();
        StoryAudioConstantContainer constantContainer = mp3Data.storyNameToConstantContainer.get(storyName);
        int numberOfFiles = constantContainer.numberOfFiles;
        String fileprefix = constantContainer.fileNamePrefix;
        ArrayList<Boolean> isThereARealBlankBetweenFiles = constantContainer.checkIfNeedSubstitution;
        for (int i = 1; i <= numberOfFiles; i++) {
            MediaPlayer mp = MediaPlayer.create(context.getApplicationContext(), context.getResources().getIdentifier(fileprefix + i,"raw",context.getPackageName()));
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
                MediaPlayer mpBlank = MediaPlayer.create(context.getApplicationContext(), context.getResources().getIdentifier("airplane","raw",context.getPackageName()));
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
