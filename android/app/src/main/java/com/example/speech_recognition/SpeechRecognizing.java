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
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.speech_recognition.utils.SpeechRecognition.SpeechRecognizerManager;
import com.example.speech_recognition.utils.server.Server;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;


public class SpeechRecognizing extends AppCompatActivity {
    private TextView returnedText, returnedErr;

    private Button startListenerBtn;
    private Button btnStopMusic;
    private Intent mSpeechRecognizerIntent;
    private String language;
    public Server server;
    private ArrayList<File> arr;
    private String url = "http://192.168.1.98:80/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_reconizing);
        server = new Server("192.168.1.79", 2207);

        returnedText = findViewById(R.id.textViewReturnText);
        returnedErr = findViewById(R.id.textViewReturnError);
        startListenerBtn = findViewById(R.id.startListenerBtn);
        btnStopMusic= findViewById(R.id.btnStopMusic);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        arr = (ArrayList) bundle.getParcelableArrayList("songs");
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

    // Handle command here
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK){
            assert data != null;
            String res =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            returnedText.setText(res);
            handleCommand(res);

        }
    }
    private void handleCommand(String res) {
        res = res.toLowerCase();
       if(res.contains("open music")){
           openMusic(res.substring(11));
       }
    }
    private void openMusic(String res){
        String[] stringArr = res.split("\\s");
        int endSongName=-1;
        String song="", singer="";
        for(int i=stringArr.length-1;i>0;--i) {
            if(stringArr[i].equals("by")) {
                endSongName=i;
                break;
            }
        }
        for (int i = 0, j = endSongName + 1; i < endSongName && j < stringArr.length; ++i, ++j) {
            song += stringArr[i].substring(0, 1).toUpperCase() + stringArr[i].substring(1).toLowerCase();
            singer += stringArr[j].substring(0, 1).toUpperCase() + stringArr[j].substring(1).toLowerCase();
        }
        int index=0;
        for(File i: arr){
            if(i.toString().contains(song)){
                song = i.toString();
                index = arr.indexOf(i);
                break;
            }
        }
        System.out.println("song path: "+  song);
        System.out.println("author: "+ singer);
        System.out.println(song+"-"+singer);
        Uri uri = Uri.parse(song);

        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();

        int finalIndex = index;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Uri uri = Uri.parse(arr.get(finalIndex).toString());
                System.out.println("Played!!!");
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
            }
        });
        btnStopMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
            }
        });
    }

    // xử lý tách câu lệnh cho

    private void httpCall() {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SpeechRecognizing.this, "Success!", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SpeechRecognizing.this, "Sent!", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
}