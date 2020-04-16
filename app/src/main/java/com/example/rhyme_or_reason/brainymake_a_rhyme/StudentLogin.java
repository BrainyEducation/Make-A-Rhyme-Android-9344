package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

public class StudentLogin extends AppCompatActivity implements View.OnClickListener {

    int height, width, elementWidth, elementHeight;
    int NUM_COLUMNS = 2;
    int HEIGHT_UNIT = 0;
    LinearLayout animalLL, colorLL, username_LL;
    ScrollView color_scrollview, animal_scrollview, username_scrollview;
    Button goButton;
    Boolean selectedAnimal = false;
    Boolean selectedColor = false;
    String chosenColor = "";
    String chosenAnimal = "";
    int chosenStudentIndex = -1;
    ArrayList<String> colorNames = new ArrayList<>();
    ArrayList<String> animalNames = new ArrayList<>();
    ArrayList<Button> animalViews = new ArrayList<>();
    ArrayList<Button> colorViews = new ArrayList<>();
    ArrayList<String> usernameNames = new ArrayList<>();
    ArrayList<Button> usernameViews = new ArrayList<>();
    EditText nameField;
    final int TEXT_SIZE = 25;
    final int TEXT_HEIGHT = 200;

    Typeface imprima;
    final int SEPARATOR_HEIGHT = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        sizingSetUp();
        loadIntentsAndViews();

        miscellaneousSetUp();

        setUpUserNames();

