package com.marcin.hashtagmessengerclient.serverConnectivity;
/**************************************************************
 * Class used to deal with all message related queries, that
 * include:
 *  - getting list of messages
 *  - deleting message
 *  - getting list of message to review (children's received messages)
 *  - getting list of SOS messages
 *  - sending message
 *  - approving the message for child to be able to see
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
import com.sendbird.android.shadow.okhttp3.MediaType;
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

import static java.lang.Thread.sleep;

public class MessagesParser {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String approveMsg = "";
    private String deleteMsg = "";

    //***********************************Approves msg***********************************************
    public String approve(Long id) {
        approveMsg = "";
        String url = ServerController.getInstance().getIp()+"/api/msg/approve?msgid=" + id;
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
                        approveMsg = jsonData;
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
        return approveMsg;
    }

    //******************************Deletes msgs completly******************************************
    public String deleteMsg(Long id) {
        deleteMsg = "";
        String url = ServerController.getInstance().getIp()+"/api/msg/delete?msgid=" + id;
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
                        deleteMsg = jsonData;
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
        return deleteMsg;
    }

//********************************Gets Messages to review*******************************************
public List<Message> getMessagesToReview(Long id) {
    ServerController.getInstance().msgToReview = new ArrayList<>();
    String url = ServerController.getInstance().getIp()+"/api/msg/kidsmsgs?userId=" + id;
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
                    try {
                        ServerController.getInstance().setMessagesToReview(parseSOSMessages(jsonData));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
    Log.v("MyLog","msgToReview size:"+ServerController.getInstance().msgToReview.size());
    return ServerController.getInstance().msgToReview;
}
    //***********************************Gets SOS msgs**********************************************
    public List<Message> getSOS(Long id) {
        String url = ServerController.getInstance().getIp()+"/api/msg/getSOS?userId=" + id;
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
                        try {
                            ServerController.getInstance().setSOS(parseSOSMessages(jsonData));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
            sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ServerController.getInstance().sos_msgs;
    }


    //*************************************Gets msgs************************************************
    public List<Message> getMessages(Long id) {
        String url = ServerController.getInstance().getIp()+"/api/msg/read?userId=" + id;
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
                        try {
                            ServerController.getInstance().setMessages(parseMessages(jsonData));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        ServerController.getInstance().instantiateMessages(id);
        return ServerController.getInstance().messages;
    }

    //***********************************Sends Message**********************************************
    public void sendMsg(Message msg) {
        String url = ServerController.getInstance().getIp()+"/api/msg/new";

        //sends msg
        //removes the new lines from string body
        String bodyWithoutNewLines = msg.getBody();
        bodyWithoutNewLines = bodyWithoutNewLines.replace("\n", " ").replace("\r", " ");


        String jsonToSend = "{ " +
                "\"id\": 0," +
                "\"senderId\": "+msg.getSenderId()+","+
                "\"recipientId\": "+ msg.getRecipientId()+","+
                "\"body\": \""+bodyWithoutNewLines+"\","+
                "\"approvedForChild\": "+msg.isApprovedForChild()+","+
                "\"flag\": "+msg.getFlag()+" }";

        Log.v("MyLog", msg.getBody());

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, jsonToSend);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", Credentials.basic(
                        ServerController.getInstance().getUsername(),
                        ServerController.getInstance().getPassword()))
                .build();
//        Response response;
//        try{
//            response = client.newCall(request).execute();
//            Log.v("MyLog", response.toString());
//        }catch(IOException e){
//            e.printStackTrace();
//        }
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
                            Log.v("MyLog", jsonData);
                    } else {
                        Log.e("MyLog", "Exception caught ");
                    }
                } catch (IOException e) {
                    Log.e("MyLog", "Exception caught ", e);
                }
            }
        });
    }




    //******************************* Parse JSON Contact *******************************************
    private ArrayList<Message> parseMessages(String jsonData) throws JSONException{
        JSONArray data = new JSONArray(jsonData);
        ArrayList<Message> messages = new ArrayList<>();
        for(int i = 0; i<data.length(); i++){
            JSONObject jsonMessage = data.getJSONObject(i);
//            Log.v("MyLog","MessageParser150: msg-"+i+jsonMessage);
            int mine = jsonMessage.getLong("senderId") ==
                    ServerController.getInstance().getId() ? Message.MSG_SENT : Message.MSG_RECEIVED;
            messages.add(new Message(
                    jsonMessage.getLong("senderId"),
                    jsonMessage.getLong("recipientId"),
                    jsonMessage.getString("body"),
                    jsonMessage.getBoolean("approvedForChild"),
                    jsonMessage.getInt("flag"),
                    mine));
        }
        Log.v("MyLog","MessageParser done!");
        return messages;
    }

    //******************************* Parse JSON Contact *******************************************
    private ArrayList<Message> parseSOSMessages(String jsonData) throws JSONException{
        JSONArray data = new JSONArray(jsonData);
        ArrayList<Message> messages = new ArrayList<>();
        for(int i = 0; i<data.length(); i++){
            JSONObject jsonMessage = data.getJSONObject(i);
//            Log.v("MyLog","MessageParser150: msg-"+i+jsonMessage);
            int mine = jsonMessage.getLong("senderId") ==
                    ServerController.getInstance().getId() ? Message.MSG_SENT : Message.MSG_RECEIVED;
            Message m = new Message(
                    jsonMessage.getLong("id"),
                    jsonMessage.getLong("senderId"),
                    jsonMessage.getLong("recipientId"),
                    jsonMessage.getString("body"),
                    jsonMessage.getBoolean("approvedForChild"),
                    jsonMessage.getInt("flag"),
                    mine);
            messages.add(m);
//            Log.v("MyLog","Message Prser id-"+m.getId());
        }
//        Log.v("MyLog","MessageParser done!");
        return messages;
    }
}
