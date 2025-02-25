package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;

import static android.view.Gravity.CENTER;
import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<View> typeViews = new ArrayList<>();
    ArrayList<View> wordViews = new ArrayList<>();
    ArrayList<String> typeList = new ArrayList<>();
    HashMap<String, ArrayList<Word> > typeToWordMapping = new HashMap<String, ArrayList<Word> >();
    HashMap<String, ArrayList<Integer>> typeToImageMapping = new HashMap<String, ArrayList<Integer>>();
    String activeType = "";
    Word selectedWord;
    int wordIndex;
    int height, width, elementWidth, elementHeight;
    final int NUM_COLUMNS = 2;
    final int TEXT_HEIGHT = 200;
    Typeface imprima;
    LinearLayout typeLL, wordLL;
    RelativeLayout topBar;
    int HEIGHT_UNIT;
    private Button progressBtn;
    private ScrollView word_scrollview, type_scrollview;
    private ImageButton up_btnW, down_btnW, up_btnT, down_btnT;

    ArrayList<Word> unlockedWords = new ArrayList<>();

    Student currStudent;

    String uuid = "";

    /**
     * Runs when the activity launches; sets up the types on the left side of the screen and loads
     * the default words in on the right side of the screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadIntentsAndViews();
        sizingSetUp();
        miscellaneousSetUp();

        getStudentFromUUID();

        setUpScroll();

        performLayout();

        loadTypeToWordMappings();

        setUpTypes();

        onClick(typeViews.get(0));
    }

    /**
     * Responsible for changing the words on the right side of the screen when a type is selected;
     * replaces the buttons on the screen (instead of overwriting the text on the buttons) and
     * ensures the appropriate lock statuses are shown (gray -> locked, light pink -> unlocked)
     */
    public void updateWordList(String currType)
    {
        activeType = currType;
        wordLL.removeAllViews(); // Removes the current buttons

        ArrayList<Word> wordList = typeToWordMapping.get(currType);

        wordViews = new ArrayList<>(); // Wipe out existing entries

        for (int index = 0; index < wordList.size(); ++index) {

            ImageView tempWordImage = new ImageView(this);

            tempWordImage.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, elementHeight));
            String taglabel = wordList.get(index).getText();
            if(currType.equals("Friends"))
                taglabel = wordList.get(index).getImageName();
            tempWordImage.setTag(taglabel);
            tempWordImage.setOnClickListener(MainActivity.this);
            int resourceID = getResources().getIdentifier(wordList.get(index).getImageName(), "drawable", getPackageName());
            setScaledImage(tempWordImage, resourceID);

            Button tempWordText = new Button(this);

            tempWordText.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, TEXT_HEIGHT));

            tempWordText.setOnClickListener(MainActivity.this);

            tempWordText.setTag(taglabel);
            if(!currType.equals("Friends"))
                tempWordText.setText(wordList.get(index).getText());
            tempWordText.setBackgroundColor(Color.WHITE);
            tempWordText.setTextColor(Color.BLACK);
            tempWordText.setTextSize(Constants.STANDARD_TEXT_SIZE);
            tempWordText.setTypeface(imprima);

            View separator = new View(this);

            separator.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, Constants.SEPARATOR_HEIGHT));

            if (!currType.equals("Friends") && (wordList.get(index)).getLockedStatus()) {
                // Is Locked
                tempWordImage.setAlpha(Constants.LOCKED_ALPHA);
            } else {
                // Is Unlocked
                //tempWordImage.setAlpha(1);
            }

            wordViews.add(tempWordImage);
            wordLL.addView(tempWordImage);
            wordLL.addView(tempWordText);
            wordLL.addView(separator);
        }
    }

    /**
     * Responsible for mapping each type to its corresponding words; this is necessary so that the
     * correct words show up users switch the type
     */
    public void loadTypeToWordMappings()
    {
        String[][] words = new String[][] { {"People", "baby", "boy", "brother", "child", "clown",
                "cook", "dancer", "family", "father", "girl", "grandma", "grandpa", "juggler",
                "king", "man", "mother", "nurse", "queen", "sister", "twins"},
                {"Pretend", "centaur", "cyclops", "dragon", "elf", "fairy", "mermaid", "yeti"},
                {"Body Parts", "ankle", /*"arm",*/ "chin", "elbow", "face", "feet", "foot", "hair",
                    "hand", "head", "lip", "mouth", "nose", "thigh", "thumb", "toe"},
                {"Animals", "ape", /*"ant"*/ "bat", "bear", "bee", "camel", "cat", "centipede",
                        "collie", "cow", /*"cub",*/ "dog", "dogs", "donkey", "elk", /*"fly",*/ "fox",
                        "goat", "kitten", "mole", "monkey", "moth", "mouse", "paw",/*"pet",*/"pig",
                        "rabbit", /*"ram",*/ "sheep", "skunk", "snail", "tail", "tiger", "toad",
                        "wasp", "whale", "wolf", "worms", "zebra"},
                {"Water Animals", "beaver", "clam", "crab", "fish", "frog", "gator", "oyster",
                        "seal", "shark"},
                {"Birds", "bird", "canary", "hen", "jay", "ostrich", "owl", "parrot", "swan"},
                {"Things", "bags", "bed", "blanket", "box", "brick", "broom", "bubble", "cast",
                        "clarinet","clock", "coin", "cushion",/*"fashion",*/"flute", "fork", "fridge",
                        /*"fright",*/ "fringe", "glass"},
                {"House Stuff", "key", "light", "mirror", "money", "music", "net", "oven", "pan",
                        "pearl", "pencil", "plug", "poison", "pot", "prize", "quiz", "saucepan",
                        "skis", "soap", "sofa", /*"spoon",*/"squares",/*"string",*/"toilet", "tuba",
                        "wheel", "zipper"},
                {"Toys", "ball", "block", "boat", "car", "crayon", "doll", "jeep", "jet", "present",
                        "puppet", "slide", "stilts", "swing",/*"toys",*/"truck", "unicycle", "wagon",
                        "yoyo"},
                {"Tools", "axe", "drill", "hatchet", "hoe", "nail", "rake", "saw", "tools"},
                {"Clothes", "boots", "clothes", /*"dress",*/"glove", "hoodie", "jacket", "purse",
                        "ring", "scarf", "shirt", "suit", "tie", "veil", "wig"},
                {"Vehicles", "ambulance", "boat", "bug", "bus", "car", "cars", "dozer", "go_kart",
                        "jeep", "moped", "plane", "taxi", "truck", "van"},
                {"Food", "apple", "bread", "burger", "cake", "candy",/*"carrot",*/"cone", "cookies",
                        "corn","grapes","hotdog","lettuce","milk","nuts","pie","plum","pretzel",
                        "snack","tea"},
                {"Places", "bridge", "hill", "house", "park", "school", "volcano", "zoo"},
                {"Outdoors", "air", "fern", "flag", "grass", "ice", /*"leaf",*/ "moon", "rain",
                        "rainbow", "sky", "snow", "star", "statue", /*"straw",*/ "tree", "wall", "wind"},
                {"Doing", "balance", "blew", "burn", "chew", "chop", "clean", "cry", "cut", "dig",
                        "draw", /*"drive",*/ "fall", "fish", "flew", /*"float", "fly", "glue",*/ "hit",
                        "hug", "juggle", "jump", "lick", "look", "love", "paint", "play", "read",
                        "rescue", "scold", "see", "sing", "ski", "skip", "sleep", "slip", "smell",
                        "smile", "spill", "stand", "stop", "swim", /*"throw"*/ "twinkle", "wash",
                        /*"weigh",*/ "whisper", "yawn"},
                {"Describe", "afraid", "cloudy", "dark",/*"eight", "eight",*/ "five", "high", "hot",
                        "loud", "naughty", "old", "quiet", "rude", "silly", "six", "sixteen",
                        "sleepy", "slow", "smart", "stripes", "twelve"},
                {"Colors", "black", "blue", "brown", "gold", "green", "purple", /*"red",*/ "silver",
                        /*"white",*/ "yellow"}
        };
        for(int i = 0; i < words.length; i++)
        {
            typeList.add(words[i][0]);
            ArrayList<Word> wordlist = new ArrayList<>();

            for(int j = 1; j < words[i].length; j++) {
                Word currWord = Word.retrieveWord(this.getApplicationContext(), words[i][j], words[i][0].toLowerCase(), uuid);
                if (currWord == null) {
                    currWord = new Word(words[i][j], true, words[i][j], words[i][j], words[i][0].toLowerCase());
                }

                if (currWord.getLockedStatus()) {
                    unlockedWords.add(currWord);
                }

                wordlist.add(currWord);
            }
            typeToWordMapping.put(words[i][0], wordlist);
        }

        typeList.add("Friends");
        ArrayList<Word> friendList = new ArrayList<>();
        for(int j = 1; j < 18; j++) {
                friendList.add(new Word("boy_" + j, false, "boy_" + j, "", "Friends"));
            if(j < 15)
                friendList.add(new Word("girl_" + j, false, "girl_" + j, "", "Friends"));
        }
        typeToWordMapping.put("Friends", friendList);
    }


    /**
     * Responsible for handling clicks for the types and words in the scrolling columns.
     * When a type is selected, it is highlighted and its words are shown in the right scrolling
     * view. When a word is selected, the next activity is launched (the quiz) for the word
     */
    public void onClick(View v) {
        //OnClick For Progress Button
        if (v == progressBtn) {
            Intent progressInt = new Intent(this, StudentProgress.class);
            startActivity(progressInt);
        }

        //OnClick For Progress Button
        if (v == progressBtn) {
            Intent progressInt = new Intent(this, StudentProgress.class);
            startActivity(progressInt);
        }


        Boolean updatingActiveType = false;

        int typeIndex = -1;

        // Check all Types first
        for (int index = 0; index < typeList.size(); ++index) {
            if (v.getTag().equals(typeList.get(index))) {
                updatingActiveType = true;
                activeType = typeList.get(index);
                typeViews.get(index).setBackgroundColor(Color.parseColor("#c8e6c2"));
                typeIndex = index;
            }
        }

        // Only clear out the type background colors if a new type is selected
        if (updatingActiveType) {
            for (int index = 0; index < typeViews.size(); ++index) {
                if (typeIndex != index) {
                    typeViews.get(index).setBackgroundColor(getResources().getColor(R.color.colorButton));
                }
            }
        }

        Boolean switchingActivities = false;

        if (!updatingActiveType) {
            ArrayList<Word> wordList = typeToWordMapping.get(activeType);

            ArrayList<String> categoryWords = new ArrayList<>();
            ArrayList<String> lengthWords = new ArrayList<>();
            ArrayList<String> letterWords = new ArrayList<>();
            ArrayList<String> otherWords = new ArrayList<>();

            int matchIndex = -1;

            for (int index = 0; index < wordList.size(); ++index) {
                if (!(v.getTag().equals(wordList.get(index).getText()) || v.getTag().equals(wordList.get(index).getImageName()))) {
                    categoryWords.add(wordList.get(index).getText());
                } else {
                    matchIndex = index;
                    switchingActivities = true;
                }
            }
            wordIndex = matchIndex;
            selectedWord = wordList.get(matchIndex);
            if(selectedWord.getAudioName().length() > 0) {
                //Most words
                for (String s : typeToWordMapping.keySet()) {
                    if (!s.equals(activeType) && !s.equals("Friends"))
                        for (Word w : typeToWordMapping.get(s)) {
                            if (w.getText().length() == selectedWord.getText().length())
                                lengthWords.add(w.getText());
                            else if (w.getText().length() > 0 && w.getText().charAt(0) == selectedWord.getText().charAt(0))
                                letterWords.add(w.getText());
                            else if (w.getText().length() > 0)
                                otherWords.add(w.getText());
                        }
                }

                if (switchingActivities) {
                    wordIndex = matchIndex;
                    selectedWord = wordList.get(matchIndex);

                    if (!selectedWord.getLockedStatus()) {
                        Handler returnHandler = new Handler();
                        returnHandler.postDelayed(new Runnable() {
                            public void run() {
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("word", selectedWord);
                                returnIntent.putExtra("uuid", uuid);
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        }, 0);   // Instantaneous
                    } else {
                        Intent newIntent = new Intent(this, Quiz.class);
                        newIntent.putExtra("word", selectedWord);
                        newIntent.putExtra("category_words", categoryWords);
                        newIntent.putExtra("length_words", lengthWords);
                        newIntent.putExtra("letter_words", letterWords);
                        newIntent.putExtra("other_words", otherWords);
                        newIntent.putExtra("uuid", uuid);
                        startActivityForResult(newIntent, 1);
                    }
                }
            } else {
                //Friends
                Intent newIntent = new Intent(this, NameFriend.class);
                newIntent.putExtra("word", selectedWord);
                newIntent.putExtra("uuid", uuid);
                startActivityForResult(newIntent, 2);
            }

        }


        if (!switchingActivities) {
            // Only executes if the selected item was a Type (will leave screen for new activity if not)
            //v.setBackgroundColor(Color.parseColor("#c8e6c2"));//"#9370DB"));

            updateWordList((String) v.getTag());
        }

    }

    /**
     * Responsible for getting the result of the quiz. When RESULT_OK, then the quiz was passed,
     * so the word will be unlocked
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        getStudentFromUUID(); // Have to call this again to get updated version of student (with added attempts)

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                //getStudentFromUUID();
                selectedWord.setUnlocked();
                unlockedWords.add(selectedWord);
                // Update the saved status of the word
                selectedWord.saveWord(this.getApplicationContext(), uuid);
                wordViews.get(wordIndex).setAlpha(1);

                currStudent.addUnlockedWord(selectedWord);
                currStudent.saveStudent(this.getApplicationContext()); // This should update the saved state of the student with its unlocked words

                // Exit quiz
                Handler returnHandler = new Handler();
                returnHandler.postDelayed(new Runnable() {
                    public void run() {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("word", selectedWord);
                        returnIntent.putExtra("uuid", uuid);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }, 0);   // Instantaneous

            }
        }
        else if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                Handler returnHandler = new Handler();
                returnHandler.postDelayed(new Runnable() {
                    public void run() {
                        Intent returnIntent = new Intent();
                        String name = data.getStringExtra("name");
                        returnIntent.putExtra("word", new Word(name, false, selectedWord.getImageName(), name.toLowerCase(), "Friends"));
                        returnIntent.putExtra("uuid", uuid);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }, 0);   // Instantaneous
            }
        }
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
        elementWidth = width / NUM_COLUMNS;
        elementHeight = elementWidth; // This is to keep the aspect ratio consistent (temporary)

        HEIGHT_UNIT = height / 10;
    }

    /**
     * Handles set-up that would otherwise be in onCreate that doesn't belong in any other function
     */
    public void miscellaneousSetUp()
    {
        imprima = ResourcesCompat.getFont(this, R.font.imprima);
    }

    /**
     * Creates the buttons that serve as options for the word being spoken
     */
    public void loadIntentsAndViews()
    {
        uuid = getIntent().getExtras().get("uuid").toString();

        word_scrollview = findViewById(R.id.WordScrollView);
        type_scrollview = findViewById(R.id.TypeScrollView);
        up_btnW = findViewById(R.id.WordScrollUpBtn);
        down_btnW = findViewById(R.id.WordScrollDownBtn);
        up_btnT = findViewById(R.id.TypeScrollUpBtn);
        down_btnT = findViewById(R.id.TypeScrollDownBtn);
        progressBtn = findViewById(R.id.ProgressButton);
        up_btnW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word_scrollview.smoothScrollBy(0, -500);
            }
        });
        down_btnW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word_scrollview.smoothScrollBy(0, 500);
            }
        });
        up_btnT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type_scrollview.smoothScrollBy(0, -500);
            }
        });
        down_btnT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type_scrollview.smoothScrollBy(0, 500);
            }
        });
        typeLL = findViewById(R.id.TypeLL);
        wordLL = findViewById(R.id.WordLL);
        topBar = findViewById(R.id.topLayout);
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
    }

    /**
     * Called when the page first loads; lays out left column of types
     */
    public void setUpTypes() {
        for (int index = 0; index < typeList.size(); ++index) {
            Button tempType = new Button(this);

            tempType.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            tempType.setText(typeList.get(index));
            tempType.setTag(typeList.get(index)); // Set tag to the name of the type
            tempType.setBackgroundColor(getResources().getColor(R.color.colorButton));
            tempType.setTextSize(Constants.STANDARD_TEXT_SIZE);
            tempType.setTypeface(imprima);
            tempType.setOnClickListener(MainActivity.this);

            View separator = new View(this);

            separator.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, Constants.SEPARATOR_HEIGHT));

            separator.setBackgroundColor(getResources().getColor(R.color.colorBackground));

            typeViews.add(tempType);
            typeLL.addView(tempType);
            typeLL.addView(generateImagePreview(tempType));
            typeLL.addView(separator);
        }
    }

    /*
     * Responsible for laying out the two images that represent a category; these are the two
     * first words for each category (as of writing, they are loaded alphabetically).
     */
    private LinearLayout generateImagePreview(Button exampleButton) {
        LinearLayout linearElement = new LinearLayout(this);
        for (int i = 0; i < 2; i++) {
            ImageView sampleImage = new ImageView(this);
            sampleImage.setLayoutParams(new LinearLayout.LayoutParams(elementHeight / 2, elementHeight / 2));
            //sampleImage.getLayoutParams().height = TEXT_HEIGHT;
            int pictureResourceID = 0;
            sampleImage.setOnClickListener(MainActivity.this);
            sampleImage.setTag(exampleButton.getText().toString());

            if (typeToWordMapping.containsKey(exampleButton.getText().toString())) {
                ArrayList<Word> wordsForSelectedType = typeToWordMapping.get(exampleButton.getText().toString());
                pictureResourceID = getResources().getIdentifier(wordsForSelectedType.get(i).getImageName(), "drawable", getPackageName());
            }

            setScaledImage(sampleImage, pictureResourceID);
            linearElement.addView(sampleImage);
        }
        return linearElement;
    }

    /**
<<<<<<< HEAD
     * Returns user to previous activity
     * @param view
=======
     * Returns the user to the Rhyme screen.
>>>>>>> DocumentationPatchesKyle
     */
    public void ClickedBackButton(View view) {
        onBackPressed();
    }

    /*
     * Scrolling happens by default; this is designed to allow the scroll buttons to move the
     * category and word choices up and down in addition to scrolling by finger
     */
    public void setUpScroll()
    {
        up_btnW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word_scrollview.smoothScrollBy(0, -500);
            }
        });
        down_btnW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word_scrollview.smoothScrollBy(0, 500);
            }
        });
        up_btnT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type_scrollview.smoothScrollBy(0, -500);
            }
        });
        down_btnT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type_scrollview.smoothScrollBy(0, 500);
            }
        });
    }

    /**
     * Uses the Student class with a static method to get a list of all students that are saved locally.
     * Spins through this list and checks if the UUID that is being passed around matches one of these students.
     * This ensures that the student variable used on the page is the saved one, preventing a modified one
     * from being passed between pages without being saved to the system.
     */
    public void getStudentFromUUID()
    {
        ArrayList<Student> allStudents = Student.retrieveStudents(this.getApplicationContext());

        for (int index = 0; index < allStudents.size(); ++index) {
            if (allStudents.get(index).getUuid().equals(uuid)) {
                currStudent = allStudents.get(index); // HERE we assign the student so we can save on exit.
                break;
            }
        }
    }
    private void setScaledImage(ImageView imageView, final int resId) {
        final ImageView iv = imageView;
        ViewTreeObserver viewTreeObserver = iv.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                iv.getViewTreeObserver().removeOnPreDrawListener(this);
                int imageViewHeight = iv.getMeasuredHeight();
                int imageViewWidth = iv.getMeasuredWidth();
                Bitmap bp = decodeSampledBitmapFromResource(getApplicationContext().getResources(),
                        resId, imageViewWidth, imageViewHeight);
                iv.setImageBitmap(bp);
                iv.setImageBitmap(decodeSampledBitmapFromResource(getApplicationContext().getResources(),
                        resId, imageViewWidth, imageViewHeight));
                return true;
            }
        });
    }
    //needed to keep memory low
    private static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                          int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds = true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        //options.inJustDecodeBounds = true;
        return BitmapFactory.decodeResource(res, resId, options);
        /*
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);*/
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


}
