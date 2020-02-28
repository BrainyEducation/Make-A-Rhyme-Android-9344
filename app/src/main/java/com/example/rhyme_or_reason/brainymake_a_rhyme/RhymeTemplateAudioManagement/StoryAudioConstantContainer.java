package com.example.rhyme_or_reason.brainymake_a_rhyme.RhymeTemplateAudioManagement;

import java.util.ArrayList;

public class StoryAudioConstantContainer {
    public String fileNamePrefix;
    public int numberOfFiles;
    public ArrayList<Boolean> checkIfNeedSubstitution;

    public StoryAudioConstantContainer(String fileNamePrefix, int numberOfFiles, ArrayList<Boolean> checkIfNeedSubstitution) {
        this.fileNamePrefix = fileNamePrefix;
        this.numberOfFiles = numberOfFiles;
        this.checkIfNeedSubstitution = checkIfNeedSubstitution;
    }
}
