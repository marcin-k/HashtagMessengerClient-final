package com.marcin.hashtagmessengerclient.login;
/**************************************************************
 * Activity used to informed user that device that they are using
 * is not supported due to low resolution
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.marcin.hashtagmessengerclient.R;

public class NotSupportedResolutionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_supported_resolution);
    }
}
