package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.view.Gravity.CENTER;

public class Quiz extends AppCompatActivity implements View.OnClickListener {

    Word lockedWord;
    ArrayList<String> wrongWords;
    String correctChoiceNum = "0";
    Button choice1;
    Button choice2;
    Button choice3;
    Button choice4;
    ImageView starIV;
    ImageView starIV2;
    ImageView starIV3;
    int correctStreak = 0;
    Typeface imprima;
    int buttonColor = Color.parseColor("#f4faf8");
    final int textSize = 40;

    /**
     * Runs when the activity launches; sets up the screen elements for selecting the word
     * TODO: Declutter; Separate into functions
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        lockedWord = (Word)getIntent().getSerializableExtra("word");
        wrongWords = getIntent().getStringArrayListExtra("wrong_words");

        imprima = ResourcesCompat.getFont(this, R.font.imprima);

        int resourceID = getResources().getIdentifier(lockedWord.getImageName(), "drawable", getPackageName());

        ImageView topIV = findViewById(R.id.TopImageView);

        topIV.setBackgroundResource(resourceID);
        topIV.setOnClickListener(Quiz.this);
        topIV.setTag("Repeat Button");

        choice1 = new Button(this);
        choice1.setTag("1");
        choice1.setTextSize(textSize);
        choice1.setTypeface(imprima);
        choice2 = new Button(this);
        choice2.setTag("2");
        choice2.setTextSize(textSize);
        choice2.setTypeface(imprima);
        choice3 = new Button(this);
        choice3.setTag("3");
        choice3.setTextSize(textSize);
        choice3.setTypeface(imprima);
        choice4 = new Button(this);
        choice4.setTag("4");
        choice4.setTextSize(textSize);
        choice4.setTypeface(imprima);

        choice1.setOnClickListener(this);
        choice2.setOnClickListener(this);
        choice3.setOnClickListener(this);
        choice4.setOnClickListener(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                300, 1
        );
        params.setMargins(5, 5, 5, 5);

        choice1.setLayoutParams(params);
        choice2.setLayoutParams(params);
        choice3.setLayoutParams(params);
        choice4.setLayoutParams(params);

        setChoices();

        LinearLayout topLL = findViewById(R.id.TopTwoOptions);
        LinearLayout bottomLL = findViewById(R.id.BottomTwoOptions);

        topLL.addView(choice1);
        topLL.addView(choice2);

        bottomLL.addView(choice3);
        bottomLL.addView(choice4);

        LinearLayout.LayoutParams repeat_params = new LinearLayout.LayoutParams(
                200,
                200
        );
        repeat_params.setMargins(5, 5, 5, 5);
        repeat_params.gravity = CENTER;

        // NOTE: Not a button (so can't click - just an ImageView right now)
        ImageButton repeatIV = findViewById(R.id.RepeatButton);

        repeatIV.setLayoutParams(repeat_params);
        repeatIV.setTag("Repeat Button");

        repeatIV.setBackgroundResource(R.drawable.repeat);
        repeatIV.setOnClickListener(Quiz.this);

        LinearLayout encompassing = findViewById(R.id.EncompassingLL);

        // Stars

        starIV = new ImageView(this);
        starIV2 = new ImageView(this);
        starIV3 = new ImageView(this);

        starIV.setBackgroundResource(R.drawable.gold_star_blank);
        starIV2.setBackgroundResource(R.drawable.gold_star_blank);
        starIV3.setBackgroundResource(R.drawable.gold_star_blank);

        starIV.setVisibility(View.INVISIBLE); // Hide star 1
        starIV2.setVisibility(View.INVISIBLE); // Hide star 2
        starIV3.setVisibility(View.INVISIBLE); // Hide star 3

        RelativeLayout relativeLayout = new RelativeLayout(this);

        RelativeLayout.LayoutParams star_params = new RelativeLayout.LayoutParams(
                200,
                200
        );

        RelativeLayout.LayoutParams star_params2 = new RelativeLayout.LayoutParams(
                200,
                200
        );

        star_params2.setMargins(200,0,0,0);

        RelativeLayout.LayoutParams star_params3 = new RelativeLayout.LayoutParams(
                200,
                200
        );

        star_params3.setMargins(400,0,0,0);

        star_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        star_params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        star_params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        relativeLayout.addView(starIV, star_params);
        relativeLayout.addView(starIV2, star_params2);
        relativeLayout.addView(starIV3, star_params3);

        encompassing.addView(relativeLayout);

        // Play the word
        int audioResourceID = getResources().getIdentifier(lockedWord.getAudioName(), "raw", getPackageName());
        MediaPlayer mPlayer = MediaPlayer.create(this.getApplicationContext(), audioResourceID);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.start();
    }

    /**
     * Picks out the wrong answers and places them in the boxes; then places the correct answer
     * randomly in one of the boxes
     */
    public void setChoices()
    {
        // NOTE: NEEDS 5+ WORDS PER CATEGORY TO WORK

        int randomInteger = (new Random()).nextInt(4) + 1;

        int randomFirstChoice = (new Random()).nextInt(wrongWords.size());
        String wrongWord1 = wrongWords.get(randomFirstChoice);

        String wrongWord2;

        while (true) {
            int randomSecondChoice = (new Random()).nextInt(wrongWords.size());
            wrongWord2 = wrongWords.get(randomSecondChoice);
            if (!wrongWord2.equals(wrongWord1)) {
                break;
            }
        }

        String wrongWord3;
        while (true) {
            int randomThirdChoice = (new Random()).nextInt(wrongWords.size());
            wrongWord3 = wrongWords.get(randomThirdChoice);
            if (!wrongWord3.equals(wrongWord1) && !wrongWord3.equals(wrongWord2)) {
                break;
            }
        }

        String wrongWord4;
        while (true) {
            int randomFourthChoice = (new Random()).nextInt(wrongWords.size());
            wrongWord4 = wrongWords.get(randomFourthChoice);
            if (!wrongWord4.equals(wrongWord1) && !wrongWord4.equals(wrongWord2) && !wrongWord4.equals(wrongWord3)) {
                break;
            }
        }

        choice1.setText(wrongWord1);
        choice1.setBackgroundColor(buttonColor);

        choice2.setText(wrongWord2);
        choice2.setBackgroundColor(buttonColor);

        choice3.setText(wrongWord3);
        choice3.setBackgroundColor(buttonColor);

        choice4.setText(wrongWord4);
        choice4.setBackgroundColor(buttonColor);

        if (randomInteger == 1) {
            choice1.setText(lockedWord.getText());
            correctChoiceNum = "1";
        } else if (randomInteger == 2) {
            choice2.setText(lockedWord.getText());
            correctChoiceNum = "2";
        } else if (randomInteger == 3) {
            choice3.setText(lockedWord.getText());
            correctChoiceNum = "3";
        } else if (randomInteger == 4) {
            choice4.setText(lockedWord.getText());
            correctChoiceNum = "4";
        }
    }

