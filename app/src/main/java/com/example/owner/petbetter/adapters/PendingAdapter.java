package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.Pending;

import java.util.ArrayList;

/**
 * Created by owner on 23/3/2018.
 */

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.PendingViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Pending item);
    }

    private LayoutInflater inflater;
    private ArrayList<Pending> pendingList;
    private final OnItemClickListener listener;

    public PendingAdapter(Context context, ArrayList<Pending> pendingList, OnItemClickListener listener){

        inflater = LayoutInflater.from(context);
        this.pendingList = pendingList;
        this.listener = listener;

    }
    @Override
    public PendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_follower,parent,false);
        PendingViewHolder holder = new PendingViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PendingViewHolder holder, int position) {

        /*
        initializeDatabase();
        final Follower thisFollower = followerList.get(position);
        User user = getUserWithId((int) thisFollower.getUserId());
        holder.followerName.setText(user.getName());
        if(user.getUserPhoto()!=null){

            String newFileName = BASE_URL + user.getUserPhoto();
            System.out.println(newFileName);
            //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
            Glide.with(inflater.getContext()).load(newFileName).error(R.drawable.app_icon_yellow).into(holder.followerProfilePic);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);//
            //setImage(holder.messageRepImage, newFileName);

            holder.followerProfilePic.setVisibility(View.VISIBLE);
        }

        if(thisFollower.getIsAllowed() == 1){
            holder.acceptButton.setVisibility(View.GONE);
            holder.rejectButton.setVisibility(View.GONE);

        }
        else {
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    approveRequest(thisFollower.getId());
                    syncFollowerChanges(thisFollower.getId());
                    updateList(getPendingFollowers(thisFollower.getTopicId()));
                }
            });

            holder.rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteFollower((int) thisFollower.getTopicId(), (int) thisFollower.getUserId());
                    removeFollower(thisFollower.getTopicId(), thisFollower.getUserId());
                    updateList(getPendingFollowers(thisFollower.getTopicId()));
                }
            });
        }
        */
    }

    public void updateList(ArrayList<Pending> newList){
        pendingList.clear();
        pendingList.addAll(newList);
        this.notifyDataSetChanged();
        //recyclerView.setAdapter(this);
    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    class PendingViewHolder extends RecyclerView.ViewHolder{

        private TextView pendingName;
        private ImageView pendingProfilePic;
        private Button approveButton;
        private Button rejectButton;

        public PendingViewHolder(View itemView){
            super(itemView);

            pendingName = (TextView) itemView.findViewById(R.id.followerName);
            pendingProfilePic = (ImageView) itemView.findViewById(R.id.followerProfilePic);
            approveButton = (Button) itemView.findViewById(R.id.acceptButton);
            rejectButton = (Button) itemView.findViewById(R.id.rejectButton);

        }
    }
}
