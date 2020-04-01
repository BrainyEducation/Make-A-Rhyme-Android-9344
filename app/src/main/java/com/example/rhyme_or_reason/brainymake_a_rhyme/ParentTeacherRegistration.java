package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ParentTeacherRegistration extends AppCompatActivity {

    EditText username_field;
    EditText password_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_teacher_registration);

        loadIntentsAndViews();
    }

    public void ClickedBackButton(View v) {
        onBackPressed();
    }

    /**
     * Launches the student registration UI
     */
    public void ClickedCreateAccount(View v)
    {
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
    }

    /**
     * Matches the scrollview and its linear layout with their respective code variables
     */
    public void loadIntentsAndViews()
    {
        username_field = findViewById(R.id.name);
        password_field = findViewById(R.id.password);
    }

    /**
     * When someone hits the back arrow from the parent/teacher main menu after making a new account,
     * shouldn't be taken back to this screen, so automatically pop backwards
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        onBackPressed();
    }
}
