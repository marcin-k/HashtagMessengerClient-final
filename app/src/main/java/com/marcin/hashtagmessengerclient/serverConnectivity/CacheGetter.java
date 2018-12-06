package com.marcin.hashtagmessengerclient.serverConnectivity;
/**************************************************************
 * Class to fill the cache with first names and their ids, server
 * retuns a string of all ids and first names separeted by
 * commas, parsing is done in the cache controller
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.util.Log;

import com.sendbird.android.shadow.okhttp3.Call;
import com.sendbird.android.shadow.okhttp3.Callback;
import com.sendbird.android.shadow.okhttp3.Credentials;
import com.sendbird.android.shadow.okhttp3.OkHttpClient;
import com.sendbird.android.shadow.okhttp3.Request;
import com.sendbird.android.shadow.okhttp3.Response;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class CacheGetter {

    private String cache = "";

    public String getCache(){
        cache = "";
        String url = ServerController.getInstance().getIp()+"/api/contacts/cache";
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
                Log.e("MyLog", "Exception caught ", e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    //checks if the response was received successfully
                    if (response.isSuccessful()) {
                        cache = jsonData;
                    } else {
                        Log.e("MyLog", "Exception caught ");
                    }
                } catch (IOException e) {
                    Log.e("MyLog", "Exception caught ", e);
                }
            }
        });
        //gives the other thread opportunity to finish up
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return cache;

    }
}
