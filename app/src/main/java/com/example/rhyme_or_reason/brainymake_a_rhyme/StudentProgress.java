package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem.ImageSaver;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;
import java.util.ArrayList;
import java.util.HashMap;

public class StudentProgress extends AppCompatActivity {

    GraphView graph;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
    TextView highestNumConsec;
    TextView successfulFirst;
    TextView numAttempts;
    TextView successfulFirstNumber;
    TextView numAttemptsNumber;
    TextView highestNumConsecNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        graph = (GraphView) findViewById(R.id.graph);
        graph.setBackgroundColor(getResources().getColor(android.R.color.white));

        ///////////////////////////////////////////////////
        successfulFirst = (TextView) findViewById(R.id.successfulFirst);
        numAttempts = (TextView) findViewById(R.id.NumAttempts);
        highestNumConsec = (TextView) findViewById(R.id.highestConsec);
        successfulFirstNumber = (TextView) findViewById(R.id.successfulFirstNumber);
        numAttemptsNumber = (TextView) findViewById(R.id.NumAttemptsNumber);
        highestNumConsecNumber = (TextView) findViewById(R.id.highestConsecutiveNumber);
        successfulFirst.setText("Number of Successful First Attempts: ");
        numAttempts.setText("Number of Attempts To Read: ");
        highestNumConsec.setText("Highest Number of Consecutive Attempts: ");
        ////////////////////////////////////////////////////

        final String w = getIntent().getExtras().get("Word").toString();
        successfulFirstNumber.setText(String.valueOf(MainMenu.successFirstMap.get(w)));
        highestNumConsecNumber.setText(String.valueOf(MainMenu.highestConsecMap.get(w)[0]));
        ArrayList<int[]> innerMap = MainMenu.attemptsMap.get(w);
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
        ImageSaver.saveImageAndReturnPath(graph, this, getIntent().getExtras().get("Word").toString());
        onBackPressed();
    }
}
