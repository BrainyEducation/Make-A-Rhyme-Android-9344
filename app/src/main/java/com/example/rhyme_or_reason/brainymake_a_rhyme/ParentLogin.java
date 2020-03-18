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

public class ParentLogin extends AppCompatActivity {
    EditText passwordfield = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        passwordfield = (EditText) findViewById(R.id.txtPassword);
    }
    /**
     * Launches the rhyme UI
     */
    public void ClickedLogin(View v)
    {
        if(passwordfield.getText().toString().equals("Password")) {
            Intent newIntent = new Intent(this, ParentTeacherMainMenu.class);
            startActivityForResult(newIntent, 1);
        }
        else {
            Toast.makeText(getApplicationContext(), "Incorrect password, try again", Toast.LENGTH_LONG).show();
            passwordfield.setText("");
        }
    }
    public void ClickedBackButton(View v) {
        onBackPressed();
    }

    /**
     * Responsible for getting the result of the quiz. When RESULT_OK, then the quiz was passed,
     * so the word will be unlocked
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        onBackPressed();
    }}
