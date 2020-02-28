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
    //final int SEPARATOR_HEIGHT = 20;
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
    private Button progressBtn;


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
            tempWordText.setText(wordList.get(index).getText());
            tempWordText.setBackgroundColor(Color.WHITE);
            tempWordText.setTextColor(Color.BLACK);
            tempWordText.setTextSize(TEXT_SIZE);
            tempWordText.setTypeface(imprima);

            View separator = new View(this);

            separator.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, Constants.SEPARATOR_HEIGHT));

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
        // Animals
        typeList.add("Animals");
        ArrayList<Word> animals = new ArrayList<>();
        animals.add(new Word("ape", true, "ape", "ape", "animals"));
        animals.add(new Word("bat", true, "bat", "bat", "animals"));
        //animals.get(1).setUnlocked(); // temporarily unlock bat
        //animals.add(new Word("bear", true, "bear", "bear"));
        //animals.add(new Word("bee", true, "bee", "bee"));
        animals.add(new Word("cat", true, "cat", "cat", "animals"));
        //animals.add(new Word("centipede", true, "centipede", "centipede"));
        animals.add(new Word("collie", true, "collie", "collie", "animals"));
        animals.add(new Word("cow", true, "cow", "cow", "animals"));
        animals.add(new Word("dog", true, "dog", "dog", "animals"));
        animals.add(new Word("dogs", true, "dogs", "dogs", "animals"));
        //animals.add(new Word("donkey", true, "donkey", "donkey"));
        animals.add(new Word("fox", true, "fox", "fox", "animals"));
        animals.add(new Word("goat", true, "goat", "goat", "animals"));
        animals.add(new Word("kitten", true, "kitten", "kitten", "animals"));
        animals.add(new Word("mole", true, "mole", "mole", "animals"));
        //animals.add(new Word("monkey", true, "monkey", "monkey"));
        animals.add(new Word("moth", true, "moth", "moth", "animals"));
        animals.add(new Word("mouse", true, "mouse", "mouse", "animals"));
        //animals.add(new Word("pets", true, "pets", "pets"));
        animals.add(new Word("rabbit", true, "rabbit", "rabbit", "animals"));
        animals.add(new Word("sheep", true, "sheep", "sheep", "animals"));
        animals.add(new Word("skunk", true, "skunk", "skunk", "animals"));
        animals.add(new Word("snail", true, "snail", "snail", "animals"));
        animals.add(new Word("swan", true, "swan", "swan", "animals"));
        //animals.add(new Word("tiger", true, "tiger", "tiger"));
        //animals.add(new Word("toad", true, "toad", "toad"));
        animals.add(new Word("wasp", true, "wasp", "wasp", "animals"));
        animals.add(new Word("wolf", true, "wolf", "wolf", "animals"));
        animals.add(new Word("zebra", true, "zebra", "zebra", "animals"));

        typeToWordMapping.put("Animals", animals);


        // Birds
        typeList.add("Birds");
        ArrayList<Word> birds = new ArrayList<>();
        //birds.add(new Word("bird", true, "bird", "bird"));
        birds.add(new Word("canary", true, "canary", "canary", "birds"));
        //birds.add(new Word("jay", true, "jay", "jay"));
        birds.add(new Word("ostrich", true, "ostrich", "ostrich", "birds"));
        birds.add(new Word("owl", true, "owl", "owl", "birds"));
        //birds.add(new Word("parrot", true, "parrot", "parrot"));
        birds.add(new Word("swan", true, "swan", "swan", "birds"));

        typeToWordMapping.put("Birds", birds);


        typeList.add("Body Parts");
        ArrayList<Word> bodyParts = new ArrayList<>();
        //bodyParts.add(new Word("Ankle", true, "ankle", "ankle"));
        //bodyParts.add(new Word("Chin", true, "chin", "chin"));
        //bodyParts.add(new Word("Elbow", true, "elbow", "elbow"));
        bodyParts.add(new Word("face", true, "face", "face", "body parts"));
        //bodyParts.add(new Word("Feet", true, "feet", "feet"));
        bodyParts.add(new Word("foot", true, "foot", "foot", "body parts"));
        bodyParts.add(new Word("hair", true, "hair", "hair", "body parts"));
        bodyParts.add(new Word("hand", true, "hand", "hand", "body parts"));
        //bodyParts.add(new Word("Head", true, "head", "head"));
        bodyParts.add(new Word("mouth", true, "mouth", "mouth", "body parts"));
        bodyParts.add(new Word("nose", true, "nose", "nose", "body parts"));

        //bodyParts.add(new Word("paw", true, "paw", "paw"));
        //bodyParts.add(new Word("shin", true, "shin", "shin"));
        //bodyParts.add(new Word("stripes", true, "stripes", "stripes"));
        bodyParts.add(new Word("tail", true, "tail", "tail", "body parts"));
        //bodyParts.add(new Word("thigh", true, "thigh", "thigh"));
        bodyParts.add(new Word("thumb", true, "thumb", "thumb", "body parts"));
        bodyParts.add(new Word("toe", true, "toe", "toe", "body parts"));

        typeToWordMapping.put("Body Parts", bodyParts);

        typeList.add("Clothing");
        ArrayList<Word> clothing = new ArrayList<>();
        clothing.add(new Word("boots", true, "boots", "boots", "clothing"));
        clothing.add(new Word("clothes", true, "clothes", "clothes", "clothing"));
        clothing.add(new Word("glove", true, "glove", "glove", "clothing"));
        //clothing.add(new Word("hoodie", true, "hoodie", "hoodie"));
        //clothing.add(new Word("jacket", true, "jacket", "jacket"));
        clothing.add(new Word("purse", true, "purse", "purse", "clothing"));
        //clothing.add(new Word("ring", true, "ring", "ring"));
        clothing.add(new Word("scarf", true, "scarf", "scarf", "clothing"));
        clothing.add(new Word("shirt", true, "shirt", "shirt", "clothing"));
        clothing.add(new Word("scarf", true, "scarf", "scarf", "clothing"));
        clothing.add(new Word("shirt", true, "skirt", "skirt", "clothing"));
        clothing.add(new Word("skirt", true, "shirt", "shirt", "clothing"));
        clothing.add(new Word("suit", true, "suit", "suit", "clothing"));
        clothing.add(new Word("tie", true, "tie", "tie", "clothing"));
        //clothing.add(new Word("veil", true, "veil", "veil"));
        clothing.add(new Word("wig", true, "wig", "wig", "clothing"));

        typeToWordMapping.put("Clothing", clothing);

        typeList.add("Colors");
        ArrayList<Word> colors = new ArrayList<>();
        //colors.add(new Word("Black", true, "black", "black"));
        colors.add(new Word("blue", true, "blue", "blue", "colors"));
        colors.add(new Word("brown", true, "brown", "brown", "colors"));
        colors.add(new Word("gold", true, "gold", "gold", "colors"));
        //colors.add(new Word("green", true, "green", "green"));
        //colors.add(new Word("orange", true, "orange", "orange"));
        colors.add(new Word("purple", true, "purple", "purple", "colors"));
        //colors.add(new Word("red", true, "red", "red"));
        colors.add(new Word("silver", true, "silver", "silver", "colors"));
        colors.add(new Word("yellow", true, "yellow", "yellow", "colors"));
        typeToWordMapping.put("Colors", colors);

        typeList.add("Describing");
        ArrayList<Word> describing = new ArrayList<>();
        describing.add(new Word("afraid", true, "afraid", "afraid", "describing"));
        describing.add(new Word("cloudy", true, "cloudy", "cloudy", "describing"));
        describing.add(new Word("dark", true, "dark", "dark", "describing"));
        //describing.add(new Word("high", true, "high", "high"));
        describing.add(new Word("hot", true, "hot", "hot", "describing"));
        //describing.add(new Word("loud", true, "loud", "loud"));
        //describing.add(new Word("naughty", true, "naughty", "naughty"));
        describing.add(new Word("old", true, "old", "old", "describing"));
        describing.add(new Word("quiet", true, "quiet", "quiet", "describing"));
        describing.add(new Word("silly", true, "silly", "silly", "describing"));
        //describing.add(new Word("old", true, "old", "old"));
        describing.add(new Word("sleepy", true, "sleepy", "sleepy", "describing"));
        //describing.add(new Word("slow", true, "slow", "slow"));
        //describing.add(new Word("smart", true, "smart", "smart"));

        typeToWordMapping.put("Describing", describing);

        typeList.add("Food");
        ArrayList<Word> food = new ArrayList<>();
        //food.add(new Word("apple", true, "apple", "apple"));
        //food.add(new Word("bread", true, "bread", "bread"));
        //food.add(new Word("burger", true, "burger", "burger"));
        food.add(new Word("cake", true, "cake", "cake", "food"));
        food.add(new Word("candy", true, "candy", "candy", "food"));
        //food.add(new Word("carrots", true, "carrots", "carrots"));
        //food.add(new Word("cone", true, "cone", "cone"));
        food.add(new Word("cookies", true, "cookies", "cookies", "food"));
        food.add(new Word("corn", true, "corn", "corn", "food"));
        food.add(new Word("grapes", true, "grapes", "grapes", "food"));
        food.add(new Word("nuts", true, "nuts", "nuts", "food"));
        //food.add(new Word("hotdog", true, "hotdog", "hotdog"));
        //food.add(new Word("lettuce", true, "lettuce", "lettuce"));
        //food.add(new Word("milk", true, "milk", "milk"));
        food.add(new Word("pie", true, "pie", "pie", "food"));
        food.add(new Word("plum", true, "plum", "plum", "food"));
        food.add(new Word("pretzel", true, "pretzel", "pretzel", "food"));
        //food.add(new Word("snack", true, "snack", "snack"));
        //food.add(new Word("tea", true, "tea", "tea"));

        typeToWordMapping.put("Food", food);

        typeList.add("Numbers");
        ArrayList<Word>  numbers = new ArrayList<>();
        numbers.add(new Word("five", true, "five", "five", "numbers"));
        numbers.add(new Word("six", true, "six", "six", "numbers"));
        //numbers.add(new Word("eight", true, "eight", "eight"));
        //numbers.add(new Word("twelve", true, "twelve", "twelve"));
        numbers.add(new Word("sixteen", true, "sixteen", "sixteen", "numbers"));

        typeToWordMapping.put("Numbers", numbers);
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

            for(String s: typeToWordMapping.keySet()) {
                if (s != activeType)
                    for(Word w: typeToWordMapping.get(s)) {
                        if (w.getText().length() == selectedWord.getText().length())
                            lengthWords.add(w.getText());
                        else if(w.getText().charAt(0) == selectedWord.getText().charAt(0))
                            letterWords.add(w.getText());
                        else
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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

            separator.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, Constants.SEPARATOR_HEIGHT));

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
