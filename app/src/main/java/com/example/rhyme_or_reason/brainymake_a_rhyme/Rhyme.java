package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.rhyme_or_reason.brainymake_a_rhyme.RhymeTemplateAudioManagement.StoryAudioManager;
import com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem.EmailSystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.EnumMap;

import static android.view.Gravity.CENTER;

public class Rhyme extends AppCompatActivity implements View.OnClickListener {

    FrameLayout totalIllustration;
    ImageView illustration;
    LinearLayout rhymeTextLL;
    ImageView img;
    ImageButton rhymeUpBtn;
    ImageButton rhymeDownBtn;
    ScrollView rhymeScroll;
    ImageButton iB;
    Button emailButton;

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;
    ImageView img6;

    ArrayList<ImageView> illustrationIVs = new ArrayList<>();

    boolean insertedWord = false;
    int imageCoordIndex = 0;
    int width;
    int height;
    int HEIGHT_UNIT;
    //final double ASPECT_RATIO = 0.6802;
    int CHARACTER_LIMIT = 20; // TODO: Set this dynamically based on screen width
    final char WORD_MARKER_START = '[';
    final char WORD_MARKER_END = ']';
    final String WORD_MARKERS = "[*****]";
    final int NUM_SPACES = 5;
    boolean displayPlayButton = true;
    ArrayList<String> wordCodes = new ArrayList<>();
    ArrayList<Button> listOfButtons = new ArrayList<>();
    int selectedButtonIndex;
    ArrayList<double[]> imageCoords = new ArrayList<>();
    Typeface imprima;
    StoryAudioManager storyAudioManager;
    ArrayList<String> wordList = new ArrayList<>();

    String storyText = "";

    RhymeTemplate currRhyme;
    boolean playCooldown = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storyAudioManager = new StoryAudioManager(this);
        setContentView(R.layout.activity_rhyme);

        miscellaneousSetUp();

        sizingSetUp();

        loadIntentsAndViews();

        loadIllustrationImage();

        System.out.println("Hit Setup");

        if (currRhyme.getName().equals("Pet Party Picnic")) {
            setImageCoordsPetPartyPicnic();
            petPartyPicnicSetUp();
        } else if (currRhyme.getName().equals("Muddy Park")) {
            setImageCoordsMuddyPark();
            muddyParkSetUp();
        }

        storySetUp();

        audioSetUp();

