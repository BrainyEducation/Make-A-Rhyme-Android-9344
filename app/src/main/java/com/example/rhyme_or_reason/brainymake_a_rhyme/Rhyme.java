package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.Gravity.CENTER;

public class Rhyme extends AppCompatActivity implements View.OnClickListener {

    ImageView illustration;
    LinearLayout rhymeTextLL;

    int width;
    int height;
    int HEIGHT_UNIT;
    final double ASPECT_RATIO = 0.6802;
    final int TEXT_SIZE = 30;
    int CHARACTER_LIMIT = 20; // TODO: Set this dynamically based on screen width
    final char WORD_MARKER_START = '[';
    final char WORD_MARKER_END = ']';
    final String WORD_MARKERS = "[]";
    final int NUM_SPACES = 5;
    ArrayList<String> wordCodes = new ArrayList<>();
    StoryAudioManager storyAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storyAudioManager = new StoryAudioManager(this);
        setContentView(R.layout.activity_rhyme);

        sizingSetUp();

        loadIntentsAndViews();

        loadIllustrationImage();

        petPartyPicnicSetUp();
    }



    /**
     * TODO: Add Comment
     */
    public void onClick(View v) {

        Intent newIntent = new Intent(this, MainActivity.class);
        //newIntent.putExtra("word", selectedWord);
        //newIntent.putExtra("wrong_words", wrongWords);
        startActivityForResult(newIntent, 1);
        storyAudioManager.setContinueAudioFlag(false);

    }

    /**
     * Creates the buttons that serve as options for the word being spoken
     */
    public void loadIntentsAndViews()
    {
        illustration = findViewById(R.id.illustration);

        rhymeTextLL = findViewById(R.id.RhymeLL);
    }

    /**
     * Creates the buttons that serve as options for the word being spoken
     */
    public void loadIllustrationImage()
    {
        // Illustration boundaries
        LinearLayout.LayoutParams illustration_params = new LinearLayout.LayoutParams(
                width, (int)(width * ASPECT_RATIO)
        );
        illustration_params.setMargins(0, 0, 0, 0);
        illustration_params.gravity = CENTER;

        illustration.setLayoutParams(illustration_params);

        illustration.setBackgroundResource(R.drawable.background_pet_party_picnic);
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

    public void petPartyPicnicSetUp()
    {
        String storyText = "Once on a pretend time didn’t you tell me\n" +
                "How you hosted a happy pet-pair party?\n" +
                "You decided on goodies to buy at the store,\n" +
                "And with your [D_5_6_7] made a guest list of four,\n" +
                "Invited [E_5_6_7] and its [A_1] to attend,\n" +
                "And a [G_5_6_7] paired with its [B-1] best friend.\n" +
                "You spread a bright blanket with places for six,\n" +
                "and bowls for [D_5_6_7] food, [D_5_6_7] food, and a mix\n" +
                "of treats for the picky [G_5_6_7].  For the rest\n" +
                "cold milk, [J-14] on napkins, and the best\n" +
                "ice cream—chocolate, vanilla, strawberry—\n" +
                "topped with sprinkles, [K-14], a bright red cherry,\n" +
                "and butterscotch syrup!  Gosh it was yummy,\n" +
                "Yum yummy in my tum, tum, tummy!\n" +
                "The [D_5_6_7] and [D_5_6_7] and  [G_5_6_7] played so well,\n" +
                "And everyone loved it, so I heard tell,\n" +
                "The [A-1] bragged that you were so clever,\n" +
                "And the [B-1] declared it the best party ever!";

        int characterCounter = 0;
        String currentLine = "";

        ArrayList<String> listOfLines = new ArrayList<>();

        for (int index = 0; index < storyText.length(); ++index) {
            if (storyText.charAt(index) == ' ') {
                if (characterCounter > CHARACTER_LIMIT) {
                    characterCounter = 0;
                    listOfLines.add(currentLine);
                    currentLine = "";
                } else {
                    ++characterCounter;
                    currentLine += storyText.charAt(index);
                }
            } else if (storyText.charAt(index) == '\n') {
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
                characterCounter = 0;
                listOfLines.add(currentLine);
                currentLine = "";
            }
            else {
                // Normal character
                ++characterCounter;
                currentLine += storyText.charAt(index);
            }
        }

        for (int counter = 0; counter < listOfLines.size(); ++counter) {
            if (!listOfLines.get(counter).contains(WORD_MARKERS)) {
                TextView singleLine = new TextView(this);
                singleLine.setTextSize(TEXT_SIZE);

                singleLine.setText(listOfLines.get(counter));

                LinearLayout.LayoutParams line_params = new LinearLayout.LayoutParams(
                        width, (140) // TODO: Set to something meaningful
                );
                line_params.setMargins(0, 0, 0, 0);
                line_params.gravity = CENTER;

                singleLine.setLayoutParams(line_params);

                rhymeTextLL.addView(singleLine);

            } else {

                String trimmed = listOfLines.get(counter).substring(0, listOfLines.get(counter).length() - 2);

                RelativeLayout textAndButton = new RelativeLayout(this);

                textAndButton.setBackgroundColor(Color.GREEN);

                RelativeLayout.LayoutParams rL_params = new RelativeLayout.LayoutParams(
                        width, (140) // TODO: Set to something meaningful
                );
                rL_params.setMargins(0, 0, 0, 0);

                TextView singleLine = new TextView(this);
                singleLine.setTextSize(TEXT_SIZE);

                singleLine.setText(trimmed);
                singleLine.setBackgroundColor(Color.GRAY);

                RelativeLayout.LayoutParams line_params = new RelativeLayout.LayoutParams(
                        (3 * width) / 4, (140) // TODO: Set to something meaningful
                );
                line_params.setMargins(0, 0, 0, 0);
                line_params.addRule(RelativeLayout.ALIGN_BOTTOM);

                singleLine.setLayoutParams(line_params);
                singleLine.setId(1);

                Button blankButton = new Button(this);

                blankButton.setBackgroundColor(Color.BLUE);
                blankButton.setOnClickListener(this);


                RelativeLayout.LayoutParams button_params = new RelativeLayout.LayoutParams(
                        (width / 4), (140) // TODO: Set to something meaningful
                );

                button_params.setMargins(0,0,0,0);

                button_params.addRule(RelativeLayout.ALIGN_BOTTOM);

                blankButton.setLayoutParams(button_params);
                blankButton.setId(2);
                button_params.addRule(RelativeLayout.RIGHT_OF, singleLine.getId());


                textAndButton.addView(singleLine);
                textAndButton.addView(blankButton);

                rhymeTextLL.addView(textAndButton);
            }
        }

        storyAudioManager.setContinueAudioFlag(true);
        ArrayList<String> wordList = new ArrayList<String>();
        wordList.add("barn");
        Thread stopThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    storyAudioManager.play_story("Pet Party Picnic Story");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        stopThread.start();

    }
}
