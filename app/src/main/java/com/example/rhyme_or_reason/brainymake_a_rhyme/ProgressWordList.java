package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProgressWordList extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    String uuid = "";
    Student currStudent;
    String studentName;
    Button emailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_wordlist);

        uuid = getIntent().getExtras().get("uuid").toString();
        Log.d("message", uuid);
        getStudentFromUUID();
        //currStudent.loadMap(this.getApplicationContext(), uuid);
        Log.d("student", currStudent.getName());
        Log.d("map", currStudent.getAttemptsMap().keySet().toString());
        ArrayList<String> attemptedwords = new ArrayList<String>(currStudent.getAttemptsMap().keySet());
        arrayAdapter = new ArrayAdapter(this, R.layout.progress_individual_words, attemptedwords);
        //SavePref(attemptedwords);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(arrayAdapter);
        //LoadFromPref();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String wordFromList = adapterView.getItemAtPosition(i).toString();
                Intent myIntent = new Intent(view.getContext(), StudentProgress.class);

                myIntent.putExtra("word", wordFromList);
                myIntent.putExtra("uuid", uuid);
                startActivityForResult(myIntent, 0);
            }
        });

        emailButton = (Button) findViewById(R.id.emailButtonSingleStudent);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmailClick(v);
            }
        });

    }

    public void ClickedBackButton(View view) {
        onBackPressed();
    }

    public void getStudentFromUUID() {
        ArrayList<Student> allStudents = Student.retrieveStudents(this.getApplicationContext());

        for (int index = 0; index < allStudents.size(); ++index) {
            if (allStudents.get(index).getUuid().equals(uuid)) {
                currStudent = allStudents.get(index); // HERE we assign the student so we can save on exit.
                break;
            }
        }
    }

    public void onEmailClick(View v) {

        //forceStopAudio();


        Intent newIntent = new Intent(ProgressWordList.this,EmailActivity.class);
        //View parentView = findViewById(R.id.totalIllustration);

        //String path = EmailSystem.saveViewAsPngAndReturnPath(parentView, this);


        //newIntent.putStringArrayListExtra("rhyme_words", wordList);

        newIntent.putExtra("imageUri", "");

        StringBuilder sb = new StringBuilder();

        sb.append("Your child, \n");
        sb.append(currStudent.getName());
        sb.append("\n\n");
        sb.append("has learned the following words:\n");


        ArrayList<Word> unlockedWords = currStudent.getUnlockedWords();
        for (int j = 0; j < unlockedWords.size(); j++) {
            Word word = unlockedWords.get(j);
            sb.append(word.getText());
            sb.append("\n");
        }



        newIntent.putExtra("general_rhyme_text", sb.toString());



        newIntent.putExtra("uuid", uuid);

        startActivity(newIntent);
    }


}
