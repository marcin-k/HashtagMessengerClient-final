package com.marcin.hashtagmessengerclient.child;
/**************************************************************
 * Navigation class, used to move between activities
 * when using navigation buttons (bottom of the screen)
 * Singleton controller
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.marcin.hashtagmessengerclient.MyApplication;
import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.contacts.SearchActivity;
import com.marcin.hashtagmessengerclient.login.LoginController;


public class _ChildNavigation implements View.OnTouchListener{


    //singleton
    private static final _ChildNavigation ourInstance = new _ChildNavigation();
    public static _ChildNavigation getInstance() {
        return ourInstance;
    }
    private _ChildNavigation(){}

    //base on element that trigger the activity corresponding action is performed
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.contacts:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), ChildContactsActivity.class);
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;
            case R.id.myallowance:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), ChildAllowanceActivity.class);
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;
            case R.id.sos:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), ChildSOSActivity.class);
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;

            case R.id.searchET:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), SearchActivity.class);
                    intent.putExtra("isItParent", false);
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;
        }
        return true;
    }
}
