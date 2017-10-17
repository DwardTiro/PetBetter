package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Notifications;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Kristian on 10/13/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(Notifications item);
    }

    private LayoutInflater inflater;
    private ArrayList<Notifications> notifs;
    private final OnItemClickListener listener;

    public NotificationsAdapter(Context context, ArrayList<Notifications> notifs, OnItemClickListener listener){

        inflater = LayoutInflater.from(context);
        this.notifs = notifs;
        this.listener = listener;

    }
    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_notification,parent,false);
        NotificationsViewHolder holder = new NotificationsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NotificationsViewHolder holder, int position) {
        //(size-1)-position;
        Notifications notif = notifs.get((notifs.size()-1)-position);
        holder.notifProfileName.setText(notif.getDoerName());
        if(notif.getIsRead()==0){
            holder.notifPostTitle.setTypeface(null, Typeface.BOLD);
        }

        if(notif.getType()==1){
            holder.notifPostTitle.setText("has replied to your post.");
            //get group name;
        }
        if(notif.getType()==2){
            holder.notifPostTitle.setText("has messaged you.");
        }

        holder.notifTimeStamp.setText("");
        holder.bind(notif, listener);

    }

    @Override
    public int getItemCount() {
        return notifs.size();
    }

    class NotificationsViewHolder extends RecyclerView.ViewHolder{

        private ImageView notifProfilePic;
        private TextView notifProfileName;
        private TextView notifPostTitle;
        private TextView notifTimeStamp;
        public NotificationsViewHolder(View itemView) {
            super(itemView);

            notifProfilePic = (ImageView) itemView.findViewById(R.id.notifProfilePic);
            notifProfileName = (TextView) itemView.findViewById(R.id.notifProfileName);
            notifPostTitle = (TextView) itemView.findViewById(R.id.notifContent);
            notifTimeStamp = (TextView) itemView.findViewById(R.id.notifTimeStamp);
        }
        public void bind(final Notifications item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
