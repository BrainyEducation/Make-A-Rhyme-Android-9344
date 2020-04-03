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

        emailButton = (Button) findViewById(R.id.emailButtonClass);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmailClick(v);
            }
        });
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
    }

    public void loadIntentsAndViews() {
        username = (String) getIntent().getStringExtra("username");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        loadStudents();
        /*
        System.out.println("Hit activity result.");
        existingRhymesLL.removeAllViews();
        loadExistingRhymes(true);
        */
    }

    public void loadStudents()
    {
        ArrayList<ParentTeacher> listOfPTs = ParentTeacher.retrieveParentTeachers(this.getApplicationContext());

        for (int index = 0; index < listOfPTs.size(); ++index) {
            if (listOfPTs.get(index).getName().equals(username)) {
                currPT = listOfPTs.get(index);
            }
        }

        ArrayList<Student> students = currPT.getStudents();
        studentList = students;

        ArrayList<String> studentNames = new ArrayList<>();

        for (int index = 0; index < students.size(); ++index) {
            studentNames.add(students.get(index).getName());
        }

        arrayAdapter = new ArrayAdapter(this, R.layout.progress_individual_words, studentNames);
        //SavePref(attemptedwords);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(arrayAdapter);
        //LoadFromPref();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String studentName = adapterView.getItemAtPosition(i).toString();
                // Link to Eric's analytics screen for that student
                /*
                Intent myIntent = new Intent(view.getContext(), StudentProgress.class);
                myIntent.putExtra("Word", wordFromList);
                startActivityForResult(myIntent, 0);
                */
            }
        });


    }


    //TODO move the email button to individual students once Eric integrates his changes
    public void onEmailClick(View v) {

        //forceStopAudio();


        Intent newIntent = new Intent(ParentTeacherMainMenu.this,EmailActivity.class);
        //View parentView = findViewById(R.id.totalIllustration);

        //String path = EmailSystem.saveViewAsPngAndReturnPath(parentView, this);


        //newIntent.putStringArrayListExtra("rhyme_words", wordList);

        newIntent.putExtra("imageUri", "");

        StringBuilder sb = new StringBuilder();

        if (studentList == null || studentList.size() == 0) {
            Snackbar.make(v, "Please add at least one child", Snackbar.LENGTH_LONG).show();
            return;
        }

        for (int i = 0; i < studentList.size(); i++) {
            Student student = studentList.get(i);
            sb.append("Student: ");
            sb.append(student.getName());
            sb.append(" has learned the following words:\n");
            ArrayList<Word> unlockedWords = student.getUnlockedWords();

            //TODO: Figure out why this list is always empty
            for (int j = 0; j < unlockedWords.size(); j++) {
                Log.d("ParentTeacherMainMenu", unlockedWords.get(j).getText());
                Word word = unlockedWords.get(j);
                sb.append(word.getText());
                sb.append("\n");
            }
            sb.append("\n");
        }


        newIntent.putExtra("general_rhyme_text", sb.toString());


        //TODO: replace this with the right UUID call
        newIntent.putExtra("uuid", "dummy-uuid");

        startActivity(newIntent);
    }
}
