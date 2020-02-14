package com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem;

import android.content.Intent;
import android.util.Log;


public class Emailer
{
    public Emailer() {
        Log.d("Emailer","emailer object created");
    }

    public Intent createEmailIntent() {
        Intent it = new Intent(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_EMAIL, new String[]{"jqdude@gmail.com"});
        it.putExtra(Intent.EXTRA_SUBJECT,"example subject");
        it.putExtra(Intent.EXTRA_TEXT,"extra text");
        it.setType("message/rfc822");
        return it;
    }
}
