package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Follower;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

/**
 * Created by Kristian on 3/18/2018.
 */

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Follower item);
    }

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Follower> followerList;
    private final OnItemClickListener listener;
    private DataAdapter petBetterDb;

    public FollowerAdapter(Context context, ArrayList<Follower> followerList, OnItemClickListener listener){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.followerList = followerList;
        this.listener = listener;
    }

    @Override
    public FollowerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_follower, parent, false);
        FollowerViewHolder holder = new FollowerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FollowerViewHolder holder, int position) {
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
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            holder.followerProfilePic.setVisibility(View.VISIBLE);
        }

        if(thisFollower.getIsAllowed() == 1){
            holder.acceptButton.setVisibility(View.INVISIBLE);
            holder.rejectButton.setVisibility(View.INVISIBLE);

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
        //get user and set UI based on it.
    }

    public void removeFollower(final long topicId, long userId){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        final Call<Void> call = service.deleteFollower(topicId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    dataSynced(3);

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public void syncFollowerChanges(final long topicId){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOO");

        Follower follower = getFollowerWithId(topicId);

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(follower);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

        final Call<Void> call = service.editFollowers(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("Followers EDITED YEY");
                    dataSynced(3);


                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    private ArrayList<Follower> getPendingFollowers(long topicId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Follower> result = petBetterDb.getPendingFollowers(topicId);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setFollowers(ArrayList<Follower> followerList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setFollowers(followerList);
        petBetterDb.closeDatabase();

        return result;
    }

    public Follower getFollowerWithId(long topicId){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        Follower result = petBetterDb.getFollowerWithId(topicId);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Follower> getUnsyncedFollowers(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Follower> result = petBetterDb.getUnsyncedFollowers();
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

    private void deleteFollower(int topicId, int userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.deleteFollower(topicId, userId);
        petBetterDb.closeDatabase();

    }

    private void approveRequest(long id){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.approveRequest(id);
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

    private User getUserWithId(int id){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getUserWithId(id);
        petBetterDb.closeDatabase();

        return result;
    }

    public void updateList(ArrayList<Follower> newList){
        followerList.clear();
        followerList.addAll(newList);
        this.notifyDataSetChanged();
        //recyclerView.setAdapter(this);
    }

    @Override
    public int getItemCount() {
        return followerList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    class FollowerViewHolder extends RecyclerView.ViewHolder{

        private TextView followerName;
        private ImageView followerProfilePic;
        private Button acceptButton;
        private Button rejectButton;

        public FollowerViewHolder(View itemView){
            super(itemView);

            followerName = (TextView) itemView.findViewById(R.id.followerName);
            followerProfilePic = (ImageView) itemView.findViewById(R.id.followerProfilePic);
            acceptButton = (Button) itemView.findViewById(R.id.acceptButton);
            rejectButton = (Button) itemView.findViewById(R.id.rejectButton);

        }
    }
}