    /**
     * Responsible for handling clicks for the four choices. Will also recognize when the repeat
     * button has been pressed and play the word again.
     */
    public void onClick(View v)
    {
        if (v.getTag().equals("Repeat Button")) {
            int audioResourceID = getResources().getIdentifier(lockedWord.getAudioName(), "raw", getPackageName());
            MediaPlayer mPlayer = MediaPlayer.create(this.getApplicationContext(), audioResourceID);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.start();
        } else {
            if (correctChoiceNum.equals(v.getTag())) {
                correctStreak += 1;
                if (correctStreak == 1) {
                    starIV.setVisibility(View.VISIBLE);
                    starIV.setBackgroundResource(R.drawable.gold_star_blank);
                } else if (correctStreak == 2) {
                    starIV2.setVisibility(View.VISIBLE);
                    starIV2.setBackgroundResource(R.drawable.gold_star_blank);
                } else if (correctStreak == 3) {
                    starIV3.setVisibility(View.VISIBLE);
                    starIV3.setBackgroundResource(R.drawable.gold_star_blank);

                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("Congratulations!");
                    dialog.setMessage("You Unlocked " + lockedWord.getText());
                    //dialog.setPositiveButton("OK", Quiz.this);
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

                starIV.setBackgroundResource(R.drawable.silver_star_blank);
                starIV2.setBackgroundResource(R.drawable.silver_star_blank);
                starIV3.setBackgroundResource(R.drawable.silver_star_blank);

                correctStreak = 0;
            }

            // Don't want any sound when going back to the main menu
            if (correctStreak != 3) {
                setChoices();
                // Play the word

                int audioResourceID = getResources().getIdentifier(lockedWord.getAudioName(), "raw", getPackageName());
                MediaPlayer mPlayer = MediaPlayer.create(this.getApplicationContext(), audioResourceID);
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.start();
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
}
