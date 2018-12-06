package com.marcin.hashtagmessengerclient.login;
/**************************************************************
 * Singleton class to emulate button movements
 * by shirking the view on press down by 10% and
 * restoring to original size when finger is lift up
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginController<T> {

    //singleton
    private static final LoginController ourInstance = new LoginController();
    public static LoginController getInstance() {
        return ourInstance;
    }
    private LoginController(){}


//---------------------------------Button Pressed Animation-----------------------------------------
    //on touch down - shrink
public void animateButtonTouched(T view){
        ObjectAnimator buttonXDown = ObjectAnimator.ofFloat(view, "scaleX",
                1, 0.9f);
        ObjectAnimator buttonYDown = ObjectAnimator.ofFloat(view, "scaleY",
                1, 0.9f);
        AnimatorSet scalePlayButtonDown = new AnimatorSet();
        scalePlayButtonDown.playTogether(buttonXDown, buttonYDown);
        scalePlayButtonDown.setDuration(100);
        scalePlayButtonDown.start();

    }

    //on release - resize to original size
    public void animateButtonReleased(T view){
        ObjectAnimator buttonXUp = ObjectAnimator.ofFloat(view, "scaleX",
                0.9f, 1);
        ObjectAnimator buttonYUp = ObjectAnimator.ofFloat(view, "scaleY",
                0.9f, 1);
        AnimatorSet scalePlayButtonUp = new AnimatorSet();
        scalePlayButtonUp.playTogether(buttonXUp, buttonYUp);
        scalePlayButtonUp.setDuration(100);
        scalePlayButtonUp.start();
    }
}
