package com.example.speech_recognition.utils.SpeechRecognition;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;

public class SpeechRecognizerManager {
    private final static String TAG = "SpeechRecognizerManager";
    public SpeechRecognizer mSpeechRecognizer = null;
    public Intent mSpeechRecognizerIntent;
    public Context mContext;
    protected boolean mIsListening;
    public String language = "en"; // "default en"
    protected long timeout = 2000l; // 2000 ms
    private Intent recognizerIntent;
    private String LOG_TAG="Voice_Recognition_Activity";


    public SpeechRecognizerManager(Context context, RecognitionListener recognitionListener, String _language)
    {
        mContext = context;
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        mSpeechRecognizer.setRecognitionListener(recognitionListener);

        language = _language;
        // Create new intent
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, language);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true); // For streaming result
//        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, timeout);
    }

    public void startListening()
    {
        if (!mIsListening) {
            mIsListening = true;
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        }
    }

    public void stop() {
        if (mIsListening && mSpeechRecognizer != null) {
            mSpeechRecognizer.stopListening();
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
            mSpeechRecognizer = null;
        }

        mIsListening = false;
    }

    public void destroy()
    {
        mIsListening = false;
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.stopListening();
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
            mSpeechRecognizer = null;
        }
    }

    public boolean ismIsListening() {
        return mIsListening;
    }

}
