// Copyright (c) 2022 Gn0hp
// 
// This software is provided 'as-is', without any express or implied
// warranty. In no event will the authors be held liable for any damages
// arising from the use of this software.
// 
// Permission is granted to anyone to use this software for any purpose,
// including commercial applications, and to alter it and redistribute it
// freely, subject to the following restrictions:
// 
// 1. The origin of this software must not be misrepresented; you must not
//    claim that you wrote the original software. If you use this software
//    in a product, an acknowledgment in the product documentation would be
//    appreciated but is not required.
// 2. Altered source versions must be plainly marked as such, and must not be
//    misrepresented as being the original software.
// 3. This notice may not be removed or altered from any source distribution.

package test_tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.io.*;

public class tcpServer {
    public static void main(String[] args) throws Exception {
        try (ServerSocket serv = new ServerSocket(2207)) {
            while (true) {
                Socket client = serv.accept();
                // OutputStream out = (client.getOutputStream());
                // InputStream in = (client.getInputStream());
                // int res = in.read();
                // while (res != -1) {
                // System.out.println((char) res);
                // res = in.read();
                // }

                // BufferedReader br = new BufferedReader(new
                // InputStreamReader(client.getInputStream()));
                // PrintWriter pw = new PrintWriter(new
                // OutputStreamWriter(client.getOutputStream()));
                // System.out.println(br.readLine());

                DataInputStream inputStream = new DataInputStream(client.getInputStream());
                // DataOutputStream dout = new DataOutputStream(client.getOutputStream());
                String data = inputStream.readUTF();
                System.out.println(data);
                // dout.writeInt(20);
                // dout.writeInt(30);

                // ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                // ObjectInputStream ooin = new ObjectInputStream(client.getInputStream());

                // ArrayList<Integer> arr = (ArrayList<Integer>) ooin.readObject();
                // Collections.sort(arr);
                // for (Integer i : arr) {
                // System.out.printf("%d ", i);
                // }

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
