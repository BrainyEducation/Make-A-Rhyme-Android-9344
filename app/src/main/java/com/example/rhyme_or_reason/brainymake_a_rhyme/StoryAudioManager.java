package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class StoryAudioManager {

    private Context context;
    private String resourcePath;
    private StoryAudioMetadataManager mp3Data;

    public StoryAudioManager(Context context) {
        this.context = context;
        String packageName = this.context.getPackageName();
        resourcePath = "android.resource://" + packageName + "/raw/";
        mp3Data = new StoryAudioMetadataManager();
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
        int numberOfFiles = mp3Data.storyNameToNumFilesMap.get(storyName);
        String fileprefix = mp3Data.storyNameToFileNameMap.get(storyName);
        for (int i = 1; i <= numberOfFiles; i++) {
            MediaPlayer mp = MediaPlayer.create(context.getApplicationContext(), context.getResources().getIdentifier(fileprefix + i,"raw",context.getPackageName()));
            mediaPlayerSequence.add(mp);
        }

        for (int i = 1; i <= numberOfFiles; i++) {
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
                continue;
            }
        }

    }

}
