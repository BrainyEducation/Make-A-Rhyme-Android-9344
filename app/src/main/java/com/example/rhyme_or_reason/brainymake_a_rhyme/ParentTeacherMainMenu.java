package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem.EmailSystem;

import java.util.ArrayList;

public class ParentTeacherMainMenu extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    String username;
    ParentTeacher currPT = null;
    Button emailButton;

    ArrayList<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_teacher_main_menu);

        loadIntentsAndViews();

        loadStudents();

    }

    public void ClickedAddStudent(View v) {
        Intent newIntent = new Intent(this, AddStudent.class);
        newIntent.putExtra("username", username);
        startActivityForResult(newIntent, 1);
    }

    public void ClickedBackButton(View v) {
        onBackPressed();
    }

    public void ClickedClassAnalytics(View v) {
        // Link to Eric's page with averaged stats
        Intent newIntent = new Intent(this, ClassProgress.class);
        newIntent.putExtra("name", currPT.getName());
        startActivityForResult(newIntent, 1);
    }

    public void loadIntentsAndViews() {
        username = (String) getIntent().getStringExtra("username");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        loadStudents();
    }

    public void loadStudents()
    {
        ArrayList<ParentTeacher> listOfPTs = ParentTeacher.retrieveParentTeachers(this.getApplicationContext());

        for (int index = 0; index < listOfPTs.size(); ++index) {
            if (listOfPTs.get(index).getName().equals(username)) {
                currPT = listOfPTs.get(index);
            }
        }


        final ArrayList<Student> students = currPT.getStudents();
        studentList = students;

        ArrayList<String> studentNames = new ArrayList<>();

        for (int index = 0; index < students.size(); ++index) {
            studentNames.add(students.get(index).getName());
        }

        arrayAdapter = new ArrayAdapter(this, R.layout.progress_individual_words, studentNames);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Use the position in the list to reference the current Student object
                Student currStudent = students.get(i);

                Intent myIntent = new Intent(view.getContext(), StudentProgressMenu.class);
                myIntent.putExtra("name", currStudent.getName());
                myIntent.putExtra("uuid", currStudent.getUuid());
                startActivityForResult(myIntent, 0);

            }
        });
    }
}