        setUpColors();
        setUpAnimals();
    }

    public void ClickedBackButton(View v) {
        onBackPressed();
    }

    /**
     * When someone hits the back arrow from the student main menu after making a new account,
     * shouldn't be taken back to this screen, so automatically pop backwards
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        finish();
        //System.out.println("SL: On Activity Result.");
        //onBackPressed();
    }

    public void miscellaneousSetUp() {
        imprima = ResourcesCompat.getFont(this, R.font.imprima);
    }

    /*
     * Child can choose one of six colors to be the color portion of their password.
     * Using animals and colors is done since they are easier for children to remember than
     * passwords and tight security is not essential for the application
     */
    public void setUpColors() {
        colorNames.add("blue");
        colorNames.add("green");
        colorNames.add("red");
        colorNames.add("yellow");
        colorNames.add("purple");
        colorNames.add("orange");

        for (int index = 0; index < colorNames.size(); ++index) {
            int resourceID = getResources().getIdentifier(colorNames.get(index), "drawable", getPackageName());
            Button tempColorImage = new Button(this);

            tempColorImage.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, elementHeight));

            tempColorImage.setTag(index + " color");
            tempColorImage.setOnClickListener(StudentLogin.this);
            tempColorImage.setBackgroundResource(resourceID);
            tempColorImage.setAlpha(0.5f);

            View separator = new View(this);

            separator.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, Constants.SEPARATOR_HEIGHT));

            colorViews.add(tempColorImage);
            colorLL.addView(tempColorImage);
            colorLL.addView(separator);
        }
    }

    /*
     * Child can choose one of six animals to be the animal portion of their password.
     * Using animals and colors is done since they are easier for children to remember than
     * passwords and tight security is not essential for the application
     */
    public void setUpAnimals() {
        animalNames.add("cat");
        animalNames.add("dog");
        animalNames.add("bee");
        animalNames.add("ape");
        animalNames.add("fox");
        animalNames.add("zebra");

        for (int index = 0; index < animalNames.size(); ++index) {
            int resourceID = getResources().getIdentifier(animalNames.get(index), "drawable", getPackageName());
            Button tempAnimalImage = new Button(this);

            tempAnimalImage.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, elementHeight));

            tempAnimalImage.setTag(index + " animal");
            tempAnimalImage.setOnClickListener(StudentLogin.this);
            tempAnimalImage.setBackgroundResource(resourceID);
            tempAnimalImage.setAlpha(0.5f);

            View separator = new View(this);

            separator.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, Constants.SEPARATOR_HEIGHT));

            animalViews.add(tempAnimalImage);
            animalLL.addView(tempAnimalImage);
            animalLL.addView(separator);
        }
    }

    /*
     * When an animal is clicked, sets the animal to be lit up and makes it the child's selection.
     * When a color is clicked, sets the color to be lit up and makes it the child's selection.
     * When a student's name is clicked, sets the name to be green.
     * When all three selections have been made, the student can attempt to login.
     */
    public void onClick(View v) {
        String tag = (String) v.getTag();

        String[] splitTag = tag.split("\\s+"); // Split on spaces

        System.out.println(tag);

        int index = Integer.parseInt(splitTag[0]);

        System.out.println(index);

        tag = splitTag[1]; // cut off the number and the space

        if (tag.equals("animal")) {
            selectedAnimal = true;
            chosenAnimal = animalNames.get(index);
            for (int count = 0; count < animalNames.size(); ++count) {
                animalViews.get(count).setAlpha(0.5f);
            }
            animalViews.get(index).setAlpha(1.0f);

        } else if (tag.equals("color")) {
            selectedColor = true;
            chosenColor = colorNames.get(index);
            for (int count = 0; count < colorNames.size(); ++count) {
                colorViews.get(count).setAlpha(0.5f);
            }
            colorViews.get(index).setAlpha(1.0f);
        } else {
            for (int count = 0; count < usernameNames.size(); ++count) {
                usernameViews.get(count).setBackgroundColor(Color.WHITE);
                usernameViews.get(count).setTextColor(Color.BLACK);
            }

            usernameViews.get(index).setBackgroundColor(getResources().getColor(R.color.darkGreen));
            usernameViews.get(index).setTextColor(Color.WHITE);

            chosenStudentIndex = index;
        }

        if (selectedAnimal && selectedColor) {
            goButton.setAlpha(1.0f);
        }
    }

    /**
     * Launches the student registration UI
     */
    public void ClickedGo(View v) {

        ArrayList<Student> allStudents = Student.retrieveStudents(this.getApplicationContext());

        Student selectedStudent = allStudents.get(chosenStudentIndex);

        if (selectedStudent.getAnimalPassword().equals(chosenAnimal) && selectedStudent.getColorPassword().equals(chosenColor)) {
            Intent newIntent = new Intent(this, MainMenu.class);
            newIntent.putExtra("uuid", selectedStudent.getUuid());
            startActivityForResult(newIntent, 1);
        } else {
            Toast.makeText(getApplicationContext(), "Incorrect login, try again", Toast.LENGTH_LONG).show();
            for (int count = 0; count < animalNames.size(); ++count) {
                animalViews.get(count).setAlpha(0.5f);
            }
            for (int count = 0; count < colorNames.size(); ++count) {
                colorViews.get(count).setAlpha(0.5f);
            }
        }
    }

    /**
     * Called when the page first loads; gets dimensions to help calculate how to dynamically lay
     * out the screen
     */
    public void sizingSetUp() {
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
     * Creates the buttons that serve as options for the word being spoken
     */
    public void loadIntentsAndViews() {
        color_scrollview = findViewById(R.id.ColorScrollView);
        animal_scrollview = findViewById(R.id.AnimalScrollView);
        username_scrollview = findViewById(R.id.UsernameScrollView);
        username_LL = findViewById(R.id.usernameLL);

        colorLL = findViewById(R.id.ColorLL);
        animalLL = findViewById(R.id.AnimalLL);
        goButton = findViewById(R.id.Go);
        goButton.setAlpha(0.0f);
        nameField = findViewById(R.id.name);
    }

    public void setUpUserNames()
    {
        ArrayList<Student> allStudents = Student.retrieveStudents(this.getApplicationContext());

        username_LL.removeAllViews();

        for (int index = 0; index < allStudents.size(); ++index) {

            Button nameText = new Button(this);

            nameText.setLayoutParams(new LinearLayout.LayoutParams(width, TEXT_HEIGHT));

            nameText.setOnClickListener(StudentLogin.this);

            nameText.setTag(index + " " + allStudents.get(index).getName());
            nameText.setText(allStudents.get(index).getName());
            nameText.setBackgroundColor(Color.WHITE);
            nameText.setTextColor(Color.BLACK);
            nameText.setTextSize(TEXT_SIZE);
            nameText.setTypeface(imprima);

            usernameViews.add(nameText);
            usernameNames.add(allStudents.get(index).getName());

            View separator = new View(this);

            separator.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, SEPARATOR_HEIGHT));

            username_LL.addView(nameText);
            username_LL.addView(separator);
        }

    }
}