package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.view.Gravity.CENTER;

public class Quiz extends AppCompatActivity implements View.OnClickListener {

    Word lockedWord;
    ArrayList<String> categoryWords;
    ArrayList<String> letterWords;
    ArrayList<String> lengthWords;
    ArrayList<String> otherWords;
    String correctChoiceNum = "0";
    Button choice1, choice2, choice3, choice4;
    ImageView starIV1, starIV2, starIV3;
    int correctStreak = 0;
    Typeface imprima;
    //final String CHOICE_BOX_COLOR = "#9370DB";
    int height, width;
    int HEIGHT_UNIT;
    ImageView topIV;
    RelativeLayout topBar;
    ImageButton repeatIV;
    LinearLayout topTwoOptions, bottomTwoOptions, encompassing;
    int lockedWordImageResourceID;
    final int NUM_CHOICES = 4;
    final int TEXT_SIZE = 40;
    int buttonColor = Color.parseColor("#f4faf8");
    //final int textSize = 40;

    /**
     * Runs when the activity launches; sets up the screen elements for selecting the word
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        loadIntentsAndViews();
        sizingSetUp();
        miscellaneousSetUp();

        createChoiceButtons();
        setChoices();

        createStarButtons();

        performLayout();

        playWordAudio();
    }
    /**
     * Picks a word from the selected category. If the category is empty, picks a word from otherwords
     */
    public String getRandomWord(ArrayList<String> list)
    {
        if(list.size() == 0)
            return getRandomWord(otherWords);
        int randomChoice = (new Random()).nextInt(list.size());
        String wrongWord = list.get(randomChoice);
        return wrongWord;
    }
    /**
     * Picks out the wrong answers and places them in the boxes; then places the correct answer
     * randomly in one of the boxes
     */
    public void setChoices()
    {
        String wrongWord1 = getRandomWord(categoryWords);
        String wrongWord2 = getRandomWord(lengthWords);
        String wrongWord3 = getRandomWord(letterWords);
        //pick the order of the words
        ArrayList<Integer> order = new ArrayList<>();
        order.add(0);
        order.add(1);
        order.add(2);
        order.add(3);
        Collections.shuffle(order);
        String[] words = new String[]{lockedWord.getText(), wrongWord1, wrongWord2, wrongWord3};


        choice1.setText(words[order.get(0)].toLowerCase());
        choice1.setBackgroundColor(buttonColor);

        choice2.setText(words[order.get(1)].toLowerCase());
        choice2.setBackgroundColor(buttonColor);

        choice3.setText(words[order.get(2)].toLowerCase());
        choice3.setBackgroundColor(buttonColor);

        choice4.setText(words[order.get(3)].toLowerCase());
        choice4.setBackgroundColor(buttonColor);

        correctChoiceNum = ""+(order.indexOf(0)+1);
    }

