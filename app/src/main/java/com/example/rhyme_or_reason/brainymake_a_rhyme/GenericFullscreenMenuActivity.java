package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem.EmailSystem;

public class GenericFullscreenMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Need some way of storing and retrieving an email
    public void sendEmail() {
        EmailSystem emailSystem = new EmailSystem();
    }

    public void configureEmailSetting() {
        DialogFragment configureEmailFragment = new DialogFragment();
        
    }




}
