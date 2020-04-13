package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem.EmailSystem;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class ClassProgress extends AppCompatActivity {

    String ptName;
    ParentTeacher currPT;
    BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
    Button emailButton;


    /**
     * TODO: Jonathan comment explanation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_progress);
        ptName = getIntent().getExtras().get("name").toString();

        ArrayList<ParentTeacher> listOfPTs = ParentTeacher.retrieveParentTeachers(this.getApplicationContext());

        for (int index = 0; index < listOfPTs.size(); ++index) {
            if (listOfPTs.get(index).getName().equals(ptName)) {
                currPT = listOfPTs.get(index);
            }
        }

        ArrayList<Student> students = currPT.getStudents();

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setBackgroundColor(getResources().getColor(android.R.color.white));
        String[] studentNames = new String[students.size() + 2]; // Add one for buffer

        series.appendData(new DataPoint(1, 0), true, 50); // Fake point at 0 for buffer
        studentNames[0] = "";

        for (int i = 1; i <= students.size(); i++) {
            series.appendData(new DataPoint(i + 1, students.get(i - 1).getAttemptsMap().size()), true, 50);
            studentNames[i] = students.get(i -  1).getName();
        }

        series.appendData(new DataPoint(students.size() + 2, 0), true, 50); // Fake point at 0 for buffer
        studentNames[students.size() + 1] = "";

        graph.addSeries(series);
        if (studentNames.length > 1) {
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(studentNames);
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        }

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });


        series.setSpacing(50);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMinY(0);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Words Attempted");
        //graph.getGridLabelRenderer().setHorizontalAxisTitle("Child");
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);

        //series.setDrawValuesOnTop(true);
        //series.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);

        emailButton = (Button) findViewById(R.id.emailButtonClassProgress);
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

    /**
     * TODO: Jonathan comment explanation
     */
    public void onEmailClick(View v) {

        Intent newIntent = new Intent(ClassProgress.this,EmailActivity.class);

        View parentView = findViewById(R.id.graph);
        String path = EmailSystem.saveViewAsPngAndReturnPath(parentView, this);
        newIntent.putExtra("imageUri", "");

        StringBuilder sb = new StringBuilder();

        ArrayList<Student> studentList = currPT.getStudents();

        if (studentList == null || studentList.size() == 0) {
            Snackbar.make(v, "Please add at least one student", Snackbar.LENGTH_LONG).show();
            return;
        }

        sb.append("This email contains the students in your class and the words they have learned.\n\n");
        sb.append("In addition, attached to this email, is a graph containing the overall progress of your class\n\n");

        for (int i = 0; i < studentList.size(); i++) {
            Student student = studentList.get(i);
            sb.append("Student: ");
            sb.append(student.getName());
            sb.append("\nhas learned the following words:\n");
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

        newIntent.putExtra("imageUri", path);
        newIntent.putExtra("general_rhyme_text", sb.toString());
        newIntent.putExtra("subject_line", "Brainy Make-A-Rhyme: Your Class Update Is Here!");


        //TODO: replace this with the right UUID call
        newIntent.putExtra("uuid", "dummy-uuid");

        startActivity(newIntent);
    }
}
