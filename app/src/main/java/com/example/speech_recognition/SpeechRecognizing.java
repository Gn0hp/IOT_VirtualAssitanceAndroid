package com.example.speech_recognition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speech_recognition.utils.SpeechRecognition.SpeechRecognizerManager;
import com.example.speech_recognition.utils.server.Server;

import java.util.ArrayList;


public class SpeechRecognizing extends AppCompatActivity {
    private TextView returnedText, returnedErr;

    private Button startListenerBtn;

    private Intent mSpeechRecognizerIntent;
    private String language;
    public Server server;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_reconizing);


        server = new Server("192.168.1.79", 2207);

        returnedText = findViewById(R.id.textViewReturnText);
        returnedErr = findViewById(R.id.textViewReturnError);
        startListenerBtn = findViewById(R.id.startListenerBtn);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        language = i.getStringExtra("language");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toast.makeText(this, "Current Language is: "+language, Toast.LENGTH_SHORT).show();

//        resetSpeechRecognizer(speechManager.mSpeechRecognizer);

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1 );
        }
        String availableSpeech;
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            Toast.makeText(this, "Available Speech" , Toast.LENGTH_SHORT).show();
        }
//        startListenerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //startListening();
//            }
//        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Permission Denied! Please grant permission to continue.", Toast.LENGTH_SHORT).show();
        }
    }


    public void startListening(View view){
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");
//        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language);
//        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, language);
        startActivityForResult(mSpeechRecognizerIntent,100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK){
            assert data != null;
            returnedText.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));

            Thread thr = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("state: "+ 0);
                        int state = server.send((String) returnedText.getText(),0);
                        System.out.println("state: "+ state);
                        if(state==1) System.out.println("Message sent");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Failed!!!");

                    }
                }
            });
            thr.start();
        }
    }



}