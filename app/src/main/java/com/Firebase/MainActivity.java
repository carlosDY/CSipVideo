package com.Firebase;

//@autor: Jefferson Rivera

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends Activity {

    Switch mySwitch;
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contenido);
        firebase.setAndroidContext(this);

        Intent intent = new Intent(this, FireService.class);
        startService(intent);

        mySwitch = (Switch)findViewById(R.id.mySwitch);

        firebase = new Firebase("https://fir-da62f.firebaseio.com/luces/sala");

        firebase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean estado = (Boolean) dataSnapshot.getValue();

                mySwitch.setChecked(estado);

                if(estado){
                    mySwitch.setText("Luz Sala Encendida");

                }else{
                    mySwitch.setText("Luz Sala Apagada");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mySwitch.setText("Luz Sala Encendida");
                    firebase.setValue(true);

                } else {
                    mySwitch.setText("Luz Sala Apagado");
                    firebase.setValue(false);
                }
            }
        });
}


}
