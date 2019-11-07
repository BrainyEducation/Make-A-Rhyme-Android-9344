package com.example.rhyme_or_reason.brainymake_a_rhyme;

import java.io.Serializable;

public class Word implements Serializable {

    private String text;
    private Boolean locked;
    private String imageName;
    private String audioName;

    public Word(String text, Boolean locked, String imageName, String audioName)
    {
        this.text = text;
        this.locked = locked;
        this.imageName = imageName;
        this.audioName = audioName;
    }

    public void setUnlocked()
    {
        this.locked = false;
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
}