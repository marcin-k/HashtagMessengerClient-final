package com.marcin.hashtagmessengerclient.serverConnectivity;
/**************************************************************
 * Class used to create a new users child and parent users
 * and set user password. Password is not exposed attribute
 * on the spring boot server therefor requires additional
 * query to set
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.util.Log;

import com.marcin.hashtagmessengerclient.model.ChildUser;
import com.marcin.hashtagmessengerclient.model.ParentUser;
import com.sendbird.android.shadow.okhttp3.Call;
import com.sendbird.android.shadow.okhttp3.Callback;
import com.sendbird.android.shadow.okhttp3.Credentials;
import com.sendbird.android.shadow.okhttp3.OkHttpClient;
import com.sendbird.android.shadow.okhttp3.Request;
import com.sendbird.android.shadow.okhttp3.RequestBody;
import com.sendbird.android.shadow.okhttp3.Response;
import java.io.IOException;
import static com.marcin.hashtagmessengerclient.serverConnectivity.MessagesParser.JSON;
import static java.lang.Thread.sleep;

public class NewUserCreator {

    //-----------------------------Creates Parent user----------------------------------------------
    public static String userId = "";
    public String registerParent(ParentUser parent) {
        String url = ServerController.getInstance().getIp()+"/api/parent/new";
        //creating a json string to create an object, id is changed by Spring boot
        String jsonToSend = "{ " +
                "\"username\": \"" + parent.getUserName() + "\"," +
                "\"firstName\": \"" + parent.getFirstName() + "\"," +
                "\"lastName\": \"" + parent.getLastName() + "\"," +
                "\"id\": 0," +
                "\"contacts\": null," +
                "\"messages\": null," +
                "\"emailAddress\": \"" + parent.getEmailAddress() + "\"," +
                "\"children\": null }";

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, jsonToSend);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = client.newCall(request);

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
                        userId = jsonData;
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
        return userId;
    }

    //----------------------------- Creates child user----------------------------------------------
    public String newChild(ChildUser child) {
        String url = ServerController.getInstance().getIp()+"/api/child/new?parentId="
                +ServerController.getInstance().getId();

        String jsonToSend = "{ " +
                "\"username\": \"" + child.getUserName() + "\"," +
                "\"firstName\": \"" + child.getFirstName() + "\"," +
                "\"lastName\": \"" + child.getLastName() + "\"," +
                "\"id\": 0," +
                "\"canAddNewContacts\": "+child.isCanAddNewContacts()+","+
                "\"canBeFound\": "+child.isCanBeFound()+","+
                "\"canReceiveMessageFromNonConctact\": "+child.isCanReceiveMessageFromNonConctact()+","+
                "\"dailyAllowance\": "+child.getDailyAllowance()+","+
                "\"messages\": null," +
                "\"contacts\": null," +
                "\"requests\": null," +
                "\"parents\": null " +
                "}";

        Log.v("MyLog", jsonToSend);
        Log.v("MyLog", "url"+url);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, jsonToSend);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", Credentials.basic(
                        ServerController.getInstance().getUsername(),
                        ServerController.getInstance().getPassword()))
                .build();

        Call call = client.newCall(request);

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
                        userId = jsonData;
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
        return userId;
    }

    //-----------------------------sets password for a user---------------------------------------------
    static String pwSetting = "";
    public String setPassword(Long id, String password){

        String url = ServerController.getInstance().getIp()+"/api/contacts/pw?userId="+id+"&pw="+password;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
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
                        pwSetting = jsonData;
                    } else {
                        Log.e("MyLog", "Exception caught ");
                    }
                }catch (IOException e) {
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
        return pwSetting;
    }
}
