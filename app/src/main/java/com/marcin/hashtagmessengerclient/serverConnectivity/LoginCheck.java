package com.marcin.hashtagmessengerclient.serverConnectivity;
/**************************************************************
 * Class used to check if login is available when creating new user
 * and to distinguish type of user based on their id,
 * to prevent child login as aprent and vice-versa
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

import org.json.JSONException;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class LoginCheck {

    private boolean isLoginAvailable = false;
    private boolean isItParent = false;

    public boolean isLoginFree(String login){
        isLoginAvailable = false;
        String url = ServerController.getInstance().getIp()+"/api/contacts/checkLogin?username=" + login;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        //Enqueue - onResponse is executed as a call back once the 2nd thread is finished

        Log.v("MyLog","url:"+url);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MyLog", "Exception caught 0", e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.v("MyLog", "On response from Contact and Children Parer kids");
                try {
                    String jsonData = response.body().string();
                    Log.v("MyLog", "json data-"+jsonData);

                    //checks if the response was received successfully
                    if (response.isSuccessful()) {
                         if (Integer.parseInt(jsonData)==7){
                             isLoginAvailable = true;
                         }
                    } else {
                        Log.e("MyLog", "Exception caught 1 ");
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
        return isLoginAvailable;
    }
//--------------------------------------------------------------------------------------------------
    public boolean isItaParent(Long id){
        isItParent = false;
        String url = ServerController.getInstance().getIp()+"/api/contacts/parentOrChild?id=" + id;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        //Enqueue - onResponse is executed as a call back once the 2nd thread is finished

        Log.v("MyLog","url:"+url);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MyLog", "Exception caught 0", e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.v("MyLog", "On response from Contact and Children Parer kids");
                try {
                    String jsonData = response.body().string();
                    Log.v("MyLog", "json data-"+jsonData);

                    //checks if the response was received successfully
                    if (response.isSuccessful()) {
                        if (Integer.parseInt(jsonData)==5){
                            isItParent = true;
                        }
                    } else {
                        Log.e("MyLog", "Exception caught 1 ");
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
        Log.e("MyLog", "returning is it parent-"+isItParent);
        return isItParent;
    }
}
