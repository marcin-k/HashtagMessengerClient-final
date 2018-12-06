package com.marcin.hashtagmessengerclient.parent;
/**************************************************************
 * Parent home page, shows the list of sos alerts and list of
 * messages to review, using two custom adapters, for performance
 * purposes that activity loads after extra welcome screen when
 * loaded for the first time (that allows cache to contact server
 * and cache users first names - used in both of those lists)
 * Structure of this class is very similar to contact list except
 * for use of alerts to dismiss the sos alert (cancel alert on the server)
 * or approve (update message on the server) disapprove (delete message
 * on the server) of potentially inappropriate content
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.chat.Message;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

import java.util.ArrayList;
import java.util.List;

public class ParentHomeActivity extends AppCompatActivity {

    private TextView noMsgsTV;
    private TextView noSOSTV;
    public static String toBeReturned = "";

    private ImageView contacts;
    private ImageView home;
    private ImageView mykids;
    private ImageView settings;

    //    sos_msgs
    private ListView listView;
    private List<Message> sos_msgs;
    private ArrayAdapter<Message> adapter;

    // msgs to approve
    private ListView messageToApproveLV;
    private List<Message> msgsToApprove;
    private ArrayAdapter<Message> msgToApproveAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);

        //buttons
        contacts = findViewById(R.id.contacts);
        home = findViewById(R.id.home);
        mykids = findViewById(R.id.mykids);
        settings = findViewById(R.id.settings);

        contacts.setOnTouchListener(_ParentNavigation.getInstance());
        home.setOnTouchListener(_ParentNavigation.getInstance());
        mykids.setOnTouchListener(_ParentNavigation.getInstance());
        settings.setOnTouchListener(_ParentNavigation.getInstance());

        ServerController.getInstance().getChildren(ServerController.getInstance().getId());
        noMsgsTV = findViewById(R.id.noMsgsTV);
        messageToApproveLV = (ListView) findViewById(R.id.msg_to_approve);
        msgsToApprove = new ArrayList<>();
        //set ListView adapter first
        msgToApproveAdapter = new MessagesToReviewAdapter(this, R.layout.msg_to_review, msgsToApprove);
        messageToApproveLV.setAdapter(msgToApproveAdapter);

        for(Message m : ServerController.getInstance().getMessagesToReview(ServerController.getInstance()
                .getId())){

            Message chatMessage = new Message(m.getId(), m.getSenderId(),
                    m.getRecipientId(), m.getBody(), m.isApprovedForChild(), m.getFlag(), m.getType());
            msgsToApprove.add(chatMessage);
        }
        msgToApproveAdapter.notifyDataSetChanged();

        //on click prompt to mark the sos as reviewed
        messageToApproveLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("MyLog","msg id:"+msgsToApprove.get(position).getId());
                showAlertForReview(msgsToApprove.get(position).getId(), position,
                        msgsToApprove.get(position).getBody());
            }
        });

        if (msgsToApprove.isEmpty()){
            noMsgsTV.setText("There is no suspicious messages for you to review");
        }
        else{
            noMsgsTV.setText("");
        }

        noSOSTV = findViewById(R.id.noSOSTV);
        listView = (ListView) findViewById(R.id.sos_msgs);
        sos_msgs = new ArrayList<>();
        //set ListView adapter first
        adapter = new SOSAdapter(this, R.layout.sos, sos_msgs);
        listView.setAdapter(adapter);

        for(Message m : ServerController.getInstance().getSOS(ServerController.getInstance()
                .getId())){

            Message chatMessage = new Message(m.getId(), m.getSenderId(),
                    m.getRecipientId(), m.getBody(), m.isApprovedForChild(), m.getFlag(), m.getType());
            sos_msgs.add(chatMessage);
        }
        adapter.notifyDataSetChanged();

        //on click prompt to mark the sos as reviewed
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("MyLog","msg id:"+ sos_msgs.get(position).getId());
                showAlertForSOS(sos_msgs.get(position).getId(), position);
            }
        });

        if (sos_msgs.isEmpty()){
            noSOSTV.setText("There is no SOS messages received");
        }
        else{
            noSOSTV.setText("");
        }

    }

    public void showAlertForReview(final Long messageId, final int position, String body){
        new AlertDialog.Builder(this)
                .setTitle("Please review the following message")
                .setMessage(body)
                .setPositiveButton("Its inappropriate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Prompt the user once explanation has been shown
                        Log.v("MyLog","delete msg "+messageId);
                        ServerController.getInstance().deleteMsg(messageId);
                        msgsToApprove.remove(position);
                        msgToApproveAdapter.notifyDataSetChanged();
                        if (msgsToApprove.isEmpty()){
                            noMsgsTV.setText("There is no suspicious messages for you to review");
                        }
                        else{
                            noMsgsTV.setText("");
                        }
                    }
                })
                .setNegativeButton("Its ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Prompt the user once explanation has been shown
                        Log.v("MyLog","keep");
                        ServerController.getInstance().approveMsg(messageId);
                        msgsToApprove.remove(position);
                        msgToApproveAdapter.notifyDataSetChanged();
                        if (msgsToApprove.isEmpty()){
                            noMsgsTV.setText("There is no suspicious messages for you to review");
                        }
                        else{
                            noMsgsTV.setText("");
                        }
                    }
                })
                .create()
                .show();
    }

    public void showAlertForSOS(final Long messageId, final int position){

        new AlertDialog.Builder(this)
                .setTitle("Would you like to mark that alert as read?")
                .setMessage("Message will disappear from the list")
                .setPositiveButton("I understand", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Prompt the user once explanation has been shown
                        Log.v("MyLog","delete msg "+messageId);
                        ServerController.getInstance().deleteMsg(messageId);
                        sos_msgs.remove(position);
                        adapter.notifyDataSetChanged();
                        if (sos_msgs.isEmpty()){
                            noSOSTV.setText("There is no SOS messages received");
                        }
                        else{
                            noSOSTV.setText("");
                        }
                    }
                })
                .setNegativeButton("No lets keep it for now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Prompt the user once explanation has been shown
                        Log.v("MyLog","keep");
                    }
                })
                .create()
                .show();
    }

}

