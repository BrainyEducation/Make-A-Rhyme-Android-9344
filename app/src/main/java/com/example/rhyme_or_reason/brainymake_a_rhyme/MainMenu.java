package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    ArrayList<RhymeTemplate> templateOptions = new ArrayList<>();
    LinearLayout rhymesLL;
    RelativeLayout topBar;
    int HEIGHT_UNIT;
    private ScrollView rhyme_template_scrollview;
    final int TEXT_SIZE = 30;
    Typeface imprima;
    int height, width;

    int RHYME_HEIGHT;

    private ImageButton upBtn;
    private ImageButton downBtn;
    private Button progressBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        progressBtn = findViewById(R.id.ProgressButton);
        progressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent thisIntent = new Intent(v.getContext(), ProgressWordList.class);
                startActivityForResult(thisIntent, 0);
            }
        });

        sizingSetUp();

        loadIntentsAndViews();

        miscellaneousSetUp();

        loadRhymeTemplates();

        loadRhymeStoryOptions();

        setUpScroll();

        MainActivity.attemptsMap = getMapFromSharedPref();
    }

    /**
     * Called when the page first loads; lays out the story template options for the child
     */
    public void loadRhymeStoryOptions()
    {
        for (int index = 0; index < templateOptions.size(); ++index) {
            Button tempType = new Button(this);

            tempType.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            tempType.setText(templateOptions.get(index).getName());
            tempType.setTag(index); // Set tag to the index in the ArrayList
            tempType.setBackgroundColor(getResources().getColor(R.color.colorBackground));
            tempType.setTextSize(TEXT_SIZE);
            tempType.setTypeface(imprima);
            tempType.setAlpha(0.6f);

            ImageView rhymeImage = new ImageView(this);
            rhymeImage.setTag(index);
            rhymeImage.setLayoutParams(new LinearLayout.LayoutParams(width, (int)(width * Constants.ASPECT_RATIO)));
            int pictureResourceID = getResources().getIdentifier(templateOptions.get(index).getImageName(), "drawable", getPackageName());

            if (!templateOptions.get(index).getLocked()) {
                tempType.setOnClickListener(this);
                rhymeImage.setOnClickListener(this);
            } else {
                rhymeImage.setAlpha(Constants.LOCKED_ALPHA);
                tempType.setAlpha(Constants.LOCKED_ALPHA);
            }

            rhymeImage.setImageResource(pictureResourceID);

            View separator = new View(this);

            separator.setLayoutParams(new LinearLayout.LayoutParams(width, Constants.SEPARATOR_HEIGHT));

            separator.setBackgroundColor(getResources().getColor(R.color.colorBackground));

            rhymesLL.addView(tempType);
            rhymesLL.addView(rhymeImage);
            rhymesLL.addView(separator);
        }
    }

    /*
     * Provides basic information about all the rhyme template options, including whether they should be
     * available to the user to select or grayed out
     */
    public void loadRhymeTemplates()
    {
        RhymeTemplate petPartyPicnic = new RhymeTemplate("Pet Party Picnic", "background_pet_party_picnic", false, 15, 6);
        RhymeTemplate muddyPark = new RhymeTemplate("Muddy Park", "background_muddy_park", false, 13, 5);
        RhymeTemplate strangerParade = new RhymeTemplate("Stranger Parade", "background_stranger_parade", false, 19, 19);

        templateOptions.add(petPartyPicnic);
        templateOptions.add(muddyPark);
        templateOptions.add(strangerParade);
    }

    /**
     * Matches the scrollview and its linear layout with their respective code variables
     */
    public void loadIntentsAndViews()
    {
        rhyme_template_scrollview = findViewById(R.id.RhymeTemplatesScrollView);
        rhymesLL = findViewById(R.id.RhymesLL);
        topBar = findViewById(R.id.topLayout);
    }

    /**
     * Handles set-up that would otherwise be in onCreate that doesn't belong in any other function
     */
    public void miscellaneousSetUp()
    {
        //Word.initialize(this.getApplicationContext());
        imprima = ResourcesCompat.getFont(this, R.font.imprima);
        RHYME_HEIGHT = (int)(width * Constants.ASPECT_RATIO);
    }

    /**
     * Called when the page first loads; gets dimensions to help calculate how to dynamically lay
     * out the screen
     */
    public void sizingSetUp()
    {
        // Display sizing set-up
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        width = screenSize.x;
        height = screenSize.y;

        HEIGHT_UNIT = height / 10;
    }

    /**
     * When a rhyme template is selected, this recognizes which one has been clicked and passes
     * along the information to the NewOrExistingRhyme screen so it knows which existing rhymes
     * to load.
     */
    public void onClick(View v) {

        RhymeTemplate selectedRhymeTemplate = templateOptions.get((int)(v.getTag()));
        Intent newIntent = new Intent(this, NewOrExistingRhyme.class);
        newIntent.putExtra("rhyme_template", selectedRhymeTemplate);
        startActivityForResult(newIntent, 1);
    }

    /*
     * Scrolling happens by default; this is designed to allow the scroll buttons to move the
     * rhyme templates up and down in addition to scrolling by finger
     */
    public void setUpScroll()
    {
        upBtn = findViewById(R.id.UpButton);
        downBtn = findViewById(R.id.DownButton);
        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rhyme_template_scrollview.smoothScrollBy(0, -(RHYME_HEIGHT + Constants.SEPARATOR_HEIGHT));
            }
        });
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rhyme_template_scrollview.smoothScrollBy(0, RHYME_HEIGHT + Constants.SEPARATOR_HEIGHT);
            }
        });
    }

    public HashMap getMapFromSharedPref() {
        SharedPreferences sharedpref = getSharedPreferences("AttemptsMap", MODE_PRIVATE);
        String val = new Gson().toJson(new HashMap<String, ArrayList<int[]>>());
        String jsonStr = sharedpref.getString("ProgressMap", val);
        TypeToken<HashMap<String, ArrayList<int[]>>> token = new TypeToken<HashMap<String, ArrayList<int[]>>>() {};
        HashMap<String, ArrayList<int[]>> mapFromPref = new Gson().fromJson(jsonStr, token.getType());
        return mapFromPref;
    }
}
