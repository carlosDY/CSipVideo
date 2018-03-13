package com.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class FireService extends Service {

    private static final String TAG = "FireService";
    protected Firebase firebase;
    private PendingIntent pendingIntent;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private  int icono;


    @Override
    public void onCreate() {

        Log.i(TAG, "Service onCreate");
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://fir-da62f.firebaseio.com/luces/sala");

        Intent myIntent = new Intent(this, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, myIntent, 0);
        //Initialize NotificationManager using Context.NOTIFICATION_SERVICE
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        new Thread(new Runnable() {

            @Override
            public void run() {

                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Boolean estado = (Boolean) dataSnapshot.getValue();
                        String msgNotificacion;

                        if(estado){
                             msgNotificacion = "Luz Sala Encendida";
                             icono = R.drawable.abs__ab_bottom_solid_dark_holo;

                        }else{
                             msgNotificacion = "Luz Sala Apagada";
                             icono = R.drawable.appwidget_inner_focus;
                        }
                        Log.v("CAMBIOOOOO DE:",msgNotificacion);

                        notificacion(msgNotificacion, icono);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                //Stop service once it finishes its task
                //stopSelf();
            }
        }).start();

        return Service.START_STICKY;
    }


    public void  notificacion(String msg, int icono){

        //Prepare Notification Builder
        notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Domótica").setSmallIcon(icono).setContentIntent(pendingIntent)
                .setContentText(msg);
        //add sound
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(uri);
        long[] v = {80,260,80};
        notificationBuilder.setVibrate(v);
        notificationManager.notify(1, notificationBuilder.build());
    }


    //The system calls this method when another component wants to bind with the service
    @Override
    public void onDestroy() {
        Log.i(TAG, "SERVICIO DESTRUIDO");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

}
