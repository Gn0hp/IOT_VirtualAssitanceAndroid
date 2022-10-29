package com.example.speech_recognition.utils.SpeechRecognition;

import android.content.Context;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import com.example.speech_recognition.handleResult.onResultsReady;

import java.util.ArrayList;

public class SpeechRecognitionListener implements RecognitionListener
{
    private onResultsReady mListener;
    private Context mContext;
    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }
    @Override
    public void onBeginningOfSpeech() {}

    @Override
    public void onBufferReceived(byte[] buffer) { }

    @Override
    public void onEndOfSpeech() {}

    @Override
    public synchronized void onError(int error) {
        if (error == SpeechRecognizer.ERROR_NETWORK) {
            ArrayList<String> errorList = new ArrayList<String>(1);
            errorList.add("STOPPED LISTENING");
            if (mListener != null) {
                mListener.onResults(errorList);
                Toast.makeText(mContext, "NETWORK ERROR", Toast.LENGTH_SHORT).show();
            }
        }

        if (error == SpeechRecognizer.ERROR_AUDIO){
            Toast.makeText(mContext, "AUDIO ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        if (partialResults != null && mListener != null) {
            ArrayList<String> texts = partialResults.getStringArrayList("android.speech.extra.UNSTABLE_TEXT");
            Toast.makeText(mContext, texts.get(0), Toast.LENGTH_SHORT).show();
            System.out.println(texts);
            mListener.onStreamingResult(texts);
        }
    }

    @Override
    public void onResults(Bundle results) {
        if (results != null && mListener != null) {
            ArrayList<String> arr = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            Toast.makeText(mContext, arr.get(0), Toast.LENGTH_SHORT).show();
            System.out.println(arr);
            mListener.onResults(arr);
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {}



    @Override
    public void onRmsChanged(float rmsdB) {}

}