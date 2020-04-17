package com.example.rhyme_or_reason.brainymake_a_rhyme.RhymeTemplateAudioManagement;

import java.util.ArrayList;

public class StoryAudioConstantContainer {

    //files belonging to the same story follow the convention of spr_x_ where x is a number
    public String fileNamePrefix;

    //the number of audio files that exist for that story
    public int numberOfFiles;

    //this array list is populated with true if between audio file x and audio file x+1, there is a word between them
    //and false if the audio file x+1 immediately follows audio file x
    public ArrayList<Boolean> checkIfNeedSubstitution;

    public StoryAudioConstantContainer(String fileNamePrefix, int numberOfFiles, ArrayList<Boolean> checkIfNeedSubstitution) {
        this.fileNamePrefix = fileNamePrefix;
        this.numberOfFiles = numberOfFiles;
        this.checkIfNeedSubstitution = checkIfNeedSubstitution;
    }
}
