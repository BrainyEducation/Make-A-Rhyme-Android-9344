package com.example.rhyme_or_reason.brainymake_a_rhyme;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Class designed purely to hold values that should hold constant throughout the application.
 * This is done to prevent values from being defined many times with potentially inconsistent
 * values.
 */
public class Constants {
    static final double ASPECT_RATIO = 0.6802;
    static final int SEPARATOR_HEIGHT = 20;
    static final int STANDARD_TEXT_SIZE = 30;
    static final int HEADER_TEXT_SIZE = 40;
    static final int SMALL_TEXT_SIZE = 20;
    static final double PET_PARTY_PICTURE_HEIGHT_SCALE = .1615;
    static final double MUDDY_PARK_PICTURE_HEIGHT_SCALE = .10;
    static final double STRANGER_PARADE_PICTURE_HEIGHT_SCALE = .1;

    static final float LOCKED_ALPHA = 0.2f;
    static ArrayList<String> backgroundNames = new ArrayList<>( Arrays.asList("Pet Party Picnic", "Muddy Park", "Stranger Parade"));
}
