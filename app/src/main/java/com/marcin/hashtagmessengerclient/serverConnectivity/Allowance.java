package com.marcin.hashtagmessengerclient.serverConnectivity;
/**************************************************************
 * Class used to get child user daily time allowance, uses
 * the base concept without response parsing
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.util.Log;

import com.marcin.hashtagmessengerclient.chat.Message;
import com.sendbird.android.shadow.okhttp3.Call;
import com.sendbird.android.shadow.okhttp3.Callback;
import com.sendbird.android.shadow.okhttp3.Credentials;
import com.sendbird.android.shadow.okhttp3.OkHttpClient;
import com.sendbird.android.shadow.okhttp3.Request;
import com.sendbird.android.shadow.okhttp3.RequestBody;
import com.sendbird.android.shadow.okhttp3.Response;

import org.json.JSONException;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class Allowance {

    private static String allowance ="";

    public String getAllowance(Long id) {
        String url = ServerController.getInstance().getIp()+"/api/child/getDailyAllowance?userId="+id;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", Credentials.basic(
                        ServerController.getInstance().getUsername(),
                        ServerController.getInstance().getPassword()))
                .build();
        Call call = client.newCall(request);
        //Enqueue - onResponse is executed as a call back once the 2nd thread is finished
        //
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MyLog", "Exception caught 0", e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    //checks if the response was received successfully
                    if (response.isSuccessful()) {
                        allowance = jsonData;
                    } else {
                        Log.e("MyLog", "Exception caught 1");
                    }
                } catch (IOException e) {
                    Log.e("MyLog", "Exception caught 2", e);
                }
            }
        });
        //gives the other thread opportunity to finish up
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allowance;
    }
}
