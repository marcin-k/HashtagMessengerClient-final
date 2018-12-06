package com.marcin.hashtagmessengerclient.parent;
/**************************************************************
 * Setting activity is used to update parent password
 * checks if the old password match the cached password
 * and if new and retyped passwords are the same
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marcin.hashtagmessengerclient.MyApplication;
import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.login.LoginActivity;
import com.marcin.hashtagmessengerclient.login.LoginController;
import com.marcin.hashtagmessengerclient.model.ParentUser;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

public class ParentSettingsActivity extends AppCompatActivity implements View.OnTouchListener{

    private ImageView contacts;
    private ImageView home;
    private ImageView mykids;
    private ImageView settings;

    private EditText currentPW;
    private EditText newPW;
    private EditText newPW2;
    private TextView updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_settings);

        //buttons
        contacts = findViewById(R.id.contacts);
        home = findViewById(R.id.home);
        mykids = findViewById(R.id.mykids);
        settings = findViewById(R.id.settings);

        currentPW = findViewById(R.id.currentPW);
        newPW = findViewById(R.id.newPW);
        newPW2 = findViewById(R.id.newPW2);
        updateButton = findViewById(R.id.updateButton);
        updateButton.setOnTouchListener(this);

        contacts.setOnTouchListener(_ParentNavigation.getInstance());
        home.setOnTouchListener(_ParentNavigation.getInstance());
        mykids.setOnTouchListener(_ParentNavigation.getInstance());
        settings.setOnTouchListener(_ParentNavigation.getInstance());
    }

    //----------------------OnClick--------------------------------------------
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.updateButton:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(updateButton);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(updateButton);

                    if (!currentPW.getText().toString().equals(ServerController.getInstance()
                            .getPassword())){

                        Toast.makeText(MyApplication.getAppContext(), "Incorrect " +
                                        "Current Password! Please try again",
                                Toast.LENGTH_LONG).show();
                    }
                    else if(!newPW.getText().toString().equals(newPW2.getText().toString())){
                        Toast.makeText(MyApplication.getAppContext(), "New and retyped password " +
                                        "do not match!",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        String pwcreated = ServerController.getInstance().setPassword(
                                ServerController.getInstance().getId(), newPW.getText().toString());
                        if (pwcreated.equalsIgnoreCase("password set")){
                            Toast.makeText(MyApplication.getAppContext(), "Password updated " +
                                            "successfully",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(MyApplication.getAppContext(), "Could not update the " +
                                            "password, please check you network connection",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }
            break;
        }
        return true;
    }
}
