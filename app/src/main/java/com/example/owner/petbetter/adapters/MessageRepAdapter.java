package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.MessageRep;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

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
    private RecyclerView recyclerView;
    private DataAdapter petBetterDb;
    private Context context;

    public MessageRepAdapter(Context context, ArrayList<MessageRep> messageRepList, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.messageRepList = messageRepList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public MessageRepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_messagerep_item, parent, false);
        System.out.println("Are we even reaching this?");
        MessageRepViewHolder holder = new MessageRepViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final MessageRepViewHolder holder, int position) {
        //User user = getUserWithId(thisMessageRep.getSenderId());
        initializeDatabase();
        MessageRep thisMessageRep = messageRepList.get(position);
        holder.messageRepName.setText(thisMessageRep.getUserName());
        holder.messageRepTime.setText(thisMessageRep.getDatePerformed());
        holder.messageRepContent.setText(thisMessageRep.getRepContent());
        //holder.messageRepImage.setImageResource(R.drawable.app_icon);

        //you might need to refresh your messagerep list since message_photo is a base 64 pre-upload and a filename otw back
        //you probably just need to load the image from the specified url


        if(thisMessageRep.getMessagePhoto()!=null){

            String newFileName = BASE_URL + thisMessageRep.getMessagePhoto();

            //System.out.println("FILE NAME PO "+newFileName);
            //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
            Glide.with(inflater.getContext()).load(newFileName).error(R.drawable.back_button).into(holder.messageRepImage);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            holder.messageRepImage.setVisibility(View.VISIBLE);
        }
        else{
            holder.messageRepImage.setVisibility(View.GONE);
        }

        User user = getUserWithId(thisMessageRep.getSenderId());

        if(user.getUserPhoto()!=null){

            String newFileName = BASE_URL + user.getUserPhoto();
            System.out.println(newFileName);
            //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
            Glide.with(inflater.getContext()).load(newFileName).error(R.drawable.back_button).into(holder.messageRepProfile);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            holder.messageRepProfile.setVisibility(View.VISIBLE);
        }


        holder.bind(thisMessageRep, listener);

    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(context);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    private User getUserWithId(long id) {

        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getUserWithId((int) id);
        petBetterDb.closeDatabase();


        return result;
    }

    public void updateList(ArrayList<MessageRep> newList){
        messageRepList.clear();
        messageRepList.addAll(newList);
        this.notifyDataSetChanged();
        //recyclerView.setAdapter(this);
    }

    @Override
    public int getItemCount() {
        return messageRepList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }



    class MessageRepViewHolder extends RecyclerView.ViewHolder{

        private ImageView messageRepImage;
        private TextView messageRepName;
        private TextView messageRepContent;
        private TextView messageRepTime;
        private ImageView messageRepProfile;
        private ImageButton deletePostRep;

        public MessageRepViewHolder(View itemView) {
            super(itemView);

            messageRepProfile = (ImageView) itemView.findViewById(R.id.commentUserProfilePicture);
            messageRepName = (TextView) itemView.findViewById(R.id.commentUserProfileName);
            messageRepContent = (TextView) itemView.findViewById(R.id.commentContent);
            messageRepTime = (TextView) itemView.findViewById(R.id.commentTimeStamp);
            messageRepImage = (ImageView) itemView.findViewById(R.id.imageRep);

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
