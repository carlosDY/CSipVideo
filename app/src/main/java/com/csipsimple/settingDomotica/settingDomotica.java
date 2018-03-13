package com.csipsimple.garage;

/**
 * Created by Carlos on 3/8/2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import com.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import com.R;
import com.actionbarsherlock.app.SherlockFragment;
import com.csipsimple.api.SipManager;
import com.csipsimple.ui.SipHome;
import com.csipsimple.utils.Log;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class settingDomotica extends SherlockFragment {

    private static final String THIS_FILE = "settingDomotica_Fragment";

    private Button BotonPrueba;
    private TextView TextoPrueba;
    private boolean mDualPane;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDualPane = getResources().getBoolean(R.bool.use_dual_panes);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.config_domotica, container, false);
        BotonPrueba=(Button) v.findViewById(R.id.configBoton);

        TextoPrueba=(TextView) v.findViewById(R.id.configText);

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

        int viewId = view.getId();
        if (viewId == BotonPrueba.getId()) {
            TextoPrueba.setText("Se aplasto el Boton y esta dentro del fragement Well Done ");
        }
    }
}


