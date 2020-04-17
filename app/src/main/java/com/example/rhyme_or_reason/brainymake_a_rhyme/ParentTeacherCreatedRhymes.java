package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

import static com.example.rhyme_or_reason.brainymake_a_rhyme.RhymeTemplate.getNumberOfExistingRhymes;
import static com.example.rhyme_or_reason.brainymake_a_rhyme.RhymeTemplate.retrieveRhymeTemplate;

public class ParentTeacherCreatedRhymes extends AppCompatActivity implements View.OnClickListener {

    int width, height;
    ImageView newIllustration;
    LinearLayout existingRhymesLL;

    RelativeLayout topBar;
    int HEIGHT_UNIT;
    private ScrollView existingRhymesScrollview;
    Typeface imprima;
    final int TEXT_SIZE = 30;

    private ImageButton up_btn;
    private ImageButton down_btn;
    String uuid = "";
    String name = "";

    ArrayList<RhymeTemplate> listOfRhymes = new ArrayList<>();

    int RHYME_HEIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_teacher_created_rhymes);

        sizingSetUp();

        loadIntentsAndViews();

        miscellaneousSetUp();

        loadExistingRhymes(false);

        setUpScrollArrows();

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

    /**
     * When a rhyme is selected, this chooses what to send to Rhyme. In the event that the new
     * template is selected, a blank template is sent; if an existing rhyme is selected, then
     * the details for that specific saved rhyme are retrieved and passed.
     * TODO: Need to design generically for when there are multiple rhyme templates.
     */
    public void onClick(View v) {

        RhymeTemplate toSend;

        toSend = listOfRhymes.get((Integer)v.getTag());

        Intent newIntent = new Intent(this, Rhyme.class);
        toSend.setSavedIllustration(null);
        newIntent.putExtra("rhyme_template", toSend);
        newIntent.putExtra("uuid", uuid);
        newIntent.putExtra("modification", false);
        startActivityForResult(newIntent, 1);
    }

    /**
     * Handles set-up that would otherwise be in onCreate that doesn't belong in any other function
     */
    public void miscellaneousSetUp()
    {
        //Word.initialize(this.getApplicationContext());
        imprima = ResourcesCompat.getFont(this, R.font.imprima);
    }

    /**
     * Called when the page first loads; lays out the existing story templates created by the child
     * in previous sessions. These are unnamed and only feature a picture of the rhyme.
     */
    public void loadExistingRhymes(boolean justReturned) {

        for (int count = 0; count < Constants.backgroundNames.size(); ++count) {
            String tempRhymeName = Constants.backgroundNames.get(count);

            int numExistingRhymes = getNumberOfExistingRhymes(this.getApplicationContext(), tempRhymeName, uuid);

            for (int index = 0; index < numExistingRhymes; ++index) {

                RhymeTemplate currRhyme = retrieveRhymeTemplate(this.getApplicationContext(), index, tempRhymeName, uuid);

                listOfRhymes.add(currRhyme);

                ImageView rhymeImage = new ImageView(this);
                rhymeImage.setTag(index);
                rhymeImage.setLayoutParams(new LinearLayout.LayoutParams(width, (int) (width * Constants.ASPECT_RATIO)));

                byte[] savedIllustration = currRhyme.getSavedIllustration();
                Bitmap savedIllustrationBitmap = BitmapFactory.decodeByteArray(savedIllustration, 0, savedIllustration.length);

                rhymeImage.setImageBitmap(savedIllustrationBitmap);

                System.out.println("3");

                rhymeImage.setOnClickListener(this);

                View separator = new View(this);

                separator.setLayoutParams(new LinearLayout.LayoutParams(width, Constants.SEPARATOR_HEIGHT));

                separator.setBackgroundColor(getResources().getColor(R.color.colorBackground));

                existingRhymesLL.addView(rhymeImage);
                existingRhymesLL.addView(separator);
            }
        }
    }

    /**
     * Matches the scrollview and its linear layout with their respective code variables
     */
    public void loadIntentsAndViews()
    {
        uuid = getIntent().getExtras().get("uuid").toString();

        existingRhymesScrollview = findViewById(R.id.ExistingRhymesScrollView);

        existingRhymesLL = findViewById(R.id.RhymesLL);

        topBar = findViewById(R.id.topLayout);
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
}

