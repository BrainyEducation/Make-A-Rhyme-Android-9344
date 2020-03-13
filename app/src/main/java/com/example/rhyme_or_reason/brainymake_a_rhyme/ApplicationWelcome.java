package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.HashMap;

public class ApplicationWelcome extends AppCompatActivity {

    ArrayList<RhymeTemplate> templateOptions = new ArrayList<>();
    LinearLayout rhymesLL;
    RelativeLayout topBar;
    int HEIGHT_UNIT;
    private ScrollView rhyme_template_scrollview;
    final int TEXT_SIZE = 30;
    Typeface imprima;
    int height, width;

    int RHYME_HEIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_welcome);

        sizingSetUp();

        loadIntentsAndViews();

        miscellaneousSetUp();

        MainActivity.attemptsMap = getMapFromSharedPref();
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
     * Launches the rhyme UI
     */
    public void ClickedLoginStudent(View v)
    {
        Intent newIntent = new Intent(this, MainMenu.class);
        startActivityForResult(newIntent, 1);
    }
    /**
     * Launches the parent login UI
     */
    public void ClickedLoginParent(View v)
    {
        Intent newIntent = new Intent(this, ParentLogin.class);
        startActivityForResult(newIntent, 1);
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

    public HashMap getMapFromSharedPref() {
        SharedPreferences sharedpref = getSharedPreferences("AttemptsMap", MODE_PRIVATE);
        String val = new Gson().toJson(new HashMap<String, ArrayList<int[]>>());
        String jsonStr = sharedpref.getString("ProgressMap", val);
        TypeToken<HashMap<String, ArrayList<int[]>>> token = new TypeToken<HashMap<String, ArrayList<int[]>>>() {};
        HashMap<String, ArrayList<int[]>> mapFromPref = new Gson().fromJson(jsonStr, token.getType());
        return mapFromPref;
    }

}
