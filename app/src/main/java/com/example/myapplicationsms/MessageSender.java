package com.example.myapplicationsms;


import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageSender extends AsyncTask<String,Void,Void> {

    Socket s;
    DataOutputStream dos;
    PrintWriter pw;

    @Override
    protected Void doInBackground(String... voids) {
        String message = voids[0];
        String ipTCP = voids[1];
        String portTCP = voids[2];
        try {
            int portTCPR =Integer.parseInt(portTCP);
            s= new Socket(ipTCP,portTCPR);
            pw = new PrintWriter(s.getOutputStream());
            pw.write(message);
            pw.flush();
            s.close();

        }catch (IOException e)
        {
            e.printStackTrace();
        }



        return null;
    }
}