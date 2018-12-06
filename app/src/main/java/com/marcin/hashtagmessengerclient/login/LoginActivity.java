package com.marcin.hashtagmessengerclient.login;
/**************************************************************
 * Despite the class name this is application entry point
 * user can select to login as child, parent or create a new
 * parent account from here, if the device is not supported
 * (due to low resolution) user is redirected to
 * NotSupportedResolutionActivity (dead end)
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.marcin.hashtagmessengerclient.R;

import com.marcin.hashtagmessengerclient.child.ChildLoginActivity;
import com.marcin.hashtagmessengerclient.parent.ParentLoginActivity;
import com.marcin.hashtagmessengerclient.parent.RegisterParentActivity;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;


public class LoginActivity extends AppCompatActivity implements View.OnTouchListener{

    private TextView titleTV;
    private TextView titleTV2;
    private TextView childTV;
    private TextView parentTV;
    private TextView registerParentTV;


    private ImageView childIV;
    private ImageView parentIV;
    private ImageView registerParentIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //checks the screen resolution if lower than 1600 x 1000
        //redirect to screen showing that requires higher resolution
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        if (height < 1600 || width < 1000){
            Intent intentChild = new Intent(this, NotSupportedResolutionActivity.class);
            startActivity(intentChild);
        }

        Log.v("MyLog","filecontent"+ServerController.getInstance().getIp());

        //UI text view
        titleTV = findViewById(R.id.titleTV);
        titleTV2 = findViewById(R.id.titleTV2);
        childTV = findViewById(R.id.childTV);
        parentTV = findViewById(R.id.parentTV);
        registerParentTV = findViewById(R.id.registerParentTV);
        //UI image view
        childIV = findViewById(R.id.childIV);
        parentIV = findViewById(R.id.parentIV);
        registerParentIV = findViewById(R.id.registerParentIV);

        //gets the custom fonts and sets the text to those fonts
        Typeface pacifico = Typeface.createFromAsset(getAssets(),  "fonts/Pacifico-Regular.ttf");
        titleTV.setTypeface(pacifico);
        titleTV2.setTypeface(pacifico);
        childTV.setTypeface(pacifico);
        parentTV.setTypeface(pacifico);
        registerParentTV.setTypeface(pacifico);


        childIV.setOnTouchListener(this);
        parentIV.setOnTouchListener(this);
        registerParentIV.setOnTouchListener(this);
    }

//----------------------OnClick--------------------------------------------
    public boolean touchPlay(MotionEvent event) {

        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.childIV:
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(childIV);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(childIV);
                    Intent intentChild = new Intent(this, ChildLoginActivity.class);
                    startActivity(intentChild);
                }
                break;

            case R.id.parentIV:
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(parentIV);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(parentIV);
                    Intent intentParent = new Intent(this, ParentLoginActivity.class);
                    startActivity(intentParent);
                }
                break;

            case R.id.registerParentIV:
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(registerParentIV);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(registerParentIV);
                    Intent intentNewAccount = new Intent(this, RegisterParentActivity.class);
                    startActivity(intentNewAccount);
                }
                break;
        }
    return true;
    }
}