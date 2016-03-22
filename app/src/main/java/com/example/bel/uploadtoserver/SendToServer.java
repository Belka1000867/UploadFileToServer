package com.example.bel.uploadtoserver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bel on 11.03.16.
 */
public class SendToServer extends AppCompatActivity {

    String fileName = "etude.mp3";
    String username = "Aleksandr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);

//        String filepath = Uri.parse("android.resource//" + getPackageName() + File.separator + R.raw.etude).getPath();
//        Log.d("DEBUG", "filepath :" + filepath);


        //open audio file with input stream
        //InputStream inputStream = getResources().openRawResource(R.raw.etude);

        //wrapping
        //DataInputStream dataInputStream = new DataInputStream(inputStream);



        //InputStream inputStream = getResources().openRawResource(R.raw.etude);
        //inputStream = new BufferedInputStream(inputStream);


            //new byte array with length of file
//            byte[] bytes = new byte[inputStream.available()];
//            Log.d("DEBUG", "InputStream available size :" + inputStream.available());
//            inputStream.read(bytes);

//            OutputStream outputStream = new FileOutputStream();
//            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
//            Log.d("DEBUG", "OUTPUT filepath :" + outputStream.toString());
//            dataOutputStream.write(bytes);
//            dataOutputStream.flush();
//            dataOutputStream.close();

            ServerRequests serverRequests = new ServerRequests(SendToServer.this);
            serverRequests.uploadRecordingInBackground(getApplicationContext(), "", fileName, username, new GetUserCallback() {
                @Override
                public void done() {
                    Log.d("DEBUG", "Uploading files finished.");
                }
            });

            //inputStream.close();
            //inputStream.close();

            //Log.d("DEBUG", "Bytes length:" + bytes.length);

    }


}
