package com.marcin.hashtagmessengerclient.parent;
/**************************************************************
 * Activity used to create a new parent account triggered
 * from the application entry point (Login activity)
 * checks:
 * - if email address uses @ and .
 * - if every entry has at least 3 characters
 * - username is not in use
 * - password mataches retyped password
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
import com.marcin.hashtagmessengerclient.serverConnectivity.NewUserCreator;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;


public class RegisterParentActivity extends AppCompatActivity implements View.OnTouchListener {

    private TextView registerTV;
    private EditText firstNameET;
    private EditText lastNameET;
    private EditText emailET;
    private EditText usernameET;
    private EditText passwordET;
    private EditText password2ET;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_parent);
        registerTV = findViewById(R.id.updateTV);
        firstNameET = findViewById(R.id.firstNameET);
        lastNameET = findViewById(R.id.lastNameET);
        emailET = findViewById(R.id.emailET);
        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        password2ET = findViewById(R.id.password2ET);
        backButton = findViewById(R.id.backButton);
        backButton.setOnTouchListener(this);
        registerTV.setOnTouchListener(this);

    }

    //----------------------OnClick--------------------------------------------
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.updateTV:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(registerTV);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(registerTV);

                    //checks if the info entered has at least 3 char in length each
                    //prevents empty form
                    if (usernameET.getText().toString().length() < 3 ||
                            firstNameET.getText().toString().length() < 3 ||
                            lastNameET.getText().toString().length() < 3 ||
                            emailET.getText().toString().length() < 3 ||
                            password2ET.getText().toString().length() < 3) {

                        Toast.makeText(MyApplication.getAppContext(), "Incorrect info entered " +
                                        "please correct the from, all fields must be at least 3" +
                                        " characters long!",
                                Toast.LENGTH_LONG).show();
                    }

                    //checks the email address

                    else if (!emailET.getText().toString().contains("@") ||
                            !emailET.getText().toString().contains(".")) {

                        Toast.makeText(MyApplication.getAppContext(), "Incorrect info entered " +
                                        "Please correct your email address!",
                                Toast.LENGTH_LONG).show();
                    } else if (usernameET.getText().toString().contains("\"") ||
                            firstNameET.getText().toString().contains("\"") ||
                            usernameET.getText().toString().contains("\"") ||
                            lastNameET.getText().toString().contains("\"") ||
                            emailET.getText().toString().contains("\"") ||
                            password2ET.getText().toString().contains("\"")) {

                        Toast.makeText(MyApplication.getAppContext(), "Incorrect info entered " +
                                        "You can not use \" symbol!",
                                Toast.LENGTH_LONG).show();
                    } else if (password2ET.getText().toString().equals(passwordET.getText().toString())) {
                        ParentUser parentUser = new ParentUser(
                                usernameET.getText().toString(),
                                firstNameET.getText().toString(),
                                lastNameET.getText().toString(),
                                passwordET.getText().toString(),
                                emailET.getText().toString());

                        try {
                            //TODO: change pass to hash
                            //to prevent false positive when one account was already created during app run
                            if (ServerController.getInstance().isLoginFree(usernameET.getText().toString())) {
                                NewUserCreator.userId = "";
                                Long id = Long.parseLong(ServerController.getInstance().registerParent(parentUser));
                                String pwcreated = ServerController.getInstance().setPassword(
                                        id, passwordET.getText().toString());

                                Intent intent = new Intent(MyApplication.getAppContext(), ParentLoginActivity.class);
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
                    //            Prompt password do not match
                    else {
                        Toast.makeText(MyApplication.getAppContext(), "Password do not match, please " +
                                        "make sure you enter correct information!",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.backButton:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), LoginActivity.class);
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;
        }
        return true;
    }
}
