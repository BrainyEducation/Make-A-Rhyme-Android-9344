package com.example.rhyme_or_reason.brainymake_a_rhyme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.Buffer;

public class ProfileActivity extends AppCompatActivity {
    String id = null;
    TextView idlabel = null;
    EditText namefield = null;
    SharedPreferences settings = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        idlabel = findViewById(R.id.IdLabel);
        namefield = findViewById(R.id.NameField);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        id = settings.getString("uuid", null);
        namefield.setText(settings.getString("name", ""));
        if(id != null)
        {
            idlabel.setText("UUID: "+id);
        }

    }
    /**
     * Handles back press click; takes user back to previous activity (word select screen)
     *
     * @param view Automatic parameter for user interaction
     */
    public void ClickedBackButton(View view) {
        onBackPressed();
    }
    private interface  Action {
        void act(String s);
    }
    public void readURL(final String path, final Action a)
    {
        final Handler handler = new Handler();
        new Thread(){
            public void run() {
                StringBuilder sb = new StringBuilder();
                try {
                    URL url = new URL("http://10.0.2.2:8000/"+path.replaceAll("\\s","")); //Emulator -> local url
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String inputLine;
                    while((inputLine = in.readLine()) != null)
                        sb.append(inputLine+"\n");
                    in.close();
                    final String res = sb.toString();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            a.act(res);
                        }
                    });
                } catch(IOException ioe)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            a.act("Request failed");
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * Handles back press click; takes user back to previous activity (word select screen)
     *
     * @param view Automatic parameter for user interaction
     */
    public void ClickedSubmitName(View view) {
        if(namefield.getText().length() > 0) {
            final SharedPreferences.Editor editor = settings.edit();
            if (id == null) {
                readURL("", new Action() {
                    @Override
                    public void act(String s) {
                        if (s.contains("Request failed")) {
                            idlabel.setText("Error connecting to server. Please try again later.");
                            return;
                        }
                        id = s.replaceAll("\n","");
                        editor.putString("uuid", id);
                        idlabel.setText("UUID: "+id);
                        readURL(id + "/name=" + namefield.getText().toString(), new Action() {
                            @Override
                            public void act(String s) {
                                if(s.contains("Request failed"))
                                    idlabel.setText("Error connecting to server. Please try again later.");
                                else {
                                    idlabel.setText("UUID: "+id);
                                    editor.putString("name", namefield.getText().toString());
                                    editor.commit();
                                }
                            }
                        });
                    }
                });

            }
            else
            {
                readURL(id + "/name=" + namefield.getText().toString(), new Action() {
                    @Override
                    public void act(String s) {
                        if(s.contains("Request failed"))
                            idlabel.setText("Error connecting to server. Please try again later.");
                        else {
                            idlabel.setText("UUID: "+id);
                            editor.putString("name", namefield.getText().toString());
                            editor.commit();
                        }
                    }
                });
            }
        }
    }


}
