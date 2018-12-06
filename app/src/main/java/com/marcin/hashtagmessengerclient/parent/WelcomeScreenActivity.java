package com.marcin.hashtagmessengerclient.parent;
/**************************************************************
 * Activity created for performance improvement, as when the
 * parent user initially loges in, there is a big number of queries
 * to spring boot server (checks login, checks user type etc...)
 * The parent's home page consists of 2 list views which requires
 * additional queries (which was default where the login leaded to).
 * To prevent slow login process this activity caches users first
 * names used in the lists in home screen and welcomes the user to the
 * app. This activity is only visible after login (as the cache is
 * available until the app shutdowns)
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
import android.widget.TextView;

import com.marcin.hashtagmessengerclient.MyApplication;
import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.cache.CacheController;
import com.marcin.hashtagmessengerclient.cache.CachedUser;
import com.marcin.hashtagmessengerclient.login.LoginController;

public class WelcomeScreenActivity extends AppCompatActivity {

    private TextView continueTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        CacheController.getInstance();

        continueTV = findViewById(R.id.continueTV);
        continueTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), ParentHomeActivity.class);
                    MyApplication.getAppContext().startActivity(intent);
                }
                return true;
            }
        });
    }
}
