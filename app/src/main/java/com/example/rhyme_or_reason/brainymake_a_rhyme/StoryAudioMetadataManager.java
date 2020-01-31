package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.res.AssetManager;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class StoryAudioMetadataManager {

    public HashMap<String, String> storyNameToFileNameMap;
    public HashMap<String, Integer> storyNameToNumFilesMap;

    public StoryAudioMetadataManager() {
        storyNameToFileNameMap = new HashMap<String, String>();
        storyNameToNumFilesMap = new HashMap<String, Integer>();

        storyNameToFileNameMap.put("Pet Party Picnic Story", "spr_1_");
        storyNameToNumFilesMap.put("Pet Party Picnic Story", 20);

    }
}
