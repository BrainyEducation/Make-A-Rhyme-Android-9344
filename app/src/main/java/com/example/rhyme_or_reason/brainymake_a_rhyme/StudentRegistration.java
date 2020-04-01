package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
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

public class StudentRegistration extends AppCompatActivity implements View.OnClickListener {

    int height, width, elementWidth, elementHeight;
    int NUM_COLUMNS = 2;
    int HEIGHT_UNIT = 0;
    LinearLayout animalLL, colorLL;
    ScrollView color_scrollview, animal_scrollview;
    Button createAccountButton;
    Boolean selectedAnimal = false;
    Boolean selectedColor = false;
    String chosenColor = "";
    String chosenAnimal = "";
    ArrayList<String> colorNames = new ArrayList<>();
    ArrayList<String> animalNames = new ArrayList<>();
    ArrayList<Button> animalViews = new ArrayList<>();
    ArrayList<Button> colorViews = new ArrayList<>();
    EditText nameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        sizingSetUp();
        loadIntentsAndViews();

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
        onBackPressed();
    }

    public void setUpColors()
    {
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
            tempColorImage.setOnClickListener(StudentRegistration.this);
            tempColorImage.setBackgroundResource(resourceID);
            tempColorImage.setAlpha(0.5f);

            View separator = new View(this);

            separator.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, Constants.SEPARATOR_HEIGHT));

            colorViews.add(tempColorImage);
            colorLL.addView(tempColorImage);
            colorLL.addView(separator);
        }
    }

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
            tempAnimalImage.setOnClickListener(StudentRegistration.this);
            tempAnimalImage.setBackgroundResource(resourceID);
            tempAnimalImage.setAlpha(0.5f);

            View separator = new View(this);

            separator.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, Constants.SEPARATOR_HEIGHT));

            animalViews.add(tempAnimalImage);
            animalLL.addView(tempAnimalImage);
            animalLL.addView(separator);
        }
    }

    public void onClick(View v)
    {
        String tag = (String)v.getTag();

        System.out.println(tag);

        int index = tag.charAt(0) - '0';

        System.out.println(index);

        tag = tag.substring(2); // cut off the SINGLE digit number and the space

        if (tag.equals("animal")) {
            selectedAnimal = true;
            for (int count = 0; count < animalNames.size(); ++count) {
                animalViews.get(count).setAlpha(0.5f);
            }
            animalViews.get(index).setAlpha(1.0f);

        } else if (tag.equals("color")) {
            selectedColor = true;
            for (int count = 0; count < colorNames.size(); ++count) {
                colorViews.get(count).setAlpha(0.5f);
            }
            colorViews.get(index).setAlpha(1.0f);
        }

        if (selectedAnimal && selectedColor) {
            createAccountButton.setAlpha(1.0f);
        }
    }

    /**
     * Launches the student registration UI
     */
    public void ClickedCreateAccount(View v)
    {
        Student newStudent = new Student(nameField.getText().toString(), chosenColor, chosenAnimal);

        newStudent.saveStudent(this.getApplicationContext()); // Saves student into local storage

        Intent newIntent = new Intent(this, MainMenu.class);
        newIntent.putExtra("uuid", newStudent.getUuid());
        startActivityForResult(newIntent, 1);

        /*
        // Need to save parent's details here
        // Add check to see if username has already been taken (locally)

        ArrayList<ParentTeacher> listOfPTs =  ParentTeacher.retrieveParentTeachers(this.getApplicationContext());

        if (username_field.getText().toString().equals("") || password_field.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please ensure that both fields are filled.", Toast.LENGTH_LONG).show();
        } else {
            Boolean validUsername = true;

            for (int index = 0; index < listOfPTs.size(); ++index) {
                if (listOfPTs.get(index).getName().equals(username_field.getText().toString())) {
                    validUsername = false;
                    break;
                }
            }

            if (validUsername) {

                ParentTeacher tempPT = new ParentTeacher(username_field.getText().toString(), password_field.getText().toString());

                tempPT.saveParentTeacher(this.getApplicationContext()); // Saves parent into local storage

                Intent newIntent = new Intent(this, ParentTeacherMainMenu.class);
                newIntent.putExtra("username", username_field.getText().toString());
                startActivityForResult(newIntent, 1);
            } else {
                Toast.makeText(getApplicationContext(), "That username is already taken; please choose a different username", Toast.LENGTH_LONG).show();
                username_field.setText("");
            }
        }
        */
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
     * Creates the buttons that serve as options for the word being spoken
     */
    public void loadIntentsAndViews()
    {
        color_scrollview = findViewById(R.id.ColorScrollView);
        animal_scrollview = findViewById(R.id.AnimalScrollView);

        colorLL = findViewById(R.id.ColorLL);
        animalLL = findViewById(R.id.AnimalLL);
        createAccountButton = findViewById(R.id.CreateAccount);
        createAccountButton.setAlpha(0.0f);
        nameField = findViewById(R.id.name);
    }
}