    /**
     * Responsible for handling clicks for the four choices. Will also recognize when the repeat
     * button has been pressed and play the word again.
     */
    public void onClick(View v)
    {
        if (v.getTag().equals("Repeat Button")) {
            playWordAudio();
        } else {
            if (correctChoiceNum.equals(v.getTag())) {
                correctStreak += 1;
                if (correctStreak == 1) {
                    starIV1.setVisibility(View.VISIBLE);
                    starIV1.setBackgroundResource(R.drawable.gold_star_blank);
                } else if (correctStreak == 2) {
                    starIV2.setVisibility(View.VISIBLE);
                    starIV2.setBackgroundResource(R.drawable.gold_star_blank);
                } else if (correctStreak == 3) {
                    starIV3.setVisibility(View.VISIBLE);
                    starIV3.setBackgroundResource(R.drawable.gold_star_blank);

                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("Congratulations!");
                    dialog.setMessage("You Unlocked " + lockedWord.getText());
                    dialog.show();

                    // Exit quiz
                    Handler returnHandler = new Handler();
                    returnHandler.postDelayed(new Runnable() {
                        public void run() {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result", true);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }, 2000);   // 2 seconds
                }
            } else {
                // Switch to silver stars

                starIV1.setBackgroundResource(R.drawable.silver_star_blank);
                starIV2.setBackgroundResource(R.drawable.silver_star_blank);
                starIV3.setBackgroundResource(R.drawable.silver_star_blank);

                correctStreak = 0;
            }

            // Don't want any sound when going back to the main menu
            if (correctStreak != 3) {
                setChoices();
                playWordAudio();
            }
        }
    }

    /**
     * Handles back press click; takes user back to previous activity (word select screen)
     *
     * @param view Automatic parameter for user interaction
     */
    public void ClickedBackButton(View view) {
        onBackPressed();
    }

    /**
     * Called when the user has passed the quiz and acknowledges the congratulatory prompt
     */
    public void onPassedQuiz()
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",true);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    /**
     * Creates the buttons that serve as options for the word being spoken
     */
    public void createChoiceButtons()
    {
        choice1 = new Button(this);
        choice1.setTag("1");
        choice1.setTextSize(TEXT_SIZE);
        choice1.setTypeface(imprima);
        choice2 = new Button(this);
        choice2.setTag("2");
        choice2.setTextSize(TEXT_SIZE);
        choice2.setTypeface(imprima);
        choice3 = new Button(this);
        choice3.setTag("3");
        choice3.setTextSize(TEXT_SIZE);
        choice3.setTypeface(imprima);
        choice4 = new Button(this);
        choice4.setTag("4");
        choice4.setTextSize(TEXT_SIZE);
        choice4.setTypeface(imprima);

        choice1.setOnClickListener(this);
        choice2.setOnClickListener(this);
        choice3.setOnClickListener(this);
        choice4.setOnClickListener(this);

        topTwoOptions.addView(choice1);
        topTwoOptions.addView(choice2);
        bottomTwoOptions.addView(choice3);
        bottomTwoOptions.addView(choice4);
    }

    /**
     * Creates the buttons that serve as options for the word being spoken
     */
    public void loadIntentsAndViews()
    {
        lockedWord = (Word)getIntent().getSerializableExtra("word");
        categoryWords = getIntent().getStringArrayListExtra("category_words");
        lengthWords = getIntent().getStringArrayListExtra("length_words");
        letterWords = getIntent().getStringArrayListExtra("letter_words");
        otherWords = getIntent().getStringArrayListExtra("other_words");

        topIV = findViewById(R.id.TopImageView);
        topBar = findViewById(R.id.topLayout);
        repeatIV = findViewById(R.id.RepeatButton);
        topTwoOptions = findViewById(R.id.TopTwoOptions);
        bottomTwoOptions = findViewById(R.id.BottomTwoOptions);
        encompassing = findViewById(R.id.EncompassingLL);
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

    /**
     * Handles set-up that would otherwise be in onCreate that doesn't belong in any other function
     */
    public void miscellaneousSetUp()
    {
        lockedWordImageResourceID = getResources().getIdentifier(lockedWord.getImageName(), "drawable", getPackageName());
        imprima = ResourcesCompat.getFont(this, R.font.imprima);

        topIV.setBackgroundResource(lockedWordImageResourceID);
        topIV.setOnClickListener(Quiz.this);
        topIV.setTag("Repeat Button"); // Can be clicked to repeat the word

        repeatIV.setBackgroundResource(R.drawable.repeat);
        repeatIV.setOnClickListener(Quiz.this);
        repeatIV.setTag("Repeat Button"); // Can be clicked to repeat the word
    }

    /**
     * Handles set-up for the three star images that indicate quiz progress
     */
    public void createStarButtons()
    {
        starIV1 = new ImageView(this);
        starIV2 = new ImageView(this);
        starIV3 = new ImageView(this);

        starIV1.setBackgroundResource(R.drawable.gold_star_blank);
        starIV2.setBackgroundResource(R.drawable.gold_star_blank);
        starIV3.setBackgroundResource(R.drawable.gold_star_blank);

        starIV1.setVisibility(View.INVISIBLE); // Hide star 1
        starIV2.setVisibility(View.INVISIBLE); // Hide star 2
        starIV3.setVisibility(View.INVISIBLE); // Hide star 3
    }

    /**
     * Plays the sound file for the word being learned
     */
    public void playWordAudio() {
        int audioResourceID = getResources().getIdentifier(lockedWord.getAudioName(), "raw", getPackageName());
        MediaPlayer mPlayer = MediaPlayer.create(this.getApplicationContext(), audioResourceID);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.start();
    }

    /**
     * Called when the page first loads; lays out elements dynamically based on screen dimensions
     */
    public void performLayout()
    {
        // Top Bar (Back Button and Help)
        topBar.getLayoutParams().height = HEIGHT_UNIT;
        topBar.getLayoutParams().width = width;
        topBar.requestLayout();

        // Image View (Picture of Word Being Quizzed)
        topIV.getLayoutParams().height = 3 * HEIGHT_UNIT;
        topIV.getLayoutParams().width = 3 * HEIGHT_UNIT; // Square images -> same height and width
        topIV.requestLayout();

        // Word Choices (1 Correct, 3 Incorrect)
        LinearLayout.LayoutParams choiceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2 * HEIGHT_UNIT - 10, 1
        );
        choiceParams.setMargins(5, 5, 5, 5);

        choice1.setLayoutParams(choiceParams);
        choice2.setLayoutParams(choiceParams);
        choice3.setLayoutParams(choiceParams);
        choice4.setLayoutParams(choiceParams);

        // Repeat Button (To repeat word audio)
        LinearLayout.LayoutParams repeat_params = new LinearLayout.LayoutParams(
                HEIGHT_UNIT,
                HEIGHT_UNIT
        );
        repeat_params.setMargins(0, 0, 0, 0);
        repeat_params.gravity = CENTER;

        repeatIV.setLayoutParams(repeat_params);

        // Star Buttons (3, side by side)
        RelativeLayout starRelativeLayout = new RelativeLayout(this);

        RelativeLayout.LayoutParams star_params1 = new RelativeLayout.LayoutParams(
                HEIGHT_UNIT,
                HEIGHT_UNIT
        );

        RelativeLayout.LayoutParams star_params2 = new RelativeLayout.LayoutParams(
                HEIGHT_UNIT,
                HEIGHT_UNIT
        );

        star_params2.setMargins(HEIGHT_UNIT,0,0,0);

        RelativeLayout.LayoutParams star_params3 = new RelativeLayout.LayoutParams(
                HEIGHT_UNIT,
                HEIGHT_UNIT
        );

        star_params3.setMargins(2 * HEIGHT_UNIT,0,0,0);

        star_params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        star_params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        star_params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        starRelativeLayout.addView(starIV1, star_params1);
        starRelativeLayout.addView(starIV2, star_params2);
        starRelativeLayout.addView(starIV3, star_params3);

        encompassing.addView(starRelativeLayout);
    }
}
