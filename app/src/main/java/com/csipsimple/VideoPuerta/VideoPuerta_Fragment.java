package com.csipsimple.VideoPuerta;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.R;
import com.actionbarsherlock.app.SherlockFragment;
import com.camera.simplemjpeg.MjpegActivity;
import com.camera.simplemjpeg.MjpegInputStream;
import com.camera.simplemjpeg.MjpegView;
import com.csipsimple.api.SipManager;
import com.csipsimple.ui.SipHome;
import com.csipsimple.utils.Log;
import android.support.v4.app.FragmentTransaction;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class VideoPuerta_Fragment extends SherlockFragment  implements View.OnClickListener {

    private static final String THIS_FILE = "garage_Fragment";
    private static final boolean DEBUG=false;
    private static final String TAG = "MJPEG";

    private Button BotonPrueba;
    private Button buttonConnect;
    private TextView TextoPrueba;
    private boolean mDualPane;
    String width="320";
    String height="240";
    String portnum =  "5000";
    String hostname = "192.168.1.109";
    String puerta =  "8080";
    String mensaje= "777";
    private MjpegView mv = null;

    //private FirebaseAnalytics mFirebaseAnalytics;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDualPane = getResources().getBoolean(R.bool.use_dual_panes);
        MjpegView.setImageSize( Integer.parseInt( width), Integer.parseInt(height));
        new DoRead().execute( hostname, portnum);


        }

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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.main, container, false);
        mv = (MjpegView) v.findViewById(R.id.mv);
        buttonConnect = (Button) v.findViewById(R.id.connect);
        buttonConnect.setOnClickListener(this);
              //  BotonPrueba = (Button) v.findViewById(R.id.Boton_Garage);
       // BotonPrueba.setOnClickListener(this);
       // TextoPrueba = (TextView) v.findViewById(R.id.Texto_Garage);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        Intent serviceIntent = new Intent(SipManager.INTENT_SIP_SERVICE);
        // Optional, but here we bundle so just ensure we are using csipsimple package
        serviceIntent.setPackage(activity.getPackageName());
    }

    @Override
    public void onDetach() {
        try {
            //   getActivity().unbindService(connection);
        } catch (Exception e) {
            // Just ignore that
            Log.w(THIS_FILE, "Unable to un bind", e);
        }
        // dialFeedback.pause();
        super.onDetach();
    }

    public void onClick(View view) {
        // ImageButton b = null;
        int viewId = view.getId();
        if (viewId == buttonConnect.getId())
        {
            MyClientTask myClientTask = new MyClientTask(hostname,
                    Integer.parseInt(puerta));
            myClientTask.execute(mensaje);

        }
        }

    //////////////////////////////////////////
    public void onResume() {
        if(DEBUG) android.util.Log.d(TAG,"onResume()");
        super.onResume();
        if(mv!=null){
            mv.resumePlayback();
        }

    }

    public void onStart() {
        if(DEBUG) android.util.Log.d(TAG,"onStart()");
        super.onStart();
    }
    public void onPause() {
        if(DEBUG) android.util.Log.d(TAG,"onPause()");
        super.onPause();
        if(mv!=null){
            mv.stopPlayback();
        }
    }
    public void onStop() {
        if(DEBUG) android.util.Log.d(TAG,"onStop()");
        super.onStop();
    }

    public void onDestroy() {
        if(DEBUG) android.util.Log.d(TAG,"onDestroy()");

        if(mv!=null){
            mv.freeCameraMemory();
        }

        super.onDestroy();
    }

    }

