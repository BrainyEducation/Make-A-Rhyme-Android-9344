package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RhymeTemplate implements Serializable {
    private String name;
    private String imageName;
    private Boolean locked;
    private int numImages;

    public RhymeTemplate(String name, String imageName, Boolean locked, int numImages) {
        this.name = name;
        this.imageName = imageName;
        this.locked = locked;
        this.numImages = numImages;
    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }

    public Boolean getLocked() {
        return locked;
    }

    public int getNumImages() {
        return numImages;
    }
}
