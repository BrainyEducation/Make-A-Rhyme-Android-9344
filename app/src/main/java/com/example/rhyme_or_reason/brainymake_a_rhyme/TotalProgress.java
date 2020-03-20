package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TotalProgress extends AppCompatActivity {

    TextView successfulFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //which categories had the highest score
        //get analytics that look at all the words together

    }

    public void ClickedBackButton(View view) {
        onBackPressed();
    }
}
