package com.marcin.hashtagmessengerclient.chat;
/**************************************************************
 * Chat activity represents a screen when communication is exchanged
 * uses a custom adapter (Message adapter with multiply item layout)
 * for received and sent messages
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.marcin.hashtagmessengerclient.MyApplication;
import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.child.ChildContactsActivity;
import com.marcin.hashtagmessengerclient.login.LoginController;
import com.marcin.hashtagmessengerclient.parent.ParentContactsActivity;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnTouchListener{

    //button to send new message
    private View btnSend;
    //field for new message content
    private EditText editText;
    private MessageAdapter adapter;
    private ImageView backButton;
    //used to distinguish what activity back button should point to
    Long contactId;
    boolean isItParent;
    //used to stop additional thread that checks for new messages
    //in the background when activity goes to background
    private static boolean chatInBackground = true;

    public List<Message> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        contactId = getIntent().getExtras().getLong("contactId");
        isItParent = getIntent().getExtras().getBoolean("isItParent");

        btnSend = findViewById(R.id.btn_chat_send);
        editText = (EditText) findViewById(R.id.msg_type);

        backButton = findViewById(R.id.backButton);
        backButton.setOnTouchListener(this);


        //event for button SEND
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equals("")) {
                    Toast.makeText(ChatActivity.this, "Please input some text...",
                            Toast.LENGTH_SHORT).show();
                } else {
                    list.add(new Message(1l, 2l, editText.getText().toString(), true, 0, Message.MSG_SENT));
                    adapter.notifyDataSetChanged();
                    Message chatMessage = new Message(ServerController.getInstance().getId(),
                            contactId, editText.getText().toString(), true, 0, Message.MSG_SENT);
                    editText.setText("");
                    ServerController.getInstance().sendMsg(chatMessage);
                }
            }
        });
        //add all messages to the list
        try{
            for(Message m : ServerController.getInstance().getMessages(ServerController.getInstance()
                    .getId())){
                //TODO: Look up if this contact id is used as a sender or recipient

                if ((m.getSenderId() == ServerController.getInstance().getId() ||
                        m.getRecipientId() == ServerController.getInstance().getId())
                        &&((m.getSenderId() == contactId || m.getRecipientId() == contactId))){

                    int mine = m.getSenderId() == ServerController.getInstance().getId() ? Message.MSG_SENT : Message.MSG_RECEIVED;
                    Message chatMessage = new Message( m.getSenderId(),
                            m.getRecipientId(), m.getBody(), m.isApprovedForChild(), m.getFlag(), mine);
                    list.add(chatMessage);
                }
            }
        }
        catch (NullPointerException e){
            Log.v("MyLog","-------------------Null pointer-------------------------------");
            Toast.makeText(MyApplication.getAppContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //build the list
        adapter = new MessageAdapter(list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        //makes the initial focus on the last element
        linearLayoutManager.setStackFromEnd(true);
        //scrolls to last element when new element is added
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                linearLayoutManager.smoothScrollToPosition(mRecyclerView, null, adapter.getItemCount());
            }
        });
    }

//----------------------Back button OnClick---------------------------------------------------------
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.backButton:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent;
                    if(isItParent) {
                        intent = new Intent(MyApplication.getAppContext(), ParentContactsActivity.class);
//                        Toast.makeText(this, "going to parent screen",
//                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        intent = new Intent(MyApplication.getAppContext(), ChildContactsActivity.class);
//                        Toast.makeText(this, "going to child screen",
//                                Toast.LENGTH_LONG).show();
                    }
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        chatInBackground = false;

        //used to refresh chat every 10 seconds while chad is in foreground
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                Log.v("MyLog","-----------------------1---------------------------");
                //creates a new list and compare amount of elements if there are new msgs refresh view
                List<Message> tempList = new ArrayList<>();
                try{
                    for(Message m : ServerController.getInstance().getMessages(ServerController.getInstance()
                            .getId())){
                        Log.v("MyLog","msg"+m.getBody());
                        if ((m.getSenderId() == ServerController.getInstance().getId() ||
                                m.getRecipientId() == ServerController.getInstance().getId())
                                &&((m.getSenderId() == contactId || m.getRecipientId() == contactId))){

                            tempList.add(m);
                        }
                    }
//                    Log.v("MyLog","--------------------------------------------------");
//                    Log.v("MyLog","list size"+list.size());
//                    Log.v("MyLog","tempList size"+tempList.size());
                    if (list.size()<tempList.size()){
                        //
//                        Log.v("MyLog","-------------------2------------------------------");
                        for (int i = list.size(); i<tempList.size(); i++){
//                            Log.v("MyLog", "body of new-"+tempList.get(i).getBody());
                            list.add(tempList.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    }

                }
                catch (NullPointerException e){
                    Log.v("MyLog","-------------------Null pointer-------------------------------");
                    Toast.makeText(MyApplication.getAppContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
                if (!chatInBackground) {
                    handler.postDelayed(this, 10000);
                }
            }
        };
        handler.postDelayed(r, 10000);
    }

    //makes changes that allow to stop the extra thread then activity
    //goes to background
    @Override
    protected void onPause() {
        super.onPause();
        chatInBackground = true;
    }
}
