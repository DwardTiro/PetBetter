package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.PostRep;

import java.util.ArrayList;

/**
 * Created by owner on 14/10/2017.
 */

public class PostRepAdapter extends RecyclerView.Adapter<PostRepAdapter.PostRepViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(PostRep item);
    }

    private LayoutInflater inflater;
    private ArrayList<PostRep> postRepList;
    private final OnItemClickListener listener;

    public PostRepAdapter(Context context, ArrayList<PostRep> postRepList, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.postRepList = postRepList;
        this.listener = listener;
    }

    @Override
    public PostRepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_post_content_item, parent, false);
        System.out.println("We here mate kek wew");
        PostRepViewHolder holder = new PostRepViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(PostRepViewHolder holder, int position) {
        PostRep thisComment = postRepList.get(position);

        holder.postRepName.setText(thisComment.getUserName());
        holder.postRepTime.setText(thisComment.getDatePerformed());
        holder.postRepContent.setText(thisComment.getRepContent());
        holder.bind(thisComment, listener);
        /*
        Veterinarian thisVet = vetList.get(position);
        holder.vetListName.setText(thisVet.getName());
        System.out.println("The vet's name is "+holder.vetListName.getText());
        holder.vetListSpecialty.setText(thisVet.getSpecialty());
        holder.vetListRating.setText(Integer.toString(thisVet.getRating()));
        holder.bind(thisVet, listener);
        */
    }

    @Override
    public int getItemCount() {

        return (postRepList.size() > 0 ? postRepList.size() : 1);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    class PostRepViewHolder extends RecyclerView.ViewHolder{

        //edit this later
        private ImageView postRepImage;
        private TextView postRepName;
        private TextView postRepContent;
        private TextView postRepTime;

        public PostRepViewHolder(View itemView) {
            super(itemView);

            postRepImage = (ImageView) itemView.findViewById(R.id.commentUserProfilePicture);
            postRepName = (TextView) itemView.findViewById(R.id.commentUserProfileName);
            postRepContent = (TextView) itemView.findViewById(R.id.commentContent);
            postRepTime = (TextView) itemView.findViewById(R.id.commentTimeStamp);
        }

        public void bind(final PostRep item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
