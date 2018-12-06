package com.marcin.hashtagmessengerclient.parent;
/**************************************************************
 * Singleton controller used for parent navigation to reduce
 * code duplication
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import com.marcin.hashtagmessengerclient.MyApplication;
import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.contacts.SearchActivity;
import com.marcin.hashtagmessengerclient.login.LoginController;

public class _ParentNavigation implements View.OnTouchListener {

    //singleton
    private static final _ParentNavigation ourInstance = new _ParentNavigation();
    public static _ParentNavigation getInstance() {
        return ourInstance;
    }
    private _ParentNavigation(){}

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.contacts:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), ParentContactsActivity.class);
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;
            case R.id.mykids:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), ParentMyKidsActivity.class);
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;
            case R.id.home:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), ParentHomeActivity.class);
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;
            case R.id.settings:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), ParentSettingsActivity.class);
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;
            case R.id.createchild:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), ParentNewChildActivity.class);
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;
            case R.id.searchET:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), SearchActivity.class);
                    intent.putExtra("isItParent", true);
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;
        }
        return true;
    }
}
