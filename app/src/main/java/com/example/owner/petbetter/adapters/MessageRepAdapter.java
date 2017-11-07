package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Marker;
import com.example.owner.petbetter.classes.MessageRep;

import java.util.ArrayList;

/**
 * Created by owner on 15/10/2017.
 */

public class MessageRepAdapter extends RecyclerView.Adapter<MessageRepAdapter.MessageRepViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(MessageRep item);
    }

    private LayoutInflater inflater;
    private ArrayList<MessageRep> messageRepList;
    private final OnItemClickListener listener;

    public MessageRepAdapter(Context context, ArrayList<MessageRep> messageRepList, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.messageRepList = messageRepList;
        this.listener = listener;
    }

    @Override
    public MessageRepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_messagerep_item, parent, false);
        System.out.println("Are we even reaching this?");
        MessageRepViewHolder holder = new MessageRepViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(MessageRepViewHolder holder, int position) {
        MessageRep thisMessageRep = messageRepList.get(position);
        holder.messageRepName.setText(thisMessageRep.getUserName());
        holder.messageRepTime.setText(thisMessageRep.getDatePerformed());
        holder.messageRepContent.setText(thisMessageRep.getRepContent());
        holder.bind(thisMessageRep, listener);

    }

    @Override
    public int getItemCount() {
        return messageRepList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }



    class MessageRepViewHolder extends RecyclerView.ViewHolder{

        private ImageView messageRepImage;
        private TextView messageRepName;
        private TextView messageRepContent;
        private TextView messageRepTime;
        private ImageButton deletePostRep;

        public MessageRepViewHolder(View itemView) {
            super(itemView);

            messageRepImage = (ImageView) itemView.findViewById(R.id.commentUserProfilePicture);
            messageRepName = (TextView) itemView.findViewById(R.id.commentUserProfileName);
            messageRepContent = (TextView) itemView.findViewById(R.id.commentContent);
            messageRepTime = (TextView) itemView.findViewById(R.id.commentTimeStamp);
        }

        public void bind(final MessageRep item, final MessageRepAdapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