        emailButton = (Button) findViewById(R.id.emailButton);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmailClick(v);
            }
        });
    }

    /**
     * Workaround designed to allow the pictures for pre-existing rhymes to show up in the
     * illustration when the page first loads. The page layout needs to be set for this to work,
     * hence why it cannot be included in onCreate.
     */
    @Override
    public void onWindowFocusChanged(boolean b)
    {
        super.onWindowFocusChanged(b);

        fillInRhyme();
    }

    public void onPlayAudio(View v) {
        //ImageButton iB = findViewById(R.id.playButton);

        if (displayPlayButton) {
            storyAudioManager.playStoryThread("Pet Party Picnic Story");
            displayPlayButton = false;
            iB.setImageResource(R.drawable.ic_pause);

        } else {
            storyAudioManager.setContinueAudioFlag(false);
            displayPlayButton = true;
            iB.setImageResource(R.drawable.ic_play);
        }
    }

    private void blockPlayButton() {
        Thread stopThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(333);
                    playCooldown = true;
                } catch (InterruptedException e) {
                }
            }
        };
        stopThread.start();
    }


    /*
    public void onPlayAudio(View v) {
        if (displayPlayButton && playCooldown) {
            storyAudioManager.clearMediaPlayer();
            storyAudioManager.playStoryThread("Pet Party Picnic Story");
            displayPlayButton = false;
            iB.setImageResource(R.drawable.ic_pause);
            playCooldown = false;
            blockPlayButton();
        } else if (!displayPlayButton && playCooldown){
            storyAudioManager.setContinueAudioFlag(false);
            storyAudioManager.clearMediaPlayer();
            displayPlayButton = true;
            iB.setImageResource(R.drawable.ic_play);
            playCooldown = false;
            blockPlayButton();
            storyAudioManager.clearMediaPlayer();
            storyAudioManager.setWordList(wordList);
        }
    }

    private void blockPlayButton() {
        Thread stopThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(333);
                    playCooldown = true;
                } catch (InterruptedException e) {
                }
            }
        };
        stopThread.start();
    }
    */

    /**
     * Occurs when the user selects an empty gap to fill with a word; saves the index of the button
     * that needs to be filled in.
     * TODO: Add Comment
     */
    public void onClick(View v) {

        Intent newIntent = new Intent(this, MainActivity.class);

        //forceStopAudio();
        storyAudioManager.setContinueAudioFlag(false);

        selectedButtonIndex = (Integer)v.getTag();

        startActivityForResult(newIntent, 1);
        storyAudioManager.setContinueAudioFlag(false);
        displayPlayButton = true;
        iB.setImageResource(R.drawable.ic_play);
    }

    /**
     * Creates the buttons that serve as options for the word being spoken
     */
    public void loadIntentsAndViews()
    {
        currRhyme = (RhymeTemplate)getIntent().getSerializableExtra("rhyme_template");

        if (currRhyme.getSavedIllustration() != null) {
            System.out.println(currRhyme.getSavedIllustration().toString());
        } else {
            System.out.println("Is Null.");
        }
        totalIllustration = findViewById(R.id.totalIllustration);

        illustration = findViewById(R.id.illustration);

        rhymeTextLL = findViewById(R.id.RhymeLL);

        rhymeScroll = findViewById(R.id.RhymeScrollView);

        iB = findViewById(R.id.playButton);

        rhymeUpBtn = findViewById(R.id.RhymeUpButton);
        rhymeDownBtn = findViewById(R.id.RhymeDownButton);
        rhymeUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rhymeScroll.smoothScrollBy(0, -500);
            }
        });
        rhymeDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rhymeScroll.smoothScrollBy(0, 500);
            }
        });
    }

    /**
     * Creates the buttons that serve as options for the word being spoken
     */
    public void loadIllustrationImage()
    {
        // Illustration boundaries
        LinearLayout.LayoutParams illustration_params = new LinearLayout.LayoutParams(
                width, (int)(width * Constants.ASPECT_RATIO)
        );
        illustration_params.setMargins(0, 0, 0, 0);
        //illustration_params.gravity = CENTER;

        totalIllustration.setLayoutParams(illustration_params);

        if (currRhyme.getName().equals("Pet Party Picnic")) {
            illustration.setBackgroundResource(R.drawable.background_pet_party_picnic);
        } else if (currRhyme.getName().equals("Muddy Park")) {
            illustration.setBackgroundResource(R.drawable.background_muddy_park);
        }
    }

    /**
     * Called when the page first loads; gets dimensions to help calculate how to dynamically lay
     * out the screen
     */
    public void sizingSetUp()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        width = screenSize.x;
        height = screenSize.y;

        HEIGHT_UNIT = height / 10; // Ten height units to allocate on the screen
    }

    public void petPartyPicnicSetUp() {
        storyText = "Once on a pretend time didn’t you tell me\n" +
                "How you hosted a happy pet-pair party?\n" +
                "You decided on goodies to buy at the store,\n" +
                "And with your [D_5_6_7] made a guest list of four,\n" +
                "Invited [E_5_6_7] and its [A_1] to attend,\n" +
                "And a [G_5_6_7] paired with its [B-1] best friend.\n" +
                "You spread a bright blanket with places for six,\n" +
                "and bowls for [D_5_6_7] food, [D_5_6_7] food, and a mix\n" +
                "of treats for the picky [G_5_6_7]. For the rest\n" +
                "cold milk, [J-14] on napkins, and the best\n" +
                "ice cream—chocolate, vanilla, strawberry—\n" +
                "topped with sprinkles, [K-14], a bright red cherry,\n" +
                "and butterscotch syrup! Gosh it was yummy,\n" +
                "Yum yummy in my tum, tum, tummy!\n" +
                "The [D_5_6_7] and [D_5_6_7] and [G_5_6_7] played so well,\n" +
                "And everyone loved it, so I heard tell,\n" +
                "The [A-1] bragged that you were so clever,\n" +
                "And the [B-1] declared it the best party ever!\n";

    }

    public void muddyParkSetUp()
    {
        storyText = "Once on a pretend time didn’t you tell me\n" +
                "You once walked your [D-3_5_6_7] to the park to run free,\n" +
                "There met [A-2] with a pet [E-3_5_6_7] come to play.\n" +
                "The pets scuffled and tussled every which way—\n" +
                "Your [D-3_5_6_7] chased the [E-3_5_6_7] all around,\n" +
                "The swings, towers, trash cans round the playground,\n" +
                "Under a see-saw the  [E-3_5_6_7] tried to hide,\n" +
                "Then climbed and tumbled down the slide,\n" +
                "Upset a kid’s play pool, shook water and dripped\n" +
                "A slippery mud puddle, and your [D-3_5_6_7] tripped,\n" +
                "The clumsy [B-1] who flopped and wallowed\n" +
                "And knocked down some poor [C-1] who followed\n" +
                "Trying to help, but flip-flopped down too,\n" +
                "Then [A-2] and the [B-1] both muddy with goo,\n" +
                "Dragged down with the [E-3_5_6_7], all three sunk\n" +
                "Down, in the oozy, slop-sloshery muck gunk.\n" +
                "How awfully— fun to sit sunk to the chin,\n" +
                "Your [D-3_5_6_7] and you grinned—and just had to jump in.\n" +
                "Grumpy grownups scrubbed clean your odds and ends,\n" +
                "But you made lots of new mud-loving friends!\n";
    }

    public void storySetUp() {
        int characterCounter = 0;
        String currentLine = "";

        ArrayList<String> listOfLines = new ArrayList<>();

        for (int index = 0; index < storyText.length(); ++index) {
            if (storyText.charAt(index) == '\n') {
                    characterCounter = 0;
                    listOfLines.add(currentLine);
                    currentLine = "";
            } else if (storyText.charAt(index) == WORD_MARKER_START) {
                ++index; // Move to the initial character of the word's 'code'
                String wordInfo = "";
                while (storyText.charAt(index) != WORD_MARKER_END) {
                    wordInfo += storyText.charAt(index);
                    ++index;
                }

                wordCodes.add(wordInfo);

                currentLine += WORD_MARKERS;
            } else {
                // Normal character
                ++characterCounter;
                currentLine += storyText.charAt(index);
            }
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double inchesWidth = Math.sqrt(Math.pow(dm.widthPixels/dm.xdpi,2));

        System.out.println("Inches Width: ");
        System.out.println(inchesWidth);

        int textSize = (int)(inchesWidth * 6); // Temporary for approximate scaling purposes

        int buttonTag = 0;

        for (int counter = 0; counter < listOfLines.size(); ++counter) {
            if (!listOfLines.get(counter).contains(WORD_MARKERS)) {
                TextView singleLine = new TextView(this);
                singleLine.setTextSize(textSize);

                String currLine = listOfLines.get(counter);

                if (currLine.charAt(0) == ' ') {
                    currLine = currLine.substring(1);
                }

                singleLine.setText(currLine);
                singleLine.setTypeface(imprima);

                LinearLayout.LayoutParams line_params = new LinearLayout.LayoutParams(
                        width, (140) // TODO: Set to something meaningful
                );
                line_params.setMargins(0, 0, 0, 0);
                line_params.gravity = CENTER;

                singleLine.setLayoutParams(line_params);

                rhymeTextLL.addView(singleLine);

            } else {

                String currLine = listOfLines.get(counter);

                RelativeLayout textAndButton = new RelativeLayout(this);

                RelativeLayout.LayoutParams rL_params = new RelativeLayout.LayoutParams(
                        width, (140) // TODO: Set to something meaningful
                );
                rL_params.setMargins(0, 0, 0, 0);

                String currPhrase = "";

                boolean inWordMarker = false;
                int idIndex = 1;

                for (int index = 0; index < currLine.length(); ++index) {
                    if (currLine.charAt(index) != WORD_MARKER_START && !inWordMarker) {
                        currPhrase += currLine.charAt(index);
                    } else if (currLine.charAt(index) == WORD_MARKER_START) {
                        inWordMarker = true;
                        TextView tempLine = new TextView(this);
                        tempLine.setTextSize(textSize);
                        tempLine.setTypeface(imprima);
                        tempLine.setText(currPhrase);
                        RelativeLayout.LayoutParams line_params = new RelativeLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, (140) // TODO: Set to something meaningful
                        );
                        line_params.setMargins(0, 0, 0, 0);
                        line_params.addRule(RelativeLayout.ALIGN_BOTTOM);
                        if (idIndex != 1) {
                            line_params.addRule(RelativeLayout.RIGHT_OF, idIndex - 1); // to the right of the button that comes before it
                        }

                        tempLine.setLayoutParams(line_params);
                        tempLine.setId(idIndex);
                        ++idIndex;

                        currPhrase = "";

                        textAndButton.addView(tempLine);

                    } else if (currLine.charAt(index) == WORD_MARKER_END) {
                        Button blankButton = new Button(this);
                        blankButton.setTextSize(textSize);
                        blankButton.setTypeface(imprima);

                        blankButton.setBackgroundColor(Color.LTGRAY);
                        blankButton.setOnClickListener(this);
                        blankButton.setTag(buttonTag);
                        ++buttonTag;

                        listOfButtons.add(blankButton);

                        RelativeLayout.LayoutParams button_params = new RelativeLayout.LayoutParams(
                                200, (100) // TODO: Set to something meaningful
                        );

                        button_params.setMargins(0, 0, 0, 0);

                        //button_params.addRule(RelativeLayout.ALIGN_BOTTOM);

                        blankButton.setLayoutParams(button_params);
                        blankButton.setId(idIndex);
                        button_params.addRule(RelativeLayout.RIGHT_OF, idIndex - 1); // to the right of the text that comes before it
                        ++idIndex;

                        blankButton.setPadding(0,0,0,0);
                        inWordMarker = false;

                        textAndButton.addView(blankButton);
                    }
                }

                if (!currPhrase.equals("")) {
                    inWordMarker = true;
                    TextView tempLine = new TextView(this);
                    tempLine.setTextSize(textSize);
                    tempLine.setTypeface(imprima);
                    tempLine.setText(currPhrase);
                    RelativeLayout.LayoutParams line_params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, (140) // TODO: Set to something meaningful
                    );
                    line_params.setMargins(0, 0, 0, 0);
                    line_params.addRule(RelativeLayout.ALIGN_BOTTOM);
                    if (idIndex != 1) {
                        line_params.addRule(RelativeLayout.RIGHT_OF, idIndex - 1); // to the right of the button that comes before it
                    }

                    tempLine.setLayoutParams(line_params);
                    tempLine.setId(idIndex);
                    ++idIndex;

                    currPhrase = "";

                    textAndButton.addView(tempLine);
                }

                rhymeTextLL.addView(textAndButton);
            }
        }
    }

    /**
     * Responsible for getting the result of the quiz. When RESULT_OK, then the quiz was passed,
     * so the word will be unlocked
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                insertedWord = true;

                //selectedWord.setUnlocked();
                Word selected = (Word)data.getSerializableExtra("word");

                wordList.set(selectedButtonIndex, selected.getText());

                // Update the wordList
                storyAudioManager.setWordList(wordList);
                currRhyme.updateChosenWords(selectedButtonIndex, selected);

                System.out.println(wordCodes.get(selectedButtonIndex));

                listOfButtons.get(selectedButtonIndex).setText(selected.getText());

                updateIllustration(selected, selectedButtonIndex);

            }
        }

    }

    /**
     * Handles set-up that would otherwise be in onCreate that doesn't belong in any other function
     */
    public void miscellaneousSetUp()
    {
        imprima = ResourcesCompat.getFont(this, R.font.imprima);

    }

    /**
     * Designed for Pet Party Picnic Rhyme Template. Finds the relative
     * position of the top left corner of the square-ish gray box in the illustration.
     * Starts at left picture and rotates counter-clockwise.
     */

    public void setImageCoordsPetPartyPicnic()
    {
        double[] image1 = {width * .0985, width * Constants.ASPECT_RATIO * .3415};
        double[] image2 = {width * .1593, width * Constants.ASPECT_RATIO * .5431};
        double[] image3 = {width * .4832, width * Constants.ASPECT_RATIO * .5585};
        double[] image4 = {width * .6321, width * Constants.ASPECT_RATIO * .4};
        double[] image5 = {width * .5251, width * Constants.ASPECT_RATIO * .16};
        double[] image6 = {width * .3312, width * Constants.ASPECT_RATIO * .1046};

        imageCoords.add(image1);
        imageCoords.add(image2);
        imageCoords.add(image3);
        imageCoords.add(image4);
        imageCoords.add(image5);
        imageCoords.add(image6);
    }

    /**
     * Designed for Muddy Park Rhyme Template. Finds the relative
     * position of the top left corner of the square-ish gray box in the illustration.
     * Starts at left picture and rotates counter-clockwise.
     */
    public void setImageCoordsMuddyPark()
    {
        double[] image1 = {width * .47, width * Constants.ASPECT_RATIO * .245};
        double[] image2 = {width * .455, width * Constants.ASPECT_RATIO * .72};
        double[] image3 = {width * .574, width * Constants.ASPECT_RATIO * .8};
        double[] image4 = {width * .68, width * Constants.ASPECT_RATIO * .675};
        double[] image5 = {width * .7646, width * Constants.ASPECT_RATIO * .4578};

        imageCoords.add(image1);
        imageCoords.add(image2);
        imageCoords.add(image3);
        imageCoords.add(image4);
        imageCoords.add(image5);
    }

    /**
     * Sets up the top illustration depending on which rhyme template the user has selected
     */
    public void loadIllustrationIVs() {

        if (currRhyme.getName().equals("Muddy Park")) {
            setUpMuddyPark();
        } else if (currRhyme.getName().equals("Pet Party Picnic")) {
            setUpPetPartyPicnic();
        }
    }

    /**
     * Sets up the illustration and its image views inside for Muddy Park Rhyme Template
     */
    public void setUpMuddyPark()
    {
        img1 = new ImageView(this);
        img2 = new ImageView(this);
        img3 = new ImageView(this);
        img4 = new ImageView(this);
        img5 = new ImageView(this);

        //img1.setBackgroundColor(Color.BLUE);

        // Image View (Picture of Word Being Quizzed)
        FrameLayout.LayoutParams img_params1 = new FrameLayout.LayoutParams(
                (int) (totalIllustration.getHeight() * Constants.MUDDY_PARK_PICTURE_HEIGHT_SCALE), (int) (totalIllustration.getHeight() * Constants.MUDDY_PARK_PICTURE_HEIGHT_SCALE)
        );

        img_params1.setMargins((int) (imageCoords.get(0)[0]), (int) (imageCoords.get(0)[1]), 0, 0);
        img1.setLayoutParams(img_params1);

        FrameLayout.LayoutParams img_params2 = new FrameLayout.LayoutParams(
                (int) (totalIllustration.getHeight() * Constants.MUDDY_PARK_PICTURE_HEIGHT_SCALE), (int) (totalIllustration.getHeight() * Constants.MUDDY_PARK_PICTURE_HEIGHT_SCALE)
        );

        img_params2.setMargins((int) (imageCoords.get(1)[0]), (int) (imageCoords.get(1)[1]), 0, 0);
        img2.setLayoutParams(img_params2);

        FrameLayout.LayoutParams img_params3 = new FrameLayout.LayoutParams(
                (int) (totalIllustration.getHeight() * Constants.MUDDY_PARK_PICTURE_HEIGHT_SCALE), (int) (totalIllustration.getHeight() * Constants.MUDDY_PARK_PICTURE_HEIGHT_SCALE)
        );

        img_params3.setMargins((int) (imageCoords.get(2)[0]), (int) (imageCoords.get(2)[1]), 0, 0);
        img3.setLayoutParams(img_params3);

        FrameLayout.LayoutParams img_params4 = new FrameLayout.LayoutParams(
                (int) (totalIllustration.getHeight() * Constants.MUDDY_PARK_PICTURE_HEIGHT_SCALE), (int) (totalIllustration.getHeight() * Constants.MUDDY_PARK_PICTURE_HEIGHT_SCALE)
        );

        img_params4.setMargins((int) (imageCoords.get(3)[0]), (int) (imageCoords.get(3)[1]), 0, 0);
        img4.setLayoutParams(img_params4);

        FrameLayout.LayoutParams img_params5 = new FrameLayout.LayoutParams(
                (int) (totalIllustration.getHeight() * Constants.MUDDY_PARK_PICTURE_HEIGHT_SCALE), (int) (totalIllustration.getHeight() * Constants.MUDDY_PARK_PICTURE_HEIGHT_SCALE)
        );

        img_params5.setMargins((int) (imageCoords.get(4)[0]), (int) (imageCoords.get(4)[1]), 0, 0);
        img5.setLayoutParams(img_params5);


        illustrationIVs.add(img1);
        illustrationIVs.add(img2);
        illustrationIVs.add(img3);
        illustrationIVs.add(img4);
        illustrationIVs.add(img5);

        //this.addContentView(img1, img_params1);

        totalIllustration.addView(img1);
        totalIllustration.addView(img2);
        totalIllustration.addView(img3);
        totalIllustration.addView(img4);
        totalIllustration.addView(img5);
    }
    /**
     * Sets up the illustration and its image views inside for Pet Party Picnic Rhyme Template
     */
    public void setUpPetPartyPicnic()
    {
        img1 = new ImageView(this);
        img2 = new ImageView(this);
        img3 = new ImageView(this);
        img4 = new ImageView(this);
        img5 = new ImageView(this);
        img6 = new ImageView(this);

        //img1.setBackgroundColor(Color.BLUE);

        // Image View (Picture of Word Being Quizzed)
        FrameLayout.LayoutParams img_params1 = new FrameLayout.LayoutParams(
                (int) (totalIllustration.getHeight() * Constants.PET_PARTY_PICTURE_HEIGHT_SCALE), (int) (totalIllustration.getHeight() * Constants.PET_PARTY_PICTURE_HEIGHT_SCALE)
        );

        img_params1.setMargins((int) (imageCoords.get(0)[0]), (int) (imageCoords.get(0)[1]), 0, 0);
        img1.setLayoutParams(img_params1);

        FrameLayout.LayoutParams img_params2 = new FrameLayout.LayoutParams(
                (int) (totalIllustration.getHeight() * Constants.PET_PARTY_PICTURE_HEIGHT_SCALE), (int) (totalIllustration.getHeight() * Constants.PET_PARTY_PICTURE_HEIGHT_SCALE)
        );

        img_params2.setMargins((int) (imageCoords.get(1)[0]), (int) (imageCoords.get(1)[1]), 0, 0);
        img2.setLayoutParams(img_params2);

        FrameLayout.LayoutParams img_params3 = new FrameLayout.LayoutParams(
                (int) (totalIllustration.getHeight() * Constants.PET_PARTY_PICTURE_HEIGHT_SCALE), (int) (totalIllustration.getHeight() * Constants.PET_PARTY_PICTURE_HEIGHT_SCALE)
        );

        img_params3.setMargins((int) (imageCoords.get(2)[0]), (int) (imageCoords.get(2)[1]), 0, 0);
        img3.setLayoutParams(img_params3);

        FrameLayout.LayoutParams img_params4 = new FrameLayout.LayoutParams(
                (int) (totalIllustration.getHeight() * Constants.PET_PARTY_PICTURE_HEIGHT_SCALE), (int) (totalIllustration.getHeight() * Constants.PET_PARTY_PICTURE_HEIGHT_SCALE)
        );

        img_params4.setMargins((int) (imageCoords.get(3)[0]), (int) (imageCoords.get(3)[1]), 0, 0);
        img4.setLayoutParams(img_params4);

        FrameLayout.LayoutParams img_params5 = new FrameLayout.LayoutParams(
                (int) (totalIllustration.getHeight() * Constants.PET_PARTY_PICTURE_HEIGHT_SCALE), (int) (totalIllustration.getHeight() * Constants.PET_PARTY_PICTURE_HEIGHT_SCALE)
        );

        img_params5.setMargins((int) (imageCoords.get(4)[0]), (int) (imageCoords.get(4)[1]), 0, 0);
        img5.setLayoutParams(img_params5);

        FrameLayout.LayoutParams img_params6 = new FrameLayout.LayoutParams(
                (int) (totalIllustration.getHeight() * Constants.PET_PARTY_PICTURE_HEIGHT_SCALE), (int) (totalIllustration.getHeight() * Constants.PET_PARTY_PICTURE_HEIGHT_SCALE)
        );

        img_params6.setMargins((int) (imageCoords.get(5)[0]), (int) (imageCoords.get(5)[1]), 0, 0);
        img6.setLayoutParams(img_params6);

        illustrationIVs.add(img1);
        illustrationIVs.add(img2);
        illustrationIVs.add(img3);
        illustrationIVs.add(img4);
        illustrationIVs.add(img5);
        illustrationIVs.add(img6);

        //this.addContentView(img1, img_params1);

        totalIllustration.addView(img1);
        totalIllustration.addView(img2);
        totalIllustration.addView(img3);
        totalIllustration.addView(img4);
        totalIllustration.addView(img5);
        totalIllustration.addView(img6);
    }

    /**
     * Returns to the new/existing rhyme selection page and saves the current rhyme
     */
    public void ClickedBackButton(View view) {


        displayPlayButton = true;
        storyAudioManager.setContinueAudioFlag(false);

        //forceStopAudio();


        //Kyle
        Bitmap illustrationBitmap = takeScreenShot();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        illustrationBitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);

        // Create a byte array from ByteArrayOutputStream
        byte[] byteArray = stream.toByteArray();


        if (insertedWord) {
            currRhyme.setSavedIllustration(byteArray);
            currRhyme.saveRhymeTemplate(this.getApplicationContext());
        }
        onBackPressed();
    }

    public void fillInRhyme()
    {
        for (int index = 0; index < listOfButtons.size(); ++index) {
            Word currWord = currRhyme.getChosenWords().get(index);
            if (currWord != null && !currWord.getText().equals("")) {
                listOfButtons.get(index).setText(currWord.getText());
                wordList.set(index, currWord.getText());
                storyAudioManager.setWordList(wordList);
                updateIllustration(currWord, index);
            }
        }
    }

    /**
     * Updates the top illustration to contain the new picture for the provided word
     */
    public void updateIllustration(Word selectedWord, int buttonIndex)
    {
        if (buttonIndex < imageCoords.size()) {

            try {
                totalIllustration.removeView(illustrationIVs.get(buttonIndex));
            } catch (Exception e) {
                // Image Views haven't been set yet; create them
                Log.println(Log.WARN, "Remove ImgView", "No pre-existing image view found");
                loadIllustrationIVs();
                totalIllustration.removeView(illustrationIVs.get(buttonIndex)); // now can remove the image
            }

            int resourceID = getResources().getIdentifier(selectedWord.getImageName(), "drawable", getPackageName());
            illustrationIVs.get(buttonIndex).setImageResource(resourceID);
            //img.setImageResource(resourceID);
            totalIllustration.addView(illustrationIVs.get(buttonIndex));
            //imageCoordIndex++;
        }
    }

    /**
     * Currently adds 15 blanks (For Pet Party Picnic) that will be changed as words are added;
     * this will need to be parameterized when we accomodate for different rhyme templates.
     */
    public void audioSetUp()
    {
        for (int index = 0; index < currRhyme.getNumBlanks(); ++index) {
            wordList.add("");
        }

        storyAudioManager.setWordList(wordList);
    }

    /**
     * Takes a screenshot of only the illustration part of the screen to be used in emails and
     * previews on the choose existing rhymes screen.
     */
    public Bitmap takeScreenShot() {
        totalIllustration.setDrawingCacheEnabled(true);
        Bitmap illustrationBitmap = Bitmap.createBitmap(totalIllustration.getDrawingCache());
        totalIllustration.setDrawingCacheEnabled(false);
        return illustrationBitmap;
    }

    public void onThing(View v) {

    }

    public void onEmailClick(View v) {

        //forceStopAudio();
        storyAudioManager.setContinueAudioFlag(false);
        displayPlayButton = true;

        Intent newIntent = new Intent(Rhyme.this,EmailActivity.class);
        View parentView = findViewById(R.id.totalIllustration);
        String path = EmailSystem.saveViewAsPngAndReturnPath(parentView, this);


        //newIntent.putExtra("current_rhyme", currRhyme);
        newIntent.putStringArrayListExtra("rhyme_words", wordList);
        //newIntent.putExtra("illustration", byteArray);
        newIntent.putExtra("imageUri", path);
        newIntent.putExtra("general_rhyme_text", storyText);

        startActivity(newIntent);
    }


    /*
    private void forceStopAudio() {
        try {
            storyAudioManager.setContinueAudioFlag(false);
        } catch (IllegalStateException e) {
            try {
                storyAudioManager.clearMediaPlayer();
            } catch (Exception nestedException) {
            }
        }
    }
    */
}