package com.example.speech_recognition;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;

import android.os.Bundle;
import android.os.Environment;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private Button btn;
    private Spinner spinner;
    private ArrayList<File> songs;
    private TextInputEditText serverAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setUpEntitiesEvent();

        runtimePermission();
        songs= findSong(Environment.getExternalStorageDirectory());
        System.out.println("Size song: "+ songs.size());
    }

    public void runtimePermission(){            //quyền truy cập và ghi âm
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                             // nếu cho phép thì displaySong(hàm để hiển thị list bài hát)
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
    private void setUpEntitiesEvent() {

        btn = findViewById(R.id.btnHello);
        spinner = findViewById(R.id.selectLangSpinner);
        serverAddress = findViewById(R.id.serverAddressInput);

        String[] items = new String[]{"Select Language", "en", "vn"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedItem = spinner.getSelectedItem().toString();
                String serverAddr = String.valueOf(serverAddress.getText());
                if (selectedItem.equals("Select Language")) selectedItem = "en";
                startActivity(new Intent(getApplicationContext(), SpeechRecognizing.class).putExtra("language", selectedItem).putExtra("songs", songs).putExtra("server", serverAddr));
                // .putExtra("key", value)....
            }
        });

    }
    public ArrayList<File> findSong(File file){         //tìm tất cả bài hát trong cả bộ nhớ
        ArrayList<File> songsList = new ArrayList<>();

        File[] files = file.listFiles();        //list ra tất cả các file trong (File) file
        try {               //nhớ là phải trong try catch
            //check all file and folder
            for (int i= 0;i<files.length;++i) {
                File singleFile=files[i];
                if (singleFile.isDirectory() && !singleFile.isHidden()) {       //nếu là folder và không bị ẩn thì gọi đệ quy tìm trong folder đó
                    songsList.addAll(findSong(singleFile));         //check in single file

                } else {
                    if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                        songsList.add(singleFile);      //end = .mp3 hoặc wav thì thêm vào songList
                    }
                }
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        Collections.sort(songsList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return songsList;

    }
}
//    private void setRecognizerIntent(){
//        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        recognizerIntent.
//    }

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