package com.example.speech_recognition.utils.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Server {
    private String host;
    private int port;
    public Server(String host, int port){
        this.host=host;
        this.port=port;
    }
    public int send(String message,int state) throws IOException {
        try(Socket client = new Socket(host, port)){
            DataOutputStream dout = new DataOutputStream(client.getOutputStream());
            dout.writeUTF(message);
            dout.flush();
            dout.close();
            System.out.println("done send!");
            if(state==0) state=1;
            return state;
        }

    }
}
