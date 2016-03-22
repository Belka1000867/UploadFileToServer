package com.example.bel.uploadtoserver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by bel on 11.03.16.
 */
public class ServerRequests {

    ProgressDialog progressDialog;

    public static final String SERVER_ADDRESS = "http://serendipity.netne.net/";
    public static final String ENCODING_FORMAT = "UTF-8";
    public static final int MAX_BUFFER_SIZE = 10 * 1024 * 1024;
    HttpURLConnection httpURLConnection;
    InputStream in;
    DataOutputStream dataOutputStream;
    String lineEnd = "\r\n";
    String twoHyphens = "--";


    public ServerRequests(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }


    public void uploadRecordingInBackground(Context context, String filePath, String fileName, String username, GetUserCallback getUserCallback){
        progressDialog.show();
        new UploadRecordingAsyncTask( context, filePath, fileName, username, getUserCallback).execute();
    }

    public class UploadRecordingAsyncTask extends AsyncTask<Void,Void, Void> {
        String fileName;
        String filePath;
        String username;
        GetUserCallback getUserCallback;
        byte[] bytes;
        Context context;

        public UploadRecordingAsyncTask(Context context, String filePath, String fileName, String username, GetUserCallback getUserCallback){
            this.filePath = filePath;
            this.fileName = fileName;
            this.username = username;
            this.getUserCallback = getUserCallback;
            this.bytes = bytes;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String boundary = "*****serendipity*****";

            try{
                Log.d("DEBUG","Request is running!");

                URL url = new URL(SERVER_ADDRESS + "upload_file.php");
                httpURLConnection = (HttpURLConnection) url.openConnection(); //open connection with the server
                httpURLConnection.setChunkedStreamingMode(0);
                httpURLConnection.setDoOutput(true); // changing default GET method to POST
                httpURLConnection.setDoInput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Connection", "keep-alive");
                httpURLConnection.setRequestProperty("Content-type", "multipart/form-data;boundary=----" + boundary);
                //httpURLConnection.setRequestProperty("Content-disposition", "form-data; name=\"file\"; filename=\"" + fileName + "\"");
                //httpURLConnection.setRequestProperty("input type", "\"file\"; name=\"file\"");

                //httpURLConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                //httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                //CHECKING the server after writing the body of the request
                //Log.d("DEBUG", "Response code from server:" + httpURLConnection.getResponseCode());
                //Log.d("DEBUG", "Response message from server:" + httpURLConnection.getResponseMessage());
                //Log.d("DEBUG", "Request method to server:" + httpURLConnection.getRequestMethod());
                //Log.d("DEBUG", "Content type:" + httpURLConnection.getContentType()); // Default: text/html



                in = new BufferedInputStream(context.getResources().openRawResource(R.raw.etude));
                //httpURLConnection.setRequestProperty("Content-Length", Integer.toString(in.available()));
                //httpURLConnection.setFixedLengthStreamingMode(in.available());
                Log.d("DEBUG", "To string "+in.toString());

                //get outputstream from http connection
                dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.writeBytes("Content-disposition: post-data; name=file;filename"+fileName);
                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                //dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + lineEnd);
                //dataOutputStream.writeBytes("input type=\"file\" name=\"file\"");
//                dataOutputStream.writeBytes("Content-type: multipart/form-data;");
//                dataOutputStream.writeBytes(lineEnd);

//                int bytesAvailable = bytes.length;
//                Log.d("DEBUG", "Is available : " +  bytesAvailable);

                //FileOutputStream fileOutputStream = new


                // create a buffer of maximum size
                int availableBytes = in.available();
                Log.d("DEBUG", "availableBytes : " + availableBytes);
                int count = Math.min(availableBytes, MAX_BUFFER_SIZE);
                byte[] buffer = new byte[count];
                // read audio and write data in byte array buffer
                int bytesRead = in.read(buffer, 0, count);
                Log.d("DEBUG", "Bytes read from Input stream : " + bytesRead);

                while (bytesRead > 0)
                {
                    dataOutputStream.write(buffer, 0, count);
                    availableBytes = in.available();

                    Log.d("DEBUG", "Available bytes in while loop : " + availableBytes);

                    count = Math.min(availableBytes, MAX_BUFFER_SIZE);
                    bytesRead = in.read(buffer, 0, count);
                }
                in.close();



//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);







//                int buffersize = Math.min(bytesAvailable, maxBufferSize);
//                byte[] buffer = new byte[buffersize];

                //int bytesRead = inputStream.read(buffer, 0, buffersize);

//                while (bytesRead>0){
//                    dataOutputStream.write(buffer, 0, buffersize);
//                    bytesAvailable = inputStream.available();
//                    buffersize = Math.min(bytesAvailable, maxBufferSize);
//                    bytesRead = inputStream.read(buffer, 0, buffersize);
//                }

                //input stream to get response from the server
                //InputStream responseFromServer = new BufferedInputStream(httpURLConnection.getInputStream());
                //responseFromServer.close();


                dataOutputStream.flush();
                dataOutputStream.close();
                httpURLConnection.disconnect();
            }catch(MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            getUserCallback.done();
            super.onPostExecute(aVoid);
        }

    }

}
