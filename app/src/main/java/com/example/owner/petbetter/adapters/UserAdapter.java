package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

/**
 * Created by owner on 15/8/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(User item);
    }

    private LayoutInflater inflater;
    private ArrayList<User> userList;
    private final OnItemClickListener listener;

    public UserAdapter(Context context, ArrayList<User> userList, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.userList = userList;
        this.listener = listener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_vet_item, parent, false);
        System.out.println("We here mate kek wew");
        UserViewHolder holder = new UserViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User thisUser = userList.get(position);
        holder.userName.setText(thisUser.getName());


        if(thisUser.getUserPhoto()!=null){

            String newFileName = BASE_URL + thisUser.getUserPhoto();
            System.out.println("USER PHOTO WHERE "+newFileName);
            Glide.with(inflater.getContext()).load(newFileName).error(R.drawable.app_icon_yellow).into(holder.userImage);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            holder.userImage.setVisibility(View.VISIBLE);
            holder.userImage.setAdjustViewBounds(true);
        }


        holder.bind(thisUser, listener);
    }

    @Override
    public int getItemCount() {

        return (userList.size() > 0 ? userList.size() : 1);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        private ImageView userImage;
        private TextView userName;
        private TextView vetListSpecialty;
        private TextView vetListRating;

        public UserViewHolder(View itemView) {
            super(itemView);
            userImage = (ImageView) itemView.findViewById(R.id.vetListImage);
            userName = (TextView) itemView.findViewById(R.id.vetListName);
            vetListSpecialty = (TextView) itemView.findViewById(R.id.vetListSpecialty);
            vetListRating = (TextView) itemView.findViewById(R.id.vetListRating);
            vetListRating.setVisibility(View.GONE);
            vetListSpecialty.setVisibility(View.GONE);
        }

        public void bind(final User item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
