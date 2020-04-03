package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentProgress extends AppCompatActivity {

    GraphView graph;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
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

        ArrayList<int[]> innerMap = (ArrayList) currStudent.getAttemptsMap().get(w);
        int total = 0;
        for (int[] att : innerMap) {
            total += att[1];
        }
        double average = total / innerMap.size();
        int[] lastAttempt = innerMap.get(innerMap.size() - 1);
        averageIncorrectionsNumber.setText(String.valueOf(average));
        numAttemptsNumber.setText(String.valueOf(lastAttempt[0]));

        for (int[] eachAttempt : innerMap) {
            series.appendData(new DataPoint(eachAttempt[0], eachAttempt[1]), true, 50);
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
}
