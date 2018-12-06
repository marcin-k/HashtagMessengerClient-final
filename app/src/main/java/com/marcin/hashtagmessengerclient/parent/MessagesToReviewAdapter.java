package com.marcin.hashtagmessengerclient.parent;
/**************************************************************
 * Custom adapter for storing list messages to review
 * in the parent home activity
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.cache.CacheController;
import com.marcin.hashtagmessengerclient.chat.Message;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

import java.util.List;

public class MessagesToReviewAdapter extends ArrayAdapter<Message> {
    private Activity activity;
    private List<Message> soslist;

    public MessagesToReviewAdapter(Activity context, int resource, List<Message> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.soslist = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessagesToReviewAdapter.ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int layoutResource = 0; // determined by view type
        Message chatMessage = getItem(position);
        int viewType = getItemViewType(position);
        layoutResource = R.layout.msg_to_review;

        if (convertView != null) {
            holder = (MessagesToReviewAdapter.ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new MessagesToReviewAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        }

        //set message content
//        ServerController.getInstance().getChildren(ServerController.getInstance().getId());
        holder.child.setText("Message to: "+CacheController.getInstance().getFirstName(chatMessage.getRecipientId()));
        return convertView;
    }

    private class ViewHolder {
        private TextView child;


        public ViewHolder(View v) {
            child = (TextView) v.findViewById(R.id.childMsgToReviewTV);
        }
    }
}
