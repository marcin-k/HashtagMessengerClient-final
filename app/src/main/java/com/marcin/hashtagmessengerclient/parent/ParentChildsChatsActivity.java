package com.marcin.hashtagmessengerclient.parent;
/**************************************************************
 * Activity used to preview childs conversations
 * its a shrink down version of Chat activity
 * without the option to input new messages
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.marcin.hashtagmessengerclient.MyApplication;
import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.chat.ChatActivity;
import com.marcin.hashtagmessengerclient.chat.Message;
import com.marcin.hashtagmessengerclient.chat.MessageAdapter;
import com.marcin.hashtagmessengerclient.child.ChildContactsActivity;
import com.marcin.hashtagmessengerclient.login.LoginController;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

import java.util.ArrayList;
import java.util.List;

public class ParentChildsChatsActivity extends AppCompatActivity implements View.OnTouchListener{

    private MessageAdapter adapter;
    private ImageView backButton;
    Long contactId;
    Long childsId;
    private static boolean chatInBackground2 = true;
    public List<Message> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_childs_chats);

        contactId = getIntent().getExtras().getLong("contactId");
        childsId = getIntent().getExtras().getLong("childsId");
        backButton = findViewById(R.id.backButton);
        backButton.setOnTouchListener(this);

        //------------------------------------------------------------------------------------------
        try{
            for(Message m : ServerController.getInstance().getMessages(childsId)){
                if ((m.getSenderId() == childsId ||
                        m.getRecipientId() == childsId)
                        &&((m.getSenderId() == contactId || m.getRecipientId() == contactId))){

                    int mine = m.getSenderId() == childsId ? Message.MSG_SENT : Message.MSG_RECEIVED;
                    Message chatMessage = new Message( m.getSenderId(),
                            m.getRecipientId(), m.getBody(), m.isApprovedForChild(), m.getFlag(), mine);
                    list.add(chatMessage);
                }
            }
        }
        catch (NullPointerException e){
            Toast.makeText(MyApplication.getAppContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

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

    //----------------------OnClick--------------------------------------------
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.backButton:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), ParentChildsContactsActivity.class);
                    intent.putExtra("childsId", childsId);
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatInBackground2 = false;

        //used to refresh chat every 10 seconds while chad is in foreground
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                Log.v("MyLog","-----------------------1---------------------------");
                //creates a new list and compare amount of elements if there are new msgs refresh view
                List<Message> tempList = new ArrayList<>();
                try{
                    for(Message m : ServerController.getInstance().getMessages(childsId)){
                        if ((m.getSenderId() == childsId ||
                                m.getRecipientId() == childsId)
                                &&((m.getSenderId() == contactId || m.getRecipientId() == contactId))){

                            tempList.add(m);
                        }
                    }
                    Log.v("MyLog","--------------------------------------------------");
                    Log.v("MyLog","list size"+list.size());
                    Log.v("MyLog","tempList size"+tempList.size());
                    if (list.size()<tempList.size()){
                        //
                        Log.v("MyLog","-------------------2------------------------------");
                        for (int i = list.size(); i<tempList.size(); i++){
                            Log.v("MyLog", "body of new-"+tempList.get(i).getBody());
                            list.add(tempList.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    }

                }
                catch (NullPointerException e){
                    Toast.makeText(MyApplication.getAppContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
                if (!chatInBackground2) {
                    handler.postDelayed(this, 10000);
                }
            }
        };
        handler.postDelayed(r, 10000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatInBackground2 = true;
    }
}
