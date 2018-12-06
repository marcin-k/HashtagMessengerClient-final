package com.marcin.hashtagmessengerclient.serverConnectivity;
/**************************************************************
 * Class used to update child object and to return a single
 * child object (for managing childs account)
 * Both of those functions are called from update child
 * activity therefore are grouped together
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.util.Log;

import com.marcin.hashtagmessengerclient.chat.Message;
import com.marcin.hashtagmessengerclient.contacts.Contact;
import com.marcin.hashtagmessengerclient.model.BaseUser;
import com.marcin.hashtagmessengerclient.model.ChildUser;
import com.marcin.hashtagmessengerclient.model.ParentUser;
import com.sendbird.android.shadow.okhttp3.Call;
import com.sendbird.android.shadow.okhttp3.Callback;
import com.sendbird.android.shadow.okhttp3.Credentials;
import com.sendbird.android.shadow.okhttp3.OkHttpClient;
import com.sendbird.android.shadow.okhttp3.Request;
import com.sendbird.android.shadow.okhttp3.RequestBody;
import com.sendbird.android.shadow.okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.marcin.hashtagmessengerclient.serverConnectivity.MessagesParser.JSON;
import static java.lang.Thread.sleep;

public class ChildUserParser {

    ChildUser childUser;
    protected String updatedChild;


//------------------------------- Update child user------------------------------------------------
    public String update(ChildUser child) {
        String url = ServerController.getInstance().getIp()+"/api/child/update?childId="
                +child.getId();

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
                        updatedChild = jsonData;
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
        return updatedChild;
    }
//---------------------------------- Get a Child with ID -------------------------------------------
    public ChildUser getChild(Long id){
            String url = ServerController.getInstance().getIp()+"/api/child/get?child=" + id;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", Credentials.basic(
                            ServerController.getInstance().getUsername(),
                            ServerController.getInstance().getPassword()))
                    .build();
            Call call = client.newCall(request);

            //Enqueue - onResponse is executed as a call back once the 2nd thread is finished
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("MyLog", "Exception caught 0", e);
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.v("MyLog", "On response from Contact and Children Parer contacts");
                    try {
                        String jsonData = response.body().string();
                        //checks if the response was received successfully
                        if (response.isSuccessful()) {
                            try {
                                if (jsonData.length() > 4) {
                                    childUser = (parseObject(jsonData));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("MyLog", "Exception caught 1 - Contact Parser");
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
            return childUser;
    }

//----------------------------------- Parse JSON Objects -------------------------------------------
    private ChildUser parseObject(String jsonData) throws JSONException {

        JSONObject data = new JSONObject(jsonData);
        ChildUser childUser = new ChildUser(data.getString("username"),
                data.getString("firstName"),
                data.getString("lastName"),
                data.getLong("id"),
                new ArrayList<BaseUser>(),
                new ArrayList<Message>(),
                "",
                new ArrayList<ParentUser>(),
                data.getBoolean("canAddNewContacts"),
                data.getBoolean("canBeFound"),
                data.getBoolean("canReceiveMessageFromNonConctact"),
                data.getInt("dailyAllowance"));
        return childUser;
    }
}
