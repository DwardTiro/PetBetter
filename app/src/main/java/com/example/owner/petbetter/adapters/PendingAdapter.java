package com.example.owner.petbetter.adapters;

import android.app.Service;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Notifications;
import com.example.owner.petbetter.classes.Pending;
import com.example.owner.petbetter.classes.Services;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;
import com.google.android.gms.vision.text.Text;

import java.sql.SQLException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

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
    private DataAdapter petBetterDb;
    private Context context;
    private HerokuService service;

    public PendingAdapter(Context context, ArrayList<Pending> pendingList, OnItemClickListener listener){

        inflater = LayoutInflater.from(context);
        this.pendingList = pendingList;
        this.listener = listener;
        this.context = context;

    }
    @Override
    public PendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_pending_item,parent,false);
        PendingViewHolder holder = new PendingViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PendingViewHolder holder, final int position) {
        initializeDatabase();
        final Pending thisPending = pendingList.get(position);
        if(thisPending.getType()!=3){

            Veterinarian thisVet = getVeterinarianWithId(thisPending.getForeignId());
            holder.pendingName.setText(thisVet.getName());

            if(thisVet.getUserPhoto()!=null){
                String newFileName = BASE_URL + thisVet.getUserPhoto();
                System.out.println(newFileName);
                //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
                Glide.with(inflater.getContext()).load(newFileName).error(R.drawable.app_icon_yellow).into(holder.pendingProfilePic);
                holder.pendingProfilePic.setVisibility(View.VISIBLE);
            }
            if(thisPending.getType()==1){
                holder.pendingInfo.setText(thisVet.getEducation());
            }
            if(thisPending.getType()==2){
                if(thisVet.getIsLicensed()==1){
                    holder.pendingInfo.setText("Has license");
                }
                else{
                    holder.pendingInfo.setText("No license");
                }
            }
            if(thisPending.getType()==4){
                holder.pendingInfo.setText(thisVet.getSpecialty());
            }
        }
        if(thisPending.getType()==3){
            System.out.println("PENDING ID "+thisPending.getForeignId());
            Services thisService = getServiceWithId(thisPending.getForeignId());
            System.out.println("SERVICE ID "+thisService.getFaciId());
            Facility facility = getFacility(thisService.getFaciId());
            holder.pendingProfilePic.setVisibility(View.GONE);
            holder.pendingName.setText(facility.getFaciName());
            holder.pendingInfo.setText(thisService.getServiceName()+" - "+thisService.getServicePrice());
        }

        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIsApproved(1, thisPending);
                //pendingList.remove(position);
                //updateList(pendingList);
            }
        });

        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIsApproved(0, thisPending);
                //pendingList.remove(position);
                //updateList(pendingList);
            }
        });
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

    public void setIsApproved(int isApproved, final Pending thisPending) {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        final int type = thisPending.getType();
        final Call<Void> call = service.setIsApproved(isApproved, thisPending.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){


                    final Call<ArrayList<Pending>> call2 = service2.getPending();
                    call2.enqueue(new Callback<ArrayList<Pending>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Pending>> call, Response<ArrayList<Pending>> response) {
                            if(response.isSuccessful()){
                                setPending(response.body());
                                dataSynced(17);
                                ArrayList<Pending> newPendingList = getPending(type);

                                //find a way to refresh this and do search after.

                                updateList(newPendingList);
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Pending>> call, Throwable t) {
                            Log.d("onFailure", t.getLocalizedMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public long setPending(ArrayList<Pending> pendingList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setPending(pendingList);
        petBetterDb.closeDatabase();

        return result;
    }

    private void dataSynced(int n){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.dataSynced(n);
        petBetterDb.closeDatabase();

    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(context);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    private Veterinarian getVeterinarianWithId(long userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Veterinarian result = petBetterDb.getVeterinarianWithId(userId);
        petBetterDb.closeDatabase();

        return result;
    }

    private Services getServiceWithId(long id){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Services result = petBetterDb.getServiceWithId(id);
        petBetterDb.closeDatabase();

        return result;
    }

    private Facility getFacility(long faciId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Facility result = petBetterDb.getFacility((int) faciId);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Pending> getPending(int type){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Pending> result = petBetterDb.getPending(type);
        petBetterDb.closeDatabase();

        return result;
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
        private TextView pendingInfo;

        public PendingViewHolder(View itemView){
            super(itemView);

            pendingName = (TextView) itemView.findViewById(R.id.pendingName);
            pendingProfilePic = (ImageView) itemView.findViewById(R.id.pendingProfilePic);
            approveButton = (Button) itemView.findViewById(R.id.acceptButton);
            rejectButton = (Button) itemView.findViewById(R.id.rejectButton);
            pendingInfo = (TextView) itemView.findViewById(R.id.pendingInfo);

        }
    }
}
