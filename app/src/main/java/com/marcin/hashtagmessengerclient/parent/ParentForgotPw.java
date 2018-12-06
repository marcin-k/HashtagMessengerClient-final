package com.marcin.hashtagmessengerclient.parent;
/**************************************************************
 * Activity used to reset parent password
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

public class ParentForgotPw extends AppCompatActivity implements View.OnTouchListener {

    private EditText email;
    private TextView sendTempPW;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_forgot_pw);
        email = findViewById(R.id.email);
        sendTempPW = findViewById(R.id.sendTempPW);
        sendTempPW.setOnTouchListener(this);
        backButton = findViewById(R.id.backButton);
        backButton.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.backButton:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), ParentLoginActivity.class);
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;
            default:
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(sendTempPW);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(sendTempPW);
                    //TODO: verify the login and trigger contact_item load
                    //email.getText().toString();
                    Toast.makeText(getApplicationContext(), "Password has been send",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, ParentLoginActivity.class);
                    startActivity(intent);
                }
        }

        return true;
    }
}

