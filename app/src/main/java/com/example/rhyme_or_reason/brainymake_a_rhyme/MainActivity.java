package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

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
    int wordIndex, typeIndex;
    int height, width, elementWidth, elementHeight;
    final String typeBgColor = "#f4faf8";
    final int ELEMENTS_ON_SCREEN = 5;
    final int NUM_COLUMNS = 2;
    final int TEXT_HEIGHT = 200;
    final int SEPARATOR_HEIGHT = 20;
    final float LOCKED_ALPHA = 0.3f;
    final int TEXT_SIZE = 30;
    Typeface imprima;
    LinearLayout typeLL;
    LinearLayout wordLL;
    LinearLayout typeWrapper;
    RelativeLayout topBar;
    int HEIGHT_UNIT;
    private ScrollView word_scrollview;
    private ScrollView type_scrollview;
    private ImageButton up_btnW;
    private ImageButton down_btnW;
    private ImageButton up_btnT;
    private ImageButton down_btnT;


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

//        up_btnW = new ImageButton(this);
//        up_btnW.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        up_btnW.setImageResource(R.drawable.up_arrow);
//        up_btnW.setBackground(null);
//        up_btnW.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                type_scrollview.smoothScrollBy(0, -500);
//            }
//        });
//        type_scrollview.addView(up_btnT);

        ArrayList<Word> wordList = typeToWordMapping.get(currType);

        wordViews = new ArrayList<>(); // Wipe out existing entries

        for (int index = 0; index < wordList.size(); ++index) {

            Button tempWordImage = new Button(this);

            tempWordImage.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, elementHeight));

            tempWordImage.setTag(wordList.get(index).getText());
            tempWordImage.setOnClickListener(MainActivity.this);
            int resourceID = getResources().getIdentifier(wordList.get(index).getImageName(), "drawable", getPackageName());
            tempWordImage.setBackgroundResource(resourceID);

            Button tempWordText = new Button(this);

            tempWordText.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, TEXT_HEIGHT));

            tempWordText.setOnClickListener(MainActivity.this);

            tempWordText.setTag(wordList.get(index).getText());
            if(currType != "Friends")
                tempWordText.setText(wordList.get(index).getText());
            tempWordText.setBackgroundColor(Color.WHITE);
            tempWordText.setTextColor(Color.BLACK);
            tempWordText.setTextSize(TEXT_SIZE);
            tempWordText.setTypeface(imprima);

            View separator = new View(this);

            separator.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, SEPARATOR_HEIGHT));

            if (wordList.get(index).getLockedStatus()) {
                // Is Locked
                tempWordImage.setAlpha(LOCKED_ALPHA);
            } else {
                // Is Unlocked
                tempWordImage.setAlpha(1);
            }

            wordViews.add(tempWordImage);
            wordLL.addView(tempWordImage);
            wordLL.addView(tempWordText);
            wordLL.addView(separator);
        }
//        down_btnW = new FloatingActionButton(this);
//        down_btnW.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        down_btnW.setImageResource(R.drawable.down_arrow);
//        down_btnW.setBackground(null);
//        down_btnW.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                word_scrollview.smoothScrollBy(0, 500);
//            }
//        });
//        wordLL.addView(down_btnW);
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
                        "collie", "cow", "cub", "dog", "dogs", "donkey", "elk", /*"fly",*/ "fox",
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
                {"Describe", "afraid", "cloudy", "dark", "eight", "eight", "five", "high", "hot",
                        "loud", "naughty", "old", "quiet", "rude", "silly", "six", "sixteen",
                        "sleepy", "slow", "smart", "stripes", "twelve"},
                {"Colors", "black", "blue", "brown", "gold", "green", "purple", "red", "silver",
                        "white", "yellow"}
        };
        for(int i = 0; i < words.length; i++)
        {
            typeList.add(words[i][0]);
            ArrayList<Word> wordlist = new ArrayList<>();
            for(int j = 1; j < words[i].length; j++)
                wordlist.add(new Word(words[i][j].replaceAll("_"," "), true,
                        words[i][j], words[i][j], words[i][0].toLowerCase()));
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
                    typeViews.get(index).setBackgroundColor(Color.parseColor(typeBgColor));
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
                if (!(v.getTag().equals(wordList.get(index).getText()))) {
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
                    if (s != activeType)
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
                        startActivityForResult(newIntent, 1);
                    }
                }
            }
            else
            {
                //Friends
                Intent newIntent = new Intent(this, NameFriend.class);
                newIntent.putExtra("word", selectedWord);
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

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                selectedWord.setUnlocked();
                wordViews.get(wordIndex).setAlpha(1);
                // Exit quiz
                Handler returnHandler = new Handler();
                returnHandler.postDelayed(new Runnable() {
                    public void run() {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("word", selectedWord);
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
                        returnIntent.putExtra("word", new Word(name, false, selectedWord.getImageName(), name, "Friends"));
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
        //elementHeight = height / ELEMENTS_ON_SCREEN;
        elementWidth = width / NUM_COLUMNS;
        elementHeight = elementWidth; // This is to keep the aspect ratio consistent (temporary)

        HEIGHT_UNIT = height / 10;
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
     * Creates the buttons that serve as options for the word being spoken
     */
    public void loadIntentsAndViews()
    {
        word_scrollview = findViewById(R.id.WordScrollView);
        type_scrollview = findViewById(R.id.TypeScrollView);
        up_btnW = findViewById(R.id.WordScrollUpBtn);
        down_btnW = findViewById(R.id.WordScrollDownBtn);
        up_btnT = findViewById(R.id.TypeScrollUpBtn);
        down_btnT = findViewById(R.id.TypeScrollDownBtn);
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
//        typeWrapper = findViewById(R.id.TypeWrapper);
//        scrollColumns = findViewById(R.id.ScrollingCols);
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
            tempType.setBackgroundColor(Color.parseColor(typeBgColor));
            tempType.setTextSize(TEXT_SIZE);
            tempType.setTypeface(imprima);
            tempType.setOnClickListener(MainActivity.this);

            View separator = new View(this);

            separator.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, SEPARATOR_HEIGHT));

            separator.setBackgroundColor(getResources().getColor(R.color.colorBackground));

            //up_btnT.setLayoutParams();
            typeViews.add(tempType);
            typeLL.addView(tempType);
            typeLL.addView(generateImagePreview(tempType));
            typeLL.addView(separator);
        }
//        down_btnT = new ImageButton(this);
//        down_btnT.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        down_btnT.setImageResource(R.drawable.down_arrow);
//        down_btnT.setBackground(null);
//        down_btnT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                type_scrollview.smoothScrollBy(0, 500);
//            }
//        });
//        typeWrapper.addView(down_btnT, 0);
//        typeWrapper.addView(typeLL, 1);
    }

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

            sampleImage.setImageResource(pictureResourceID);
            linearElement.addView(sampleImage);
        }
        return linearElement;
    }

    public void ClickedBackButton(View view) {
        onBackPressed();
    }

}
