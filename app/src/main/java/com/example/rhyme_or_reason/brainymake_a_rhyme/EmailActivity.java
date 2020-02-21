package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem.EmailSystem;

public class EmailActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
    }


    public void sendEmail(View v) {
        String[] a = new String[1];
        a[0] = "jqdude@gmail.com";
        String subject = "hi";
        String body = "this is the body text";
        EmailSystem emailSystem = new EmailSystem(a, subject, body);
        Intent in = emailSystem.createEmailIntent();
        try {
            startActivity(Intent.createChooser(in, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
           Log.d("Email error", "No email clients installed");
        }
    }

    public void configureEmailSetting() {
        DialogFragment configureEmailFragment = new DialogFragment();
    }

    public void onClick(View v) {

    }


}
