package com.marcin.hashtagmessengerclient.parent;
/**************************************************************
 * Activity used for parent login
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
import com.marcin.hashtagmessengerclient.child.ChildContactsActivity;
import com.marcin.hashtagmessengerclient.login.LoginActivity;
import com.marcin.hashtagmessengerclient.login.LoginController;
import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

public class ParentLoginActivity extends AppCompatActivity implements View.OnTouchListener{

    private TextView loginTV;
    private TextView forgotPwTV;
    private EditText username;
    private EditText password;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        loginTV = findViewById(R.id.loginTV);
        forgotPwTV = findViewById(R.id.forgotPwTV);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginTV.setOnTouchListener(this);
        forgotPwTV.setOnTouchListener(this);
        backButton = findViewById(R.id.backButton);
        backButton.setOnTouchListener(this);
    }

    //----------------------OnClick--------------------------------------------
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.loginTV:
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(loginTV);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(loginTV);
                    String stringId = ServerController.getInstance().login(username.getText().toString(), password.getText().toString());
                    Long id;
                    try {
                        id = Long.parseLong(stringId);
                        if (id==0l){
                            Toast.makeText(MyApplication.getAppContext(), "Login unsuccessful",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            if (ServerController.getInstance().isItaParent(id)) {
                                ServerController.getInstance().setId(id);
                                ServerController.getInstance().setUsername(username.getText().toString());
                                ServerController.getInstance().setPassword(password.getText().toString());
                                Intent intent = new Intent(this, WelcomeScreenActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(MyApplication.getAppContext(), "You are using child's username," +
                                                " please use child's login page",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    catch (NumberFormatException e){
                        Toast.makeText(MyApplication.getAppContext(), "Can not reach server, check your network connection",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.forgotPwTV:
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(forgotPwTV);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(forgotPwTV);
                    Intent intent = new Intent(this, ParentForgotPw.class);
                    startActivity(intent);
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
