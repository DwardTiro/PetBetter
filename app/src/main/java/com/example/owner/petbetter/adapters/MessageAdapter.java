package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Marker;
import com.example.owner.petbetter.classes.Message;
import com.example.owner.petbetter.classes.Post;

import java.util.ArrayList;

/**
 * Created by owner on 2/10/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<Message> messageList;

    public MessageAdapter(Context context, ArrayList<Message> messageList) {
        inflater = LayoutInflater.from(context);
        this.messageList = messageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_messages_item, parent, false);
        //inflate the message ITEM layout.
        System.out.println("Are we even reaching this?");
        MessageViewHolder holder = new MessageViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message thisMessage = messageList.get(position);

        holder.messageSender.setText(thisMessage.getFromName());
        holder.messageContent.setText(thisMessage.getMessageContent());
        //set messageContent dude
        /*
        holder.bookmarkListName.setText(thisBookmark.getBldgName());
        System.out.println("Bldg name is: " +holder.bookmarkListName.getText());
        holder.bookmarkListAddress.setText(thisBookmark.getLocation());
        */
    }

    public void update(ArrayList<Message> list){
        messageList.clear();
        messageList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{

        private TextView messageSender;
        private TextView messageContent;

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageSender = (TextView) itemView.findViewById(R.id.messageSender);
            messageContent = (TextView) itemView.findViewById(R.id.messageContent);
        }
    }
}
