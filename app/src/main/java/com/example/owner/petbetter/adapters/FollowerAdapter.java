package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.User;

import java.util.ArrayList;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

/**
 * Created by Kristian on 3/18/2018.
 */

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<User> followerList;

    public FollowerAdapter(Context context, ArrayList<User> followerList){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.followerList = followerList;
    }

    @Override
    public FollowerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_topic_followers, parent, false);
        FollowerViewHolder holder = new FollowerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FollowerViewHolder holder, int position) {
        final User thisFollower = followerList.get(position);
        holder.followerName.setText(thisFollower.getName());


        if(thisFollower.getUserPhoto()!=null){

            String newFileName = BASE_URL + thisFollower.getUserPhoto();
            System.out.println(newFileName);
            Glide.with(inflater.getContext()).load(newFileName).error(R.drawable.app_icon_yellow).into(holder.followerPicture);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            holder.followerPicture.setVisibility(View.VISIBLE);
            holder.followerPicture.setAdjustViewBounds(true);
        }

    }

    @Override
    public int getItemCount() {
        return followerList.size();
    }

    class FollowerViewHolder extends RecyclerView.ViewHolder{

        private TextView followerName;
        private ImageView followerPicture;

        public FollowerViewHolder(View itemView){
            super(itemView);

            followerName = (TextView) itemView.findViewById(R.id.followerName);
            followerPicture = (ImageView) itemView.findViewById(R.id.followerProfilePic);
        }
    }
}
