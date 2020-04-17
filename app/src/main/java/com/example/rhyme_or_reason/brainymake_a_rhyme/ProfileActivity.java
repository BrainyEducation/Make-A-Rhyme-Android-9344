package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    String id = null;
    TextView idlabel = null;
    TextView namefield = null;
    SharedPreferences settings = null;
    String uuid = "";
    Student currStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        loadIntentsAndViews();

        getStudentFromUUID();

        namefield.setText(currStudent.getName());
        idlabel.setText(currStudent.getUuid());
    }

    public void getStudentFromUUID()
    {
        ArrayList<Student> allStudents = Student.retrieveStudents(this.getApplicationContext());

        for (int index = 0; index < allStudents.size(); ++index) {
            if (allStudents.get(index).getUuid().equals(uuid)) {
                currStudent = allStudents.get(index);
            }
        }
    }

    /**
     * Matches the scrollview and its linear layout with their respective code variables
     */
    public void loadIntentsAndViews()
    {
        uuid = getIntent().getExtras().get("uuid").toString();

        idlabel = findViewById(R.id.uuidField);
        namefield = findViewById(R.id.NameField);
    }

    /**
     * Handles back press click; takes user back to previous activity (word select screen)
     *
     * @param view Automatic parameter for user interaction
     */
    public void ClickedBackButton(View view) {
        onBackPressed();
    }

    public void ClickedCopy(View v)
    {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());

        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        prefsEditor.putString("UUID Clipboard", uuid);
        prefsEditor.commit();

        Toast.makeText(getApplicationContext(), "Copied! Paste this in the Parent Menu.", Toast.LENGTH_LONG).show();
    }
}
