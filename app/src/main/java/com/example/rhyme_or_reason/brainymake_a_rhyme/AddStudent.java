package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class AddStudent extends AppCompatActivity {

    String username = "";
    EditText uuid;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        loadIntentsAndViews();
    }

    /**
     * Returns to the ParentTeacherMainMenu (without having added a student
     */
    public void ClickedBackButton(View v) {
        onBackPressed();
    }

    /**
     * Attempts to add the student by their UUID (not their name); if the parent/teacher has already added
     * the student or the ID is incorrect, the student will not be added.
     */
    public void ClickedAddByUUID(View v)
    {
        ArrayList<ParentTeacher> listOfPTs = ParentTeacher.retrieveParentTeachers(this.getApplicationContext());

        ParentTeacher currPT = null;

        for (int index = 0; index < listOfPTs.size(); ++index) {
            if (listOfPTs.get(index).getName().equals(username)) {
                currPT = listOfPTs.get(index);
            }
        }

        ArrayList<Student> listOfStudents = Student.retrieveStudents(this.getApplicationContext());

        Student toAdd = null;

        for (int index = 0; index < listOfStudents.size(); ++index) {
            if (listOfStudents.get(index).getUuid().equals(uuid.getText().toString())) {
                toAdd = listOfStudents.get(index);
            }
        }

        if (toAdd != null && currPT != null) {
            boolean valid = true;
            for (int index = 0; index < currPT.getStudents().size(); ++index) {
                if (toAdd.getUuid().equals(currPT.getStudents().get(index).getUuid())) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                currPT.addStudent(toAdd);
                currPT.saveParentTeacher(this.getApplicationContext());
                onBackPressed(); // Only go back if successful
            } else {
                Toast.makeText(getApplicationContext(), "You've already added that student", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No student found with that identifier.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Takes the 'copied' UUID that is saved in shared preferences and places it in the Student ID field
     */
    public void clickedPaste(View v)
    {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());

        String currSavedUUID = appSharedPrefs.getString("UUID Clipboard", "");

        uuid.setText(currSavedUUID);
    }

    /**
     * Matches the scrollview and its linear layout with their respective code variables
     */
    public void loadIntentsAndViews()
    {
        username = getIntent().getExtras().get("username").toString();

        //name = findViewById(R.id.studentName);
        uuid = findViewById(R.id.UUID);
    }

}
