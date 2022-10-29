package com.example.speech_recognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speech_recognition.utils.server.Server;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setUpEntitiesEvent();
    }
    private void setUpEntitiesEvent(){
        btn=findViewById(R.id.btnHello);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SpeechRecognizing.class));
                // .putExtra("key", value)....;
                System.out.println("change");
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
                            srv.send("Chào server nhé =)))",state);
                            System.out.println("Message sent");
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Failed!!!");

                        }
                    }
                });
                thr.start();
* */