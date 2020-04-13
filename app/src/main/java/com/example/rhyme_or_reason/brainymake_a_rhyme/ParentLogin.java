package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ParentLogin extends AppCompatActivity {

    EditText username_field;
    EditText password_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);

        username_field = (EditText) findViewById(R.id.name);
        password_field = (EditText) findViewById(R.id.txtPassword);
    }

    /**
     * Launches the Parent Main Menu UI
     */
    public void ClickedLogin(View v)
    {
        // Check if user exists with that username and password

        ArrayList<ParentTeacher> listOfPTs = ParentTeacher.retrieveParentTeachers(this.getApplicationContext());

        Boolean successful = false;

        for (int index = 0; index < listOfPTs.size(); ++index) {
            if (listOfPTs.get(index).getName().equals(username_field.getText().toString())) {
                if (listOfPTs.get(index).getPassword().equals(password_field.getText().toString())) {
                    successful = true;
                    Intent newIntent = new Intent(this, ParentTeacherMainMenu.class);
                    newIntent.putExtra("username", username_field.getText().toString());

                    startActivityForResult(newIntent, 1);
                }
            }
        }

        if (!successful) {
            Toast.makeText(getApplicationContext(), "Incorrect login, try again", Toast.LENGTH_LONG).show();
            password_field.setText("");
            username_field.setText("");
        }
    }

    /**
     * Returns users to the Application Welcome screen.
     */
    public void ClickedBackButton(View v) {
        onBackPressed();
    }

    /**
     * When the parent/teacher hits the back button on the Parent Teacher Main Menu, this will
     * automatically send the user back to the Application Welcome screen.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        onBackPressed();
    }}
