package com.example.rhyme_or_reason.brainymake_a_rhyme.emailSystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailSystem
{
    private String[] emails;
    private String subjectLine;
    private String emailBody;
    public static int maxEmailDisplayLength = 28;
    //regex comes from https://howtodoinjava.com/regex/java-regex-validate-email-address/
    private String emailValidationRegex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private Pattern emailRegexPattern;
    private static int maxEmails = 20;
    private String emailShortener = "... ";
    private Uri attachmentURI;


    public EmailSystem() {
        emailRegexPattern = Pattern.compile(emailValidationRegex);
    }

    public void setEmailVariables(String[] emails, String subjectLine, String emailBody, String path) {
        this.emails = emails;
        this.subjectLine = subjectLine;
        this.emailBody = emailBody;
        Log.d("ATTACHMULTIPLEORIGINAL", path);
        this.attachmentURI = reformatPathStringToURI(path);
    }

    private Uri reformatPathStringToURI(String s) {
        String returnString = s.substring(1);
        Uri imageURI = new Uri.Builder().appendPath(returnString).build();
        return imageURI;
    }

    public Intent createEmailIntent(Context context) {
        Intent it = new Intent(Intent.ACTION_SEND_MULTIPLE);
        it.putExtra(Intent.EXTRA_EMAIL, emails);
        it.putExtra(Intent.EXTRA_SUBJECT,subjectLine);
        it.putExtra(Intent.EXTRA_TEXT,emailBody);
        it.setType("image/png");



        String path = attachmentURI.getPath();
       // Log.d("examplepath",path);

        //File f = new File(attachmentURI.getPath());
        //Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider",f);

        //it.putExtra(Intent.EXTRA_STREAM, contentUri);

        //ArrayList<Parcelable>  uris = new ArrayList<Parcelable>();
        //uris.add(contentUri);
        //Log.d("ATTACHMULTIPLE", uris.get(0).getPath());
        //it.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        //it.putExtra(Intent.EXTRA_STREAM, uris.get(0));

       // it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        //Log.d("attachmentURI", attachmentURI.getPath());


        HashMap<String, String> filenameToPathMap = ImageSaver.getFilenameToPathMap();
        ArrayList<Uri> uris = new ArrayList<>();
        for (String s : filenameToPathMap.keySet()) {
            Log.d("ATTACHMULTIPLE",filenameToPathMap.get(s));
            Log.d("ATTACHMULTIPLECOMPARE",attachmentURI.getPath());
            File f = new File(filenameToPathMap.get(s));
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider",f);
            uris.add(contentUri);
        }

        it.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);


        it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        return it;
    }


    public boolean isValidEmail(String email) {
        return emailRegexPattern.matcher(email).matches();
    }

    public boolean tooManyEmails(ArrayList<String> emails) {
        return emails.size() > maxEmails;
    }

    public String shortenedEmailText(String email) {
        if (email.length() >= maxEmailDisplayLength) {
            int atSymbolIndex = email.indexOf("@");
            String domainName = email.substring(atSymbolIndex);
            int usedUpCharacters = domainName.length();
            int remainingCharacters = maxEmailDisplayLength - usedUpCharacters;
            String emailPrefix = email.substring(0, remainingCharacters - emailShortener.length());
            StringBuilder sb = new StringBuilder();
            sb.append(emailPrefix);
            sb.append(emailShortener);
            sb.append(domainName);
            return sb.toString();
        }
        return email;
    }

    /**
     * Takes a view, saves it as a png file, and returns a string representing the path to
     * the file
     */
    public static String saveViewAsPngAndReturnPath(View parentView, Context context) {
        /*
        Bitmap bm = Bitmap.createBitmap(parentView.getWidth(), parentView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        parentView.draw(canvas);

        String path = "";
        try {
            File imagePath = new File(context.getFilesDir(),"images");
            imagePath.mkdir();
            File imageFile = new File(imagePath.getPath(), "brainy_image.png");
            path = imageFile.getPath();
            FileOutputStream out = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            Log.d("notfound", e.toString());
        } catch (IOException e) {
            Log.d("ioFileOutput",e.toString());
        }
        return path;*/
        return ImageSaver.saveImageAndReturnPath(parentView, context, "brainy_image");
    }
}
