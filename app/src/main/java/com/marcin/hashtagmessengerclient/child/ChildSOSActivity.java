package com.marcin.hashtagmessengerclient.child;
/**************************************************************
 * Activity used for sending SOS alerts, prompts for
 * location permissions to inlcude in the message
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.marcin.hashtagmessengerclient.MyApplication;
import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.chat.Message;
import com.marcin.hashtagmessengerclient.chat.MessageAdapter;
import com.marcin.hashtagmessengerclient.login.LoginController;
import com.marcin.hashtagmessengerclient.parent.SOSAdapter;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

import java.util.ArrayList;
import java.util.List;

public class ChildSOSActivity extends AppCompatActivity implements View.OnTouchListener {

    private ImageView contacts;
    private ImageView myallowance;
    private ImageView sos;
    private ImageView sosButtonIV;

    private double latitude;
    private double longitude;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    boolean gotLocationPermissions = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_sos);

        contacts = findViewById(R.id.contacts);
        myallowance = findViewById(R.id.myallowance);
        sos = findViewById(R.id.sos);
        sosButtonIV = findViewById(R.id.sosButtonIV);

        contacts.setOnTouchListener(_ChildNavigation.getInstance());
        myallowance.setOnTouchListener(_ChildNavigation.getInstance());
        sos.setOnTouchListener(_ChildNavigation.getInstance());
        sosButtonIV.setOnTouchListener(this);

        //gets users weather location (latitude, longitude);
        gotLocationPermissions = checkLocationPermission();


    }



    //------------ Checks if location is enabled and prompts user for it ---------------------------
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission. ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission. ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Alert!")
                        .setMessage("We need your location to be able to send SOS to your parents")
                        .setPositiveButton("I understand", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(ChildSOSActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission. ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            return true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()){
            case R.id.sosButtonIV:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    //if app could get the location
                    if (gotLocationPermissions){
                        Message m = new Message(ServerController.getInstance().getId(),
                                0l, latitude+","+longitude, true, 1, Message.MSG_SENT);

                        ServerController.getInstance().sendMsg(m);
                        Toast.makeText(MyApplication.getAppContext(), "Message send!",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        gotLocationPermissions = checkLocationPermission();
                    }
                }
                break;
            }
        return true;
    }
}
