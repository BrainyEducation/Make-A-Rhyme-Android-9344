package com.example.rhyme_or_reason.brainymake_a_rhyme.RhymeTemplateAudioManagement;

import java.util.ArrayList;
import java.util.HashMap;

public class StoryAudioConstants {

    public HashMap<String, StoryAudioConstantContainer> storyNameToConstantContainer;

    public StoryAudioConstants() {
        storyNameToConstantContainer = new HashMap<String, StoryAudioConstantContainer>();
        LoadPetPartyPicnicStory();
        LoadMuddyPark();
        LoadStrangerParade();
    }

    private void LoadPetPartyPicnicStory() {
        String petPartyPicnicFilePrefix = "spr_1_";
        int petPartyPicinicFileQuantity = 20;
        //note that this list of booleans must match the number of files. The last boolean should ALWAYS BE FALSE
        //boolean[] petPartyPicnicInterruptBooleans = {false, false, true, true, true, true, true, false, true, true, true, true, true, true, true, true, false, true, true, false};
        boolean[] petPartyPicnicInterruptBooleans = {false, false, true, true, true, true, true, false, true, true, true, true, true, true, true, true, false, true, true, false};
        ArrayList<Boolean> petPartyPicnicInterrupts = new ArrayList<Boolean>();
        for (boolean b : petPartyPicnicInterruptBooleans) {
            petPartyPicnicInterrupts.add(b);
        }

        //
        StoryAudioConstantContainer constantContainer = new StoryAudioConstantContainer(petPartyPicnicFilePrefix, petPartyPicinicFileQuantity, petPartyPicnicInterrupts);
        storyNameToConstantContainer.put("Pet Party Picnic", constantContainer);
    }

    private void LoadMuddyPark() {
        String prefix = "spr_3_";
        int fileQuantity = 16;
        boolean[] interruptBooleans = {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false};
        ArrayList<Boolean> booleanList = new ArrayList<Boolean>();
        for (boolean b : interruptBooleans) {
            booleanList.add(b);
        }
        StoryAudioConstantContainer constantContainer = new StoryAudioConstantContainer(prefix, fileQuantity,booleanList);
        storyNameToConstantContainer.put("Muddy Park", constantContainer);
    }

    private void LoadStrangerParade() {
        String prefix = "spr_4_";
        int fileQuantity = 20;
        boolean[] interruptBooleans = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false};
        ArrayList<Boolean> booleanList = new ArrayList<Boolean>();
        for (boolean b : interruptBooleans) {
            booleanList.add(b);
        }
        StoryAudioConstantContainer constantContainer = new StoryAudioConstantContainer(prefix, fileQuantity,booleanList);
        storyNameToConstantContainer.put("Stranger Parade", constantContainer);
    }
}