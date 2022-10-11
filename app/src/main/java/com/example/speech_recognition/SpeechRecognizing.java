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
import android.widget.ProgressBar;
import android.widget.TextView;

public class SpeechRecognizing extends AppCompatActivity {
    private SpeechRecognizer speechRecognizer = null;
    private TextView returnedText, returnedErr;
    private ProgressBar progressBar;
    private Intent recognizerIntent;
    private String language = "vi"; //"en"
    private String LOG_TAG="Voice_Recognition_Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_reconizing);
        returnedText = findViewById(R.id.textViewReturnText);
        returnedErr = findViewById(R.id.textViewReturnError);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

//        resetSpeechRecognizer(speechRecognizer);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

//        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
//        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1 );
//        }

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
            speechRecognizer.setRecognitionListener((RecognitionListener) this);
        }else finish();
    }
    private void setRecognizerIntent() {
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,3 );

        // Create new intent
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, language);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true); // For streaming result
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 20000);
    }
}