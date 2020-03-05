package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class StudentProgress extends AppCompatActivity{

    GraphView graph;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        graph = (GraphView) findViewById(R.id.graph);
        for (Integer k : Quiz.attemptsMap.keySet()) {
            series.appendData(new DataPoint(k, Quiz.attemptsMap.get(k)), true, 50);
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
}
