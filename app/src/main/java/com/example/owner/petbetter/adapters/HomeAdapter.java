package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Marker;
import com.example.owner.petbetter.classes.Post;

import java.util.ArrayList;

/**
 * Created by owner on 2/10/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(Post item);
    }

    private LayoutInflater inflater;
    private ArrayList<Post> postList;
    private final OnItemClickListener listener;

    public HomeAdapter(Context context, ArrayList<Post> postList, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.postList = postList;
        this.listener = listener;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_home_item, parent, false);
        System.out.println("Are we even reaching this?");
        HomeViewHolder holder = new HomeViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        Post thisPost = postList.get(position);

        holder.topicName.setText(thisPost.getTopicName());
        holder.topicDescription.setText(thisPost.getTopicContent());
        holder.topicUser.setText(thisPost.getTopicUser());
        holder.bind(thisPost, listener);
        /*
        holder.bookmarkListName.setText(thisBookmark.getBldgName());
        System.out.println("Bldg name is: " +holder.bookmarkListName.getText());
        holder.bookmarkListAddress.setText(thisBookmark.getLocation());
        */
    }

    public void update(ArrayList<Post> list){
        postList.clear();
        postList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    class HomeViewHolder extends RecyclerView.ViewHolder{

        private TextView topicName;
        private TextView topicDescription;
        private TextView topicUser;

        public HomeViewHolder(View itemView) {
            super(itemView);

            topicName = (TextView) itemView.findViewById(R.id.topicName);
            topicDescription = (TextView) itemView.findViewById(R.id.topicDescription);
            topicUser = (TextView) itemView.findViewById(R.id.topicUser);
        }

        public void bind(final Post item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
