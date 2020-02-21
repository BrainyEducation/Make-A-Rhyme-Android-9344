package com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem;

import android.content.Intent;
import android.util.Log;


public class EmailSystem
{
    private String[] emails;
    private String subjectLine;
    private String emailBody;

    public EmailSystem() {

    }

    public EmailSystem(String[] emails, String subjectLine, String emailBody) {
        this.emails = emails;
        this.subjectLine = subjectLine;
        this.emailBody = emailBody;
    }

    public Intent createEmailIntent() {
        Intent it = new Intent(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_EMAIL, emails);
        it.putExtra(Intent.EXTRA_SUBJECT,subjectLine);
        it.putExtra(Intent.EXTRA_TEXT,emailBody);
        it.setType("message/rfc822");
        return it;
    }
}
