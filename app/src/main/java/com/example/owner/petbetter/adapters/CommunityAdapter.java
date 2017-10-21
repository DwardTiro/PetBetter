package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;

import java.sql.SQLException;
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
    private User user;
    private Context context;
    private DataAdapter petBetterDb;
    private ArrayList<Post> topicPosts;

    public CommunityAdapter(Context context, ArrayList<Topic> topicList, User user, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.topicList = topicList;
        this.listener = listener;
        this.user = user;
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_community_item, parent, false);
        System.out.println("Are we even reaching this?");
        CommunityViewHolder holder = new CommunityViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(CommunityViewHolder holder, int position) {
        final Topic thisTopic = topicList.get(position);
        holder.topicName.setText(thisTopic.getTopicName());
        holder.topicDescription.setText(thisTopic.getTopicDesc());
        holder.topicUser.setText(thisTopic.getCreatorName());
        holder.textviewFollowers.setText(Integer.toString(thisTopic.getFollowerCount()));
        holder.bind(thisTopic, listener);

        if(user.getUserId()==thisTopic.getCreatorId()){
            holder.deleteTopicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initializeDatabase();
                    topicPosts = getTopicPosts(thisTopic.getId());
                    deleteTopic(thisTopic.getId());
                    for(int i=0;i<topicPosts.size();i++){
                        deletePost(topicPosts.get(i).getId());
                    }
                }
            });
        }
        else{
            holder.deleteTopicButton.setVisibility(View.INVISIBLE);
            holder.deleteTopicButton.setEnabled(false);
        }

    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(context);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    private long deleteTopic(long topicId){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.deleteTopic(topicId);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Post> getTopicPosts(long topicId){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Post> result = petBetterDb.getTopicPosts(topicId);
        petBetterDb.closeDatabase();

        return result;
    }

    private long deletePost(long postId){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.deletePost(postId);
        petBetterDb.closeDatabase();

        return result;
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
        private TextView textviewFollowers;
        private ImageButton deleteTopicButton;

        public CommunityViewHolder(View itemView) {
            super(itemView);

            topicName = (TextView) itemView.findViewById(R.id.topicComName);
            topicDescription = (TextView) itemView.findViewById(R.id.topicComDescription);
            topicUser = (TextView) itemView.findViewById(R.id.topicComUser);
            textviewFollowers = (TextView) itemView.findViewById(R.id.textViewFollowers);
            deleteTopicButton = (ImageButton) itemView.findViewById(R.id.deleteTopicButton);
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
