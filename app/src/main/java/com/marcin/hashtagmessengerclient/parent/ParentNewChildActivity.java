package com.marcin.hashtagmessengerclient.parent;
/**************************************************************
 * Activity used to create a new child user account
 * setting the can be found attribute affects if the account would
 * be returned during searches
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.marcin.hashtagmessengerclient.MyApplication;
import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.chat.Message;
import com.marcin.hashtagmessengerclient.login.LoginController;
import com.marcin.hashtagmessengerclient.model.BaseUser;
import com.marcin.hashtagmessengerclient.model.ChildUser;
import com.marcin.hashtagmessengerclient.model.ParentUser;
import com.marcin.hashtagmessengerclient.serverConnectivity.NewUserCreator;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

import java.util.ArrayList;

public class ParentNewChildActivity extends AppCompatActivity implements View.OnTouchListener {

    private Spinner allowanceSP;
    private ImageView mykids;
    private EditText firstNameET;
    private EditText lastNameET;
    private EditText usernameET;
    private EditText passwordET;
    private Switch canAddSW;
    private Switch canReceiveSW;
    private Switch canBeFoundSW;
    private TextView createTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_new_child);

        //spinner setup for picking up allowance
        allowanceSP = (Spinner) findViewById(R.id.allowanceSP);
        //---- Sets a custom style for spinner(spinner.xml) and a dropdown menu(spinner_dropdown.xml)
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.duration_array, R.layout.spinner);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        allowanceSP.setAdapter(adapter);

        mykids = findViewById(R.id.mykids);
        firstNameET = findViewById(R.id.firstNameET);
        lastNameET = findViewById(R.id.lastNameET);
        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        canAddSW = findViewById(R.id.canAddSW);
        canReceiveSW = findViewById(R.id.canReceiveSW);
        canBeFoundSW = findViewById(R.id.canBeFoundSW);
        createTV = findViewById(R.id.updateTV);

        //register listeners for buttons
        mykids.setOnTouchListener(_ParentNavigation.getInstance());
        createTV.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            LoginController.getInstance().animateButtonTouched(v);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            //checks if the info entered has at least 3 char in length each
            //prevents empty form
            if (usernameET.getText().toString().length() < 3 ||
                    firstNameET.getText().toString().length() < 3 ||
                    usernameET.getText().toString().length() < 3 ||
                    passwordET.getText().toString().length() < 3) {

                Toast.makeText(MyApplication.getAppContext(), "Incorrect info entered " +
                                "please correct the from, all fields must be at least 3" +
                                " characters long!",
                        Toast.LENGTH_LONG).show();
            }
            else{

                ChildUser child = new ChildUser(
                        usernameET.getText().toString(),
                        firstNameET.getText().toString(),
                        lastNameET.getText().toString(),
                        0l,
                        new ArrayList<BaseUser>(),
                        new ArrayList<Message>(),
                        passwordET.getText().toString(),
                        new ArrayList<ParentUser>(),
                        canAddSW.isChecked(),
                        canBeFoundSW.isChecked(),
                        canReceiveSW.isChecked(),
                        Integer.parseInt(allowanceSP.getSelectedItem().toString()));

                try {
                    if (ServerController.getInstance().isLoginFree(usernameET.getText().toString())) {
                        //to prevent false positive when one account was already created during app run
                        NewUserCreator.userId = "";
                        Long id = Long.parseLong(ServerController.getInstance().createChild(child));
                        String pwcreated = ServerController.getInstance().setPassword(
                                id, passwordET.getText().toString());

                        Intent intent = new Intent(MyApplication.getAppContext(), ParentMyKidsActivity.class);
                        Toast.makeText(MyApplication.getAppContext(), "Account Created!",
                                Toast.LENGTH_LONG).show();
                        MyApplication.getAppContext().startActivity(intent);
                    }
                    else{
                        Toast.makeText(MyApplication.getAppContext(), "Username is not available, pick different username",
                                Toast.LENGTH_LONG).show();
                    }
                }
                //exception would be thrown if the server cant be reach the empty string is returned
                catch (NumberFormatException e) {
                    Toast.makeText(MyApplication.getAppContext(), "Cant reach the server " +
                                    "please check your network connection!",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        return true;
    }
}
