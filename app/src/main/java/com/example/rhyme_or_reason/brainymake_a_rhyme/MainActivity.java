package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<View>typeViews = new ArrayList<>();
    ArrayList<View>wordViews = new ArrayList<>();
    ArrayList<String> typeList = new ArrayList<>();
    HashMap<String, ArrayList<Word> > typeToWordMapping = new HashMap<String, ArrayList<Word> >();
    String activeType = "";

    /**
     * Launches when the page appears; sets up the types on the left side of the screen and loads
     * the default words in on the right side of the screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout typeLL = findViewById(R.id.TypeLL);

        loadTypeToWordMappings();

        for (int index = 0; index < typeList.size(); ++index) {
            Button tempType = new Button(this);

            tempType.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            tempType.setText(typeList.get(index));
            tempType.setTag(typeList.get(index)); // Set tag to the name of the type
            tempType.setBackgroundColor(Color.parseColor("#add8e6"));
            tempType.setOnClickListener(MainActivity.this);

            typeViews.add(tempType);
            typeLL.addView(tempType);
        }

        onClick(typeViews.get(0));
        //updateWordList("Animals"); // Default Set-Up
    }

    /**
     * Responsible for changing the words on the right side of the screen when a type is selected;
     * replaces the buttons on the screen (instead of overwriting the text on the buttons) and
     * ensures the appropriate lock statuses are shown (gray -> locked, light pink -> unlocked)
     */
    public void updateWordList(String currType)
    {
        activeType = currType;
        LinearLayout wordLL = findViewById(R.id.WordLL);
        wordLL.removeAllViews(); // Removes the current buttons

        ArrayList<Word> wordList = typeToWordMapping.get(currType);

        for (int index = 0; index < wordList.size(); ++index) {
            Button tempWord = new Button(this);

            tempWord.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            tempWord.setOnClickListener(MainActivity.this);

            tempWord.setTag(wordList.get(index).getText());
            tempWord.setText(wordList.get(index).getText());
            if (wordList.get(index).getLockedStatus()) {
                // Is Locked
                tempWord.setBackgroundColor(Color.parseColor("#cccccc"));
            } else {
                // Is Unlocked
                tempWord.setBackgroundColor(Color.parseColor("#ffe5e9"));
            }

            wordViews.add(tempWord);
            wordLL.addView(tempWord);
        }
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
        animals.add(new Word("Ape", true, "ape", "ape"));
        animals.add(new Word("Bat", true, "bat", "bat"));
        //animals.add(new Word("Bear", true, "bear", "bear"));
        //animals.add(new Word("Bee", true, "bee", "bee"));
        animals.add(new Word("Cat", true, "cat", "cat"));
        //animals.add(new Word("Centipede", true, "centipede", "centipede"));
        animals.add(new Word("Collie", true, "collie", "collie"));
        animals.add(new Word("Cow", true, "cow", "cow"));
        animals.add(new Word("Dog", true, "dog", "dog"));

        typeToWordMapping.put("Animals", animals);

        // Birds
        typeList.add("Birds");
        ArrayList<Word> birds = new ArrayList<>();
        //birds.add(new Word("Bird", true, "bird", "bird"));
        birds.add(new Word("Canary", true, "canary", "canary"));
        //birds.add(new Word("Jay", true, "jay", "jay"));
        birds.add(new Word("Ostrich", true, "ostrich", "ostrich"));
        birds.add(new Word("Owl", true, "owl", "owl"));
        //birds.add(new Word("Parrot", true, "parrot", "parrot"));
        birds.add(new Word("Swan", true, "swan", "swan"));

        typeToWordMapping.put("Birds", birds);

        typeList.add("Body Parts");
        ArrayList<Word> bodyParts = new ArrayList<>();
        //bodyParts.add(new Word("Ankle", true, "ankle", "ankle"));
        //bodyParts.add(new Word("Chin", true, "chin", "chin"));
        //bodyParts.add(new Word("Elbow", true, "elbow", "elbow"));
        bodyParts.add(new Word("Face", true, "face", "face"));
        //bodyParts.add(new Word("Feet", true, "feet", "feet"));
        bodyParts.add(new Word("Foot", true, "foot", "foot"));
        bodyParts.add(new Word("Hair", true, "hair", "hair"));
        bodyParts.add(new Word("Hand", true, "hand", "hand"));
        //bodyParts.add(new Word("Head", true, "head", "head"));

        typeToWordMapping.put("Body Parts", bodyParts);

        typeList.add("Clothing");
        ArrayList<Word> clothing = new ArrayList<>();
        clothing.add(new Word("Boots", true, "boots", "boots"));
        clothing.add(new Word("Clothes", true, "clothes", "clothes"));
        clothing.add(new Word("Glove", true, "glove", "glove"));
        //clothing.add(new Word("Hoodie", true, "hoodie", "hoodie"));
        //clothing.add(new Word("Jacket", true, "jacket", "jacket"));
        clothing.add(new Word("Purse", true, "purse", "purse"));
        //clothing.add(new Word("Ring", true, "ring", "ring"));
        clothing.add(new Word("Scarf", true, "scarf", "scarf"));
        clothing.add(new Word("Shirt", true, "shirt", "shirt"));

        typeToWordMapping.put("Clothing", clothing);

        typeList.add("Colors");
        ArrayList<Word> colors = new ArrayList<>();
        //colors.add(new Word("Black", true, "black", "black"));
        colors.add(new Word("Blue", true, "blue", "blue"));
        colors.add(new Word("Brown", true, "brown", "brown"));
        colors.add(new Word("Gold", true, "gold", "gold"));
        //colors.add(new Word("Green", true, "green", "green"));
        //colors.add(new Word("Orange", true, "orange", "orange"));
        colors.add(new Word("Purple", true, "purple", "purple"));
        //colors.add(new Word("Red", true, "red", "red"));

        typeToWordMapping.put("Colors", colors);

        typeList.add("Describing");
        ArrayList<Word> describing = new ArrayList<>();
        describing.add(new Word("Afraid", true, "afraid", "afraid"));
        describing.add(new Word("Cloudy", true, "cloudy", "cloudy"));
        describing.add(new Word("Dark", true, "dark", "dark"));
        //describing.add(new Word("High", true, "high", "high"));
        describing.add(new Word("Hot", true, "hot", "hot"));
        //describing.add(new Word("Loud", true, "loud", "loud"));
        //describing.add(new Word("Naughty", true, "naughty", "naughty"));

        typeToWordMapping.put("Describing", describing);

        typeList.add("Food");
        ArrayList<Word> food = new ArrayList<>();
        //food.add(new Word("Apple", true, "apple", "apple"));
        //food.add(new Word("Bread", true, "bread", "bread"));
        //food.add(new Word("Burger", true, "burger", "burger"));
        food.add(new Word("Cake", true, "cake", "cake"));
        food.add(new Word("Candy", true, "candy", "candy"));
        //food.add(new Word("Carrots", true, "carrots", "carrots"));
        //food.add(new Word("Cone", true, "cone", "cone"));
        food.add(new Word("Cookies", true, "cookies", "cookies"));

        typeToWordMapping.put("Food", food);
    }

    public void onClick(View v) {

        ImageView topIV = findViewById(R.id.TopImageView);

        for (int index = 0; index < typeViews.size(); ++index) {
            typeViews.get(index).setBackgroundColor(Color.parseColor("#add8e6"));
        }

        // Check all Types first
        if (v.getTag().equals("Animals")) {
            topIV.setBackgroundResource(R.drawable.paw);
        } else if (v.getTag().equals("Birds")) {
            topIV.setBackgroundResource(R.drawable.bird);
        } else if (v.getTag().equals("Body Parts")) {
            topIV.setBackgroundResource(R.drawable.ankle);
        } else if (v.getTag().equals("Clothing")) {
            topIV.setBackgroundResource(R.drawable.clothes);
        } else if (v.getTag().equals("Colors")) {
            topIV.setBackgroundResource(R.drawable.blue);
        } else if (v.getTag().equals("Describing")) {
            topIV.setBackgroundResource(R.drawable.cloudy);
        } else if (v.getTag().equals("Food")) {
            topIV.setBackgroundResource(R.drawable.apple);
        }

        Boolean switchingActivities = false;

        if (!activeType.equals("")) {
            ArrayList<Word> wordList = typeToWordMapping.get(activeType);

            ArrayList<String> wrongWords = new ArrayList<>();

            int matchIndex = -1;

            for (int index = 0; index < wordList.size(); ++index) {
                if (!(v.getTag().equals(wordList.get(index).getText()))) {
                    wrongWords.add(wordList.get(index).getText());
                } else {
                    matchIndex = index;
                    switchingActivities = true;
                }
            }

            if (switchingActivities) {
                Intent newIntent = new Intent(this, Quiz.class);
                newIntent.putExtra("word", wordList.get(matchIndex));
                newIntent.putExtra("wrong_words", wrongWords);
                startActivity(newIntent);
            }
        }

        if (!switchingActivities) {
            // Only executes if the selected item was a Type (will leave screen for new activity if not)
            v.setBackgroundColor(Color.parseColor("#9370DB"));

            updateWordList((String) v.getTag());
        }
    }
}
