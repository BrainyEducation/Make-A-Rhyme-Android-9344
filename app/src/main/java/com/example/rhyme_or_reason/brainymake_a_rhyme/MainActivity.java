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
        LinearLayout wordLL = findViewById(R.id.WordLL);
        wordLL.removeAllViews(); // Removes the current buttons

        ArrayList<Word> wordList = typeToWordMapping.get(currType);

        for (int index = 0; index < wordList.size(); ++index) {
            Button tempWord = new Button(this);

            tempWord.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

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
        animals.add(new Word("Ape", true));
        animals.add(new Word("Bat", true));
        animals.add(new Word("Bear", true));
        animals.add(new Word("Bee", true));
        animals.add(new Word("Cat", true));
        animals.add(new Word("Centipede", true));

        typeToWordMapping.put("Animals", animals);

        // Birds
        typeList.add("Birds");
        ArrayList<Word> birds = new ArrayList<>();
        birds.add(new Word("Bird", true));
        birds.add(new Word("Canary", true));
        birds.add(new Word("Jay", true));
        birds.add(new Word("Ostrich", true));
        birds.add(new Word("Owl", true));
        birds.add(new Word("Parrot", true));
        birds.add(new Word("Swan", true));

        typeToWordMapping.put("Birds", birds);

        typeList.add("Body Parts");
        ArrayList<Word> bodyParts = new ArrayList<>();
        bodyParts.add(new Word("Ankle", true));
        bodyParts.add(new Word("Chin", true));
        bodyParts.add(new Word("Elbow", true));
        bodyParts.add(new Word("Face", true));
        bodyParts.add(new Word("Feet", true));
        bodyParts.add(new Word("Foot", true));
        bodyParts.add(new Word("Hair", true));
        bodyParts.add(new Word("Hand", true));
        bodyParts.add(new Word("Head", true));

        typeToWordMapping.put("Body Parts", bodyParts);

        typeList.add("Clothing");
        ArrayList<Word> clothing = new ArrayList<>();
        clothing.add(new Word("Boots", true));
        clothing.add(new Word("Clothes", true));
        clothing.add(new Word("Glove", true));
        clothing.add(new Word("Hoodie", true));
        clothing.add(new Word("Jacket", true));
        clothing.add(new Word("Purse", true));
        clothing.add(new Word("Ring", true));
        clothing.add(new Word("Scarf", true));
        clothing.add(new Word("Shirt", true));

        typeToWordMapping.put("Clothing", clothing);

        typeList.add("Colors");
        ArrayList<Word> colors = new ArrayList<>();
        colors.add(new Word("Black", true));
        colors.add(new Word("Blue", true));
        colors.add(new Word("Brown", true));
        colors.add(new Word("Gold", true));
        colors.add(new Word("Green", true));
        colors.add(new Word("Orange", true));
        colors.add(new Word("Purple", true));
        colors.add(new Word("Red", true));

        typeToWordMapping.put("Colors", colors);

        typeList.add("Describing");
        ArrayList<Word> describing = new ArrayList<>();
        describing.add(new Word("Afraid", true));
        describing.add(new Word("Cloudy", true));
        describing.add(new Word("Dark", true));
        describing.add(new Word("High", true));
        describing.add(new Word("Hot", true));
        describing.add(new Word("Loud", true));
        describing.add(new Word("Naughty", true));

        typeToWordMapping.put("Describing", describing);

        typeList.add("Food");
        ArrayList<Word> food = new ArrayList<>();
        food.add(new Word("Apple", true));
        food.add(new Word("Bread", true));
        food.add(new Word("Burger", true));
        food.add(new Word("Cake", true));
        food.add(new Word("Candy", true));
        food.add(new Word("Carrots", true));
        food.add(new Word("Cone", true));
        food.add(new Word("Cookies", true));

        typeToWordMapping.put("Food", food);
    }

    public void onClick(View v) {

        ImageView topIV = findViewById(R.id.TopImageView);

        for (int index = 0; index < typeViews.size(); ++index) {
            typeViews.get(index).setBackgroundColor(Color.parseColor("#add8e6"));
        }

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

        v.setBackgroundColor(Color.parseColor("#9370DB"));

        updateWordList((String)v.getTag());
    }

}
