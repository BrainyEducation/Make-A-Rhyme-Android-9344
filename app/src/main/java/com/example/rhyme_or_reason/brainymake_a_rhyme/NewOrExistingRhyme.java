package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import static com.example.rhyme_or_reason.brainymake_a_rhyme.RhymeTemplate.getNumberOfExistingRhymes;
import static com.example.rhyme_or_reason.brainymake_a_rhyme.RhymeTemplate.retrieveRhymeTemplate;

public class NewOrExistingRhyme extends AppCompatActivity implements View.OnClickListener {

    int width, height;
    ImageView newIllustration;
    LinearLayout existingRhymesLL;

    RelativeLayout topBar;
    int HEIGHT_UNIT;
    private ScrollView existingRhymesScrollview;
    Typeface imprima;
    final int TEXT_SIZE = 30;
    RhymeTemplate chosenRhymeTemplate;

    private ImageButton up_btn;
    private ImageButton down_btn;

    int RHYME_HEIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_or_existing_rhyme);

        sizingSetUp();

        loadIntentsAndViews();

        miscellaneousSetUp();

        loadNewIllustrationImage();

        loadExistingRhymes();

        setUpScrollArrows();
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
     * Matches the scrollview and its linear layout with their respective code variables
     */
    public void loadIntentsAndViews()
    {
        chosenRhymeTemplate = (RhymeTemplate)getIntent().getSerializableExtra("rhyme_template");

        newIllustration = findViewById(R.id.newIllustration);

        existingRhymesScrollview = findViewById(R.id.ExistingRhymesScrollView);

        existingRhymesLL = findViewById(R.id.RhymesLL);

        topBar = findViewById(R.id.topLayout);
    }

    /**
     * Called when the page first loads; lays out the existing story templates created by the child
     * in previous sessions. These are unnamed and only feature a picture of the rhyme.
     * TODO: Need rhyme template illustrations that have the pictures inserted
     */
    public void loadExistingRhymes() {

        int numExistingRhymes = getNumberOfExistingRhymes(this.getApplicationContext());

        for (int index = 0; index < numExistingRhymes; ++index) {

            RhymeTemplate currRhyme = retrieveRhymeTemplate(this.getApplicationContext(), index);

            ImageView rhymeImage = new ImageView(this);
            rhymeImage.setTag(index);
            rhymeImage.setLayoutParams(new LinearLayout.LayoutParams(width, (int) (width * Constants.ASPECT_RATIO)));
            int pictureResourceID = getResources().getIdentifier(currRhyme.getImageName(), "drawable", getPackageName());

            rhymeImage.setOnClickListener(this);

            rhymeImage.setImageResource(pictureResourceID);

            View separator = new View(this);

            separator.setLayoutParams(new LinearLayout.LayoutParams(width, Constants.SEPARATOR_HEIGHT));

            separator.setBackgroundColor(getResources().getColor(R.color.colorBackground));

            existingRhymesLL.addView(rhymeImage);
            existingRhymesLL.addView(separator);
        }
    }

    /**
     * Handles set-up that would otherwise be in onCreate that doesn't belong in any other function
     */
    public void miscellaneousSetUp()
    {
        Word.initialize(this.getApplicationContext());
        imprima = ResourcesCompat.getFont(this, R.font.imprima);
    }

    /**
     * When a rhyme is selected, this chooses what to send to Rhyme. In the event that the new
     * template is selected, a blank template is sent; if an existing rhyme is selected, then
     * the details for that specific saved rhyme are retrieved and passed.
     * TODO: Need to design generically for when there are multiple rhyme templates.
     */
    public void onClick(View v) {

        // Need to update this

        RhymeTemplate toSend;

        if (v.getTag() == "NEW") {
            toSend = chosenRhymeTemplate;
        } else {
            RhymeTemplate selectedRhyme = retrieveRhymeTemplate(this.getApplicationContext(), (Integer)v.getTag());
            toSend = selectedRhyme;
        }

        Intent newIntent = new Intent(this, Rhyme.class);
        newIntent.putExtra("rhyme_template", toSend);
        startActivityForResult(newIntent, 1);
    }

    /**
     * Fills the top illustration with the proper image (and sizes it correctly)
     */
    public void loadNewIllustrationImage()
    {
        // Illustration boundaries
        LinearLayout.LayoutParams illustration_params = new LinearLayout.LayoutParams(
                width, (int)(width * Constants.ASPECT_RATIO)
        );

        RHYME_HEIGHT = (int)(width * Constants.ASPECT_RATIO);
        illustration_params.setMargins(0, 0, 0, 0);

        newIllustration.setTag("NEW");
        newIllustration.setLayoutParams(illustration_params);
        newIllustration.setBackgroundResource(getResources().getIdentifier(chosenRhymeTemplate.getImageName(), "drawable", getPackageName()));
        newIllustration.setOnClickListener(this);
    }

    public void ClickedBackButton(View view) {
        onBackPressed();
    }

    /*
     * Scrolling happens by default; this is designed to allow the scroll buttons to move the
     * existing rhymes up and down in addition to scrolling by finger
     */
    public void setUpScrollArrows() {
        up_btn = findViewById(R.id.ScrollUpBtn);
        down_btn = findViewById(R.id.ScrollDownBtn);
        up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existingRhymesScrollview.smoothScrollBy(0, -(RHYME_HEIGHT + Constants.SEPARATOR_HEIGHT));
            }
        });
        down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existingRhymesScrollview.smoothScrollBy(0, RHYME_HEIGHT + Constants.SEPARATOR_HEIGHT);
            }
        });
    }

}
