package com.camera.simplemjpeg;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
//import com.camera.R;

import com.R;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

//import android.support.v7.app.AppCompatActivity;


public class MjpegActivity extends Activity  {
    private static final boolean DEBUG=false;
    private static final String TAG = "MJPEG";
    String width="320";
    String height="240";
    String portnum =  "5000";
    String hostname = "192.168.1.109";
    String puerta =  "8080";
    String mensaje= "777";

    private MjpegView mv = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MjpegView.setImageSize( Integer.parseInt( width), Integer.parseInt(height));

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);
        mv = (MjpegView) findViewById(R.id.mv);

        // receive parameters from PreferenceActivity
        Bundle bundle = getIntent().getExtras();

        new DoRead().execute( hostname, portnum);


        Button  buttonConnect = (Button) findViewById(R.id.connect);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyClientTask myClientTask = new MyClientTask(hostname,
                        Integer.parseInt(puerta));
                myClientTask.execute(mensaje);

            }
        });



    }
//////////////////////////////////////////////////////7



    public class MyClientTask extends AsyncTask<String, Void, Void> {

        String dstAddress;
        int dstPort;
        String response;

        MyClientTask(String addr, int port) {
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Socket socket = new Socket(dstAddress, dstPort);

                OutputStream outputStream = socket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(params[0]);

                socket.close();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

    }




    //////////////////////////////////////////
    public void onResume() {
        if(DEBUG) Log.d(TAG,"onResume()");
        super.onResume();
        if(mv!=null){
            mv.resumePlayback();
        }

    }

    public void onStart() {
        if(DEBUG) Log.d(TAG,"onStart()");
        super.onStart();
    }
    public void onPause() {
        if(DEBUG) Log.d(TAG,"onPause()");
        super.onPause();
        if(mv!=null){
            mv.stopPlayback();
        }
    }
    public void onStop() {
        if(DEBUG) Log.d(TAG,"onStop()");
        super.onStop();
    }

    public void onDestroy() {
        if(DEBUG) Log.d(TAG,"onDestroy()");

        if(mv!=null){
            mv.freeCameraMemory();
        }

        super.onDestroy();
    }

    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground(String... params){
            Socket socket = null;
            try {
                socket = new Socket( params[0], Integer.valueOf( params[1]));
                return (new MjpegInputStream(socket.getInputStream()));
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(MjpegInputStream result) {
            mv.setSource(result);
            if(result!=null) result.setSkip(1);
            mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
            mv.showFps(true);
        }
    }
}