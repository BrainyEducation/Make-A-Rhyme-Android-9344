package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
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

import static android.view.Gravity.CENTER;

public class ApplicationWelcome extends AppCompatActivity {

    ArrayList<RhymeTemplate> templateOptions = new ArrayList<>();
    LinearLayout rhymesLL;
    RelativeLayout topBar;
    int HEIGHT_UNIT;
    private ScrollView rhyme_template_scrollview;
    final int TEXT_SIZE = 30;
    Typeface imprima;
    int height, width;
    ImageView welcomeImage;
    Button kidsButton;
    Button parentsButton;
    Button registrationButton;
    int colorToggle = 0;

    int RHYME_HEIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_welcome);

        sizingSetUp();

        loadIntentsAndViews();

        miscellaneousSetUp();

        //buttonScaling();

        // Responsible for flipping the color from black to white every second
        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){

                if (colorToggle == 0) {
                    kidsButton.setTextColor(Color.WHITE);
                    colorToggle = 1;
                } else {
                    kidsButton.setTextColor(Color.BLACK);
                    colorToggle = 0;
                }
                handler.postDelayed(this, delay);
            }
        }, delay);

    }

    @Override
    public void onWindowFocusChanged(boolean b)
    {
        super.onWindowFocusChanged(b);

        buttonScaling();
    }

    /**
     * Matches the scrollview and its linear layout with their respective code variables
     */
    public void loadIntentsAndViews()
    {
        rhyme_template_scrollview = findViewById(R.id.RhymeTemplatesScrollView);
        rhymesLL = findViewById(R.id.RhymesLL);
        topBar = findViewById(R.id.topLayout);
        welcomeImage = findViewById(R.id.WelcomeImage);
        kidsButton = findViewById(R.id.child_login);
        parentsButton = findViewById(R.id.parent_login);
        registrationButton = findViewById(R.id.registration);

        // Illustration boundaries
        LinearLayout.LayoutParams illustration_params = new LinearLayout.LayoutParams(
                width, (int)(width * .811)
        );
        illustration_params.setMargins(0, 0, 0, 0);

        welcomeImage.setLayoutParams(illustration_params);
        welcomeImage.setBackgroundResource(R.drawable.logo);
    }

    /**
     * Handles set-up that would otherwise be in onCreate that doesn't belong in any other function
     */
    public void miscellaneousSetUp()
    {
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
     * Launches the rhyme UI (or the registration UI if it's the first user)
     */
    public void ClickedLoginStudent(View v)
    {
        ArrayList<Student> allStudents = Student.retrieveStudents(this.getApplicationContext());

        // In the event that there are no students currently registered, redirect to registration screen.
        if (allStudents.size() == 0) {
            Intent newIntent = new Intent(this, StudentRegistration.class);
            startActivityForResult(newIntent, 1);
        } else {
            Intent newIntent = new Intent(this, StudentLogin.class);
            startActivityForResult(newIntent, 1);
        }

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
     * Launches the generic registration UI
     */
    public void ClickedRegistration(View v)
    {
        Intent newIntent = new Intent(this, Registration1.class);
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

    /**
     * Scales fractionally depending on the size of the device so that it looks the same regardless
     * of what the user is using the app on.
     */
    public void buttonScaling()
    {
        LinearLayout.LayoutParams welcomeParams = new LinearLayout.LayoutParams(
                28 * HEIGHT_UNIT / 10,
                28 * HEIGHT_UNIT / 10
        );
        welcomeParams.setMargins(width / 20, HEIGHT_UNIT / 10, width / 20, HEIGHT_UNIT / 10);
        welcomeParams.gravity = CENTER;

        welcomeImage.setLayoutParams(welcomeParams);

        ////////

        LinearLayout.LayoutParams kidsParams = new LinearLayout.LayoutParams(
                9 * width / 10,
                18 * HEIGHT_UNIT / 10
        );
        kidsParams.setMargins(width / 20, HEIGHT_UNIT / 10, width / 20, HEIGHT_UNIT / 10);

        kidsButton.setLayoutParams(kidsParams);

        ////////

        LinearLayout.LayoutParams parentsParams = new LinearLayout.LayoutParams(
                9 * width / 10,
                8 * HEIGHT_UNIT / 10
        );
        parentsParams.setMargins(width / 20, HEIGHT_UNIT / 10, width / 20, HEIGHT_UNIT / 10);

        parentsButton.setLayoutParams(parentsParams);

        ////////

        LinearLayout.LayoutParams registrationParams = new LinearLayout.LayoutParams(
                9 * width / 10,
                8 * HEIGHT_UNIT / 10
        );
        registrationParams.setMargins(width / 20, HEIGHT_UNIT / 10, width / 20, HEIGHT_UNIT / 10);

        registrationButton.setLayoutParams(registrationParams);

    }

}
