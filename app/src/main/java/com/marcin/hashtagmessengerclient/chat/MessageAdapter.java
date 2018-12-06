package com.marcin.hashtagmessengerclient.chat;

/**************************************************************
 * Custom list view adapter for chat view,
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.marcin.hashtagmessengerclient.R;

import java.util.List;

import static com.marcin.hashtagmessengerclient.chat.Message.MSG_RECEIVED;
import static com.marcin.hashtagmessengerclient.chat.Message.MSG_SENT;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> mList;

    public MessageAdapter(List<Message> list) {
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case MSG_SENT :
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
                return new SentMsg(view);
            case MSG_RECEIVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
                return new ReceivedMsg(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message object = mList.get(position);
        if (object != null) {
            switch (object.getType()) {
                case MSG_SENT:
                    ((SentMsg) holder).mTitle.setText(object.getBody());
                    break;
                case MSG_RECEIVED:
                    ((ReceivedMsg) holder).mTitle.setText(object.getBody());
                    ((ReceivedMsg) holder).mDescription.setText("description");
                    break;
            }
        }
    }

    //number of elements in the list
    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }

    //type of view (received msg / sent msg)
    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            Message object = mList.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
    }

    //message send
    public static class SentMsg extends RecyclerView.ViewHolder {
        private TextView mTitle;

        public SentMsg(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.titleTextView);
        }
    }

    //message received
    public static class ReceivedMsg extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDescription;

        public ReceivedMsg(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.titleTextView);
            mDescription = (TextView) itemView.findViewById(R.id.descriptionTextView);
        }
    }
}