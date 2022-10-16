package com.example.speech_recognition;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speech_recognition.utils.server.Server;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        setUpEntitiesEvent();
    }
    private void setUpEntitiesEvent(){
        btn=findViewById(R.id.btnHello);
        spinner = findViewById(R.id.selectLangSpinner);

        String[] items = new String[]{"Select Language","en", "vn"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedItem = spinner.getSelectedItem().toString();
                if(selectedItem.equals("Select Language")) selectedItem = "en";
                startActivity(new Intent(getApplicationContext(), SpeechRecognizing.class).putExtra("language",selectedItem));
                // .putExtra("key", value)....
            }
        });

    }

//    private void setRecognizerIntent(){
//        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        recognizerIntent.
//    }
}
// send message to server
/*
int state=0;
                Thread thr = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Server srv = new Server("10.0.2.2", 2207);
                        try {
                            srv.send("Chào server nhé",state);
                            System.out.println("Message sent");
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Failed!!!");

                        }
                    }
                });
                thr.start();
* */