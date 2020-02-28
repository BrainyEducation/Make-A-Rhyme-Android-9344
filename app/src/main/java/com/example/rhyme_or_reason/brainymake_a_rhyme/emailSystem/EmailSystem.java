package com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem;

import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailSystem
{
    private String[] emails;
    private String subjectLine;
    private String emailBody;
    public static int maxEmailDisplayLength = 15;
    //regex comes from https://howtodoinjava.com/regex/java-regex-validate-email-address/
    private String emailValidationRegex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private Pattern emailRegexPattern;
    private static int maxEmails = 20;

    public EmailSystem() {
        emailRegexPattern = Pattern.compile(emailValidationRegex);
    }

    public void setEmailVariables(String[] emails, String subjectLine, String emailBody) {
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

    public boolean isValidEmail(String email) {
        return emailRegexPattern.matcher(email).matches();
    }

    public boolean tooManyEmails(ArrayList<String> emails) {
        return emails.size() > maxEmails;
    }
}
