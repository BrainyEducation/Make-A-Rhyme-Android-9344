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

public class NameFriend extends AppCompatActivity implements View.OnClickListener {

    Word lockedWord;
    Typeface imprima;
    int height, width;
    int HEIGHT_UNIT;
    ImageView topIV;
    RelativeLayout topBar;
    LinearLayout topTwoOptions, bottomTwoOptions, encompassing;
    int lockedWordImageResourceID;
    final int TEXT_SIZE = 40;
    ArrayList<View> wordViews = new ArrayList<>();
    int elementWidth, elementHeight;
    final int TEXT_HEIGHT = 200;
    final int SEPARATOR_HEIGHT = 20;
    LinearLayout friendLL;

    /**
     * Runs when the activity launches; sets up the screen elements for selecting the word
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_friend);

        loadIntentsAndViews();
        sizingSetUp();
        miscellaneousSetUp();

        performLayout();
    }

    /**
     * Responsible for handling clicks for the four choices. Will also recognize when the repeat
     * button has been pressed and play the word again.
     */
    public void onClick(View v)
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("name",v.getTag().toString());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
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
     * Creates the buttons that serve as options for the word being spoken
     */
    public void loadIntentsAndViews()
    {
        lockedWord = (Word)getIntent().getSerializableExtra("word");

        topIV = findViewById(R.id.TopImageView);
        topBar = findViewById(R.id.topLayout);
        topTwoOptions = findViewById(R.id.TopTwoOptions);
        bottomTwoOptions = findViewById(R.id.BottomTwoOptions);
        encompassing = findViewById(R.id.EncompassingLL);
        friendLL = findViewById(R.id.WordLL);
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
        //elementHeight = height / ELEMENTS_ON_SCREEN;
        elementWidth = width;
        elementHeight = elementWidth; // This is to keep the aspect ratio consistent (temporary)

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
        topIV.setOnClickListener(NameFriend.this);

        String[] names = new String[] {"Bob", "John", "Will", "Han", "Dev", "Don", "Ted", "Pau",
                "Paz", "Kim", "Jim", "Eli", "Khalid", "Alex", "Sunil", "Adam", "Sam", "Joe", "Zoe",
                "Eva", "Mia", "Rio", "Uma", "Joy", "Rose", "Mary", "Lily", "Iris", "Ann", "Joan",
                "Pat", "Jan", "Deb", "Kate", "Beth"};

        friendLL.removeAllViews();
        wordViews = new ArrayList<>(); // Wipe out existing entries

        for (int index = 0; index < names.length; ++index) {

            Button tempFriendText = new Button(this);

            tempFriendText.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, TEXT_HEIGHT));

            tempFriendText.setOnClickListener(NameFriend.this);

            tempFriendText.setTag(names[index]);
            tempFriendText.setText(names[index]);
            tempFriendText.setBackgroundColor(Color.WHITE);
            tempFriendText.setTextColor(Color.BLACK);
            tempFriendText.setTextSize(TEXT_SIZE);
            tempFriendText.setTypeface(imprima);

            View separator = new View(this);

            separator.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, SEPARATOR_HEIGHT));

            friendLL.addView(tempFriendText);
            friendLL.addView(separator);
        }
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


        // Repeat Button (To repeat word audio)
        LinearLayout.LayoutParams repeat_params = new LinearLayout.LayoutParams(
                HEIGHT_UNIT,
                HEIGHT_UNIT
        );
        repeat_params.setMargins(0, 0, 0, 0);
        repeat_params.gravity = CENTER;


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

        encompassing.addView(starRelativeLayout);
    }
}
