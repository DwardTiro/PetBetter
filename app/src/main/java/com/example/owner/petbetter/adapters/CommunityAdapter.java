package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Topic;

import java.util.ArrayList;

/**
 * Created by owner on 2/10/2017.
 */

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Topic item);
    }

    private LayoutInflater inflater;
    private ArrayList<Topic> topicList;
    private final OnItemClickListener listener;

    public CommunityAdapter(Context context, ArrayList<Topic> topicList, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.topicList = topicList;
        this.listener = listener;
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_home_item, parent, false);
        System.out.println("Are we even reaching this?");
        CommunityViewHolder holder = new CommunityViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(CommunityViewHolder holder, int position) {
        Topic thisTopic = topicList.get(position);
        holder.topicName.setText(thisTopic.getTopicName());
        holder.topicDescription.setText(thisTopic.getTopicDesc());
        holder.topicUser.setText(thisTopic.getCreatorName());
        holder.bind(thisTopic, listener);

    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }



    class CommunityViewHolder extends RecyclerView.ViewHolder{

        private TextView topicName;
        private TextView topicDescription;
        private TextView topicUser;

        public CommunityViewHolder(View itemView) {
            super(itemView);

            topicName = (TextView) itemView.findViewById(R.id.topicName);
            topicDescription = (TextView) itemView.findViewById(R.id.topicDescription);
            topicUser = (TextView) itemView.findViewById(R.id.topicUser);
        }

        public void bind(final Topic item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
