package com.marcin.hashtagmessengerclient.serverConnectivity;
/**************************************************************
 * Class used to for login process, returns the id based
 * on the username and password
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

public class Login {

    private static String id;

    public String login(String username, String password) {
        id="";
        String url = ServerController.getInstance().getIp()+"/api/contacts/login?username="+username+"&pw="+password;
        Log.v("MyLog", "url"+url);
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
                        id = jsonData;
                    } else {
                        Log.e("MyLog", "Exception caught 1");
                        id = "";
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
        return id;
    }
}
