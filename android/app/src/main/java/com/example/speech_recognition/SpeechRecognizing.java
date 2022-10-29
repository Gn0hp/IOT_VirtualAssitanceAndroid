package com.example.speech_recognition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speech_recognition.handleResult.onResultsReady;
import com.example.speech_recognition.utils.SpeechRecognition.SpeechRecognitionListener;
import com.example.speech_recognition.utils.SpeechRecognition.SpeechRecognizerManager;

public class SpeechRecognizing extends AppCompatActivity {
    private SpeechRecognizerManager speechManager = null;
    private TextView returnedText, returnedErr;
    private ProgressBar progressBar;
    private String LOG_TAG="Voice_Recognition_Activity";
    private Button startListenerBtn;
    private onResultsReady mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_reconizing);
        returnedText = findViewById(R.id.textViewReturnText);
        returnedErr = findViewById(R.id.textViewReturnError);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        startListenerBtn = findViewById(R.id.startListenerBtn);

        speechManager = new SpeechRecognizerManager(getApplicationContext(),mListener);

        resetSpeechRecognizer(speechManager.mSpeechRecognizer);
        setUpEntitiesEvent();
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1 );
        }

    }
    public void resetSpeechRecognizer(SpeechRecognizer speech){
        if(speech != null){
            speech.destroy();
        }
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.i(LOG_TAG, "isRecognitionAvailable: "+ SpeechRecognizer.isOnDeviceRecognitionAvailable(this));
        }
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            speech.setRecognitionListener(new SpeechRecognitionListener());
        }else finish();
    }
    private void setUpEntitiesEvent(){
        startListenerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startListenerBtn.getText().equals("Stop Listenning")){
                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar.setIndeterminate(false);
                    speechManager.stop();
                    startListenerBtn.setText("Start Listenning");
                }
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);
                speechManager.startListening();
                startListenerBtn.setText("Stop Listenning");
            }
        });
    }

}