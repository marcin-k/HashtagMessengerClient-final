package com.marcin.hashtagmessengerclient.child;
/**************************************************************
 * Activity that shows the childs daily time allowance
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

public class ChildAllowanceActivity extends AppCompatActivity {

    private ImageView contacts;
    private ImageView myallowance;
    private ImageView sos;
    private TextView dailyLimitTV;
    private TextView timeLeftTodayTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_allowance);

        contacts = findViewById(R.id.contacts);
        myallowance = findViewById(R.id.myallowance);
        sos = findViewById(R.id.sos);
        dailyLimitTV = findViewById(R.id.dailyLimitTV);
        timeLeftTodayTV = findViewById(R.id.timeLeftTodayTV);

        contacts.setOnTouchListener(_ChildNavigation.getInstance());
        myallowance.setOnTouchListener(_ChildNavigation.getInstance());
        sos.setOnTouchListener(_ChildNavigation.getInstance());

        String allowance = ServerController.getInstance().getAllowance();
//        Log.v("allowance:",allowance);
        dailyLimitTV.setText(allowance+" min");

        int timeLeft = Integer.parseInt(allowance);
        timeLeftTodayTV.setText((timeLeft-25)+" min");

    }
}
