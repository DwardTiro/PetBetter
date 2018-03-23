package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.Veterinarian;

import java.util.ArrayList;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

/**
 * Created by owner on 24/3/2018.
 */

public class MonitorAdapter extends RecyclerView.Adapter<MonitorAdapter.MonitorViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Veterinarian> vetList;
    private ArrayList<Facility> faciList;
    private ArrayList<Topic> topicList;
    private ArrayList<Post> postList;
    private int type;

    public MonitorAdapter(Context context, ArrayList list, int type){
        inflater = LayoutInflater.from(context);
        this.type = type;
        if(type==1){
            this.vetList = list;
        }
        if(type==2){
            this.faciList = list;
        }
        if(type==3){
            this.topicList = list;
        }
        if(type==4){
            this.postList = list;
        }
    }

    @Override
    public MonitorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_monitor,parent,false);
        MonitorViewHolder holder = new MonitorViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MonitorViewHolder holder, int position) {
        if(type==1){
            Veterinarian thisVet = vetList.get(position);
            holder.monitorName.setText(thisVet.getName());
            if(thisVet.getUserPhoto()!=null){
                String newFileName = BASE_URL + thisVet.getUserPhoto();
                System.out.println(newFileName);
                //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
                Glide.with(inflater.getContext()).load(newFileName).error(R.drawable.app_icon_yellow).into(holder.monitorProfilePic);
                holder.monitorProfilePic.setVisibility(View.VISIBLE);
            }
            holder.banButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //remove vet
                }
            });
        }
        if(type==2){
            Facility thisFaci = faciList.get(position);
            holder.monitorName.setText(thisFaci.getFaciName());
            if(thisFaci.getFaciPhoto()!=null){
                String newFileName = BASE_URL + thisFaci.getFaciPhoto();
                System.out.println(newFileName);
                //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
                Glide.with(inflater.getContext()).load(newFileName).error(R.drawable.app_icon_yellow).into(holder.monitorProfilePic);
                holder.monitorProfilePic.setVisibility(View.VISIBLE);
            }
            holder.banButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //remove faci
                }
            });
        }
        if(type==3){
            Topic thisTopic = topicList.get(position);
            holder.monitorName.setText(thisTopic.getTopicName());
            holder.monitorProfilePic.setVisibility(View.GONE);
            holder.banButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //remove topic
                }
            });
        }
        if(type==4){
            Post thisPost = postList.get(position);
            holder.monitorName.setText(thisPost.getTopicName());
           holder.monitorProfilePic.setVisibility(View.GONE);
            holder.banButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //remove post
                }
            });
        }
    }

    public void updateList(ArrayList newList){
        if(type==1){
            vetList.clear();
            vetList.addAll(newList);
        }

        if(type==2){
            faciList.clear();
            faciList.addAll(newList);
        }

        if(type==3){
            topicList.clear();
            topicList.addAll(newList);
        }

        if(type==4){
            postList.clear();
            postList.addAll(newList);
        }

        this.notifyDataSetChanged();
        //recyclerView.setAdapter(this);
    }

    @Override
    public int getItemCount() {
        if(type==1){
            return vetList.size();
        }
        if(type==2){
            return faciList.size();
        }
        if(type==3){
            return topicList.size();
        }
        if(type==4){
            return postList.size();
        }
        else{
            return 0;
        }
    }

    class MonitorViewHolder extends RecyclerView.ViewHolder{

        private TextView monitorName;
        private ImageView monitorProfilePic;
        private Button banButton;

        public MonitorViewHolder(View itemView){
            super(itemView);

            monitorName = (TextView) itemView.findViewById(R.id.monitorName);
            monitorProfilePic = (ImageView) itemView.findViewById(R.id.monitorProfilePic);
            banButton = (Button) itemView.findViewById(R.id.banButton);

        }
    }
}
