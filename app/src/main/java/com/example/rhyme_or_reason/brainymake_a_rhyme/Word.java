package com.example.rhyme_or_reason.brainymake_a_rhyme;

public class Word {

    private String text;
    private Boolean locked;

    public Word(String text, Boolean locked)
    {
        this.text = text;
        this.locked = locked;
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

}