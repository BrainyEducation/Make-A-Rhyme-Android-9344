package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem.EmailSystem;
import com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem.ImageSaver;
import android.widget.TextView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentProgress extends AppCompatActivity {

    GraphView graph;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
    Button emailButton;
    String uuid = "";
    Student currStudent;
    TextView highestNumConsec;
    TextView averageIncorrections;
    TextView numAttempts;
    TextView averageIncorrectionsNumber;
    TextView numAttemptsNumber;
    TextView highestNumConsecNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        uuid = getIntent().getExtras().get("uuid").toString();
        getStudentFromUUID();

        graph = (GraphView) findViewById(R.id.graph);

        graph.setBackgroundColor(getResources().getColor(android.R.color.white));
        ///////////////////////////////////////////////////
        averageIncorrections = (TextView) findViewById(R.id.AverageIncorrections);
        numAttempts = (TextView) findViewById(R.id.NumAttempts);
        highestNumConsec = (TextView) findViewById(R.id.highestConsec);
        averageIncorrectionsNumber = (TextView) findViewById(R.id.AverageIncorrectionsNumber);
        numAttemptsNumber = (TextView) findViewById(R.id.NumAttemptsNumber);
        highestNumConsecNumber = (TextView) findViewById(R.id.highestConsecutiveNumber);
        averageIncorrections.setText("Average Incorrect Selections Per Attempt: ");
        numAttempts.setText("Number of Attempts To Read: ");
        //highestNumConsec.setText("Highest Number of Consecutive Attempts: ");
        ////////////////////////////////////////////////////

        String w = getIntent().getExtras().get("word").toString();

        ArrayList<Integer> innerList = currStudent.getAttemptsMap().get(w);
        int total = 0;
        for (int att : innerList) {
            total += att;
        }
        double average = total / innerList.size();

        averageIncorrectionsNumber.setText(String.valueOf(average));
        numAttemptsNumber.setText(String.valueOf(innerList.size()));

        for (int i = 0; i < innerList.size(); i++) {
            series.appendData(new DataPoint(i + 1, innerList.get(i)), true, 50);
        }

        series.setDrawDataPoints(true);
        graph.addSeries(series);

        graph.getViewport().setMinX(1);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxX(5);
        graph.getViewport().setMaxY(10);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Incorrect Selections");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Attempts");
//            graph.getGridLabelRenderer().setNumHorizontalLabels(5);
//            graph.getGridLabelRenderer().setNumVerticalLabels(10);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);

        emailButton = (Button) findViewById(R.id.emailButtonAnalytics);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmailClick(v);
            }
        });
    }

    public void ClickedBackButton(View view) {
        try {
            ImageSaver.saveImageAndReturnPath(graph, this, getIntent().getExtras().get("Word").toString());
        } catch (NullPointerException e) {
            Log.d("StudentProgress", "No Word Object detected");
        }
        onBackPressed();
    }


    public void onEmailClick(View v) {

        uuid = getIntent().getExtras().get("uuid").toString();
        //forceStopAudio();

        Intent newIntent = new Intent(StudentProgress.this, EmailActivity.class);
        View parentView = findViewById(R.id.graph);
        String path = EmailSystem.saveViewAsPngAndReturnPath(parentView, this);


        //newIntent.putExtra("current_rhyme", currRhyme);
        //newIntent.putStringArrayListExtra("rhyme_words", wordList);
        //newIntent.putExtra("illustration", byteArray);
        newIntent.putExtra("imageUri", path);
        newIntent.putExtra("general_rhyme_text", "Here is the student's progress with this word");
        newIntent.putExtra("uuid", uuid);

        startActivity(newIntent);

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
}
