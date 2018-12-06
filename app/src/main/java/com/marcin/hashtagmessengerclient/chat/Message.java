package com.marcin.hashtagmessengerclient.chat;
/**************************************************************
 * Class represents a message object (single communication)
 * used in the chat activity
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.util.Log;

import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

public class Message {
    //class variables used to distinguish the received message from sent
    public static final int MSG_SENT = 0;
    public static final int MSG_RECEIVED = 1;
//--------------------------Instance variables------------------------------------------------------
    private Long id;
    private boolean isMine;
    private Long senderId;
    private Long recipientId;
    private String body;
    private boolean approvedForChild;
    private int flag;
    private int mType;
//--------------------------Constructors------------------------------------------------------------
    public Message( Long senderId, Long recipientId, String body,
                    boolean approvedForChild, int flag, int type) {
        this.id = 0l;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.body = body;
        this.approvedForChild = approvedForChild;
        this.flag = flag;

        if (senderId == ServerController.getInstance().getId()){
            isMine = true;
        }
        else{
            isMine = false;
        }
        mType = type;
    }

    public Message(Long id, Long senderId, Long recipientId, String body,
                    boolean approvedForChild, int flag, int type) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.body = body;
        this.approvedForChild = approvedForChild;
        this.flag = flag;
        if (senderId == ServerController.getInstance().getId()){
            isMine = true;
        }
        else{
            isMine = false;
        }
        mType = type;
    }

    public Message(String content, boolean isMine) {
        this.body = content;
        isMine = isMine;
    }
//-------------------------Mutator methods----------------------------------------------------------
    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isApprovedForChild() {
        return approvedForChild;
    }

    public void setApprovedForChild(boolean approvedForChild) {
        this.approvedForChild = approvedForChild;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getType() {
        return mType;
    }

    public Long getId() {
        return id;
    }
}
