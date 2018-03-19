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
import com.example.owner.petbetter.classes.Message;
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

public class MessageRequestAdapter extends RecyclerView.Adapter<MessageRequestAdapter.MessageRequestViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Message item);
    }

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Message> messageList;
    private final OnItemClickListener listener;
    private DataAdapter petBetterDb;

    public MessageRequestAdapter(Context context, ArrayList<Message> messageList, OnItemClickListener listener){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.messageList = messageList;
        this.listener = listener;
    }

    @Override
    public MessageRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_follower, parent, false);
        MessageRequestViewHolder holder = new MessageRequestViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MessageRequestViewHolder holder, final int position) {
        initializeDatabase();
        final Message thisMessage = messageList.get(position);
        User user = getUserWithId((int) thisMessage.getUserId());
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

        if(thisMessage.getIsAllowed() == 1){
            holder.acceptButton.setVisibility(View.INVISIBLE);
            holder.rejectButton.setVisibility(View.INVISIBLE);

        }
        else {
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    syncMessageChanges(thisMessage);
                    approveMessage(thisMessage.getId());
                    messageList.remove(position);
                    notifyDataSetChanged();
                    //updateList(getPendingMessages(thisMessage.getUserId()));
                }
            });

            holder.rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    removeMessage(thisMessage.getId());
                    deleteMessage(thisMessage.getId());
                    messageList.remove(position);
                    notifyDataSetChanged();
                    //updateList(getPendingMessages(thisMessage.getUserId()));
                }
            });
        }
        //get user and set UI based on it.
    }

    public void removeMessage(long messageId){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

        final Call<Void> call = service.deleteMessage(messageId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    dataSynced(5);

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
    }

    private ArrayList<Message> getPendingMessages(long userId){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Message> result = petBetterDb.getPendingMessages(userId);
        petBetterDb.closeDatabase();

        return result;
    }

    public void syncMessageChanges(Message message){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOO");


        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(message);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());

        final Call<Void> call = service.editMessage(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    dataSynced(5);


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

    private void deleteMessage(long id){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.deleteMessage(id);
        petBetterDb.closeDatabase();

    }

    private void approveMessage(long id){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        petBetterDb.approveMessage(id);
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

    public void updateList(ArrayList<Message> newList){
        messageList.clear();
        messageList.addAll(newList);
        this.notifyDataSetChanged();
        //recyclerView.setAdapter(this);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    class MessageRequestViewHolder extends RecyclerView.ViewHolder{

        private TextView followerName;
        private ImageView followerProfilePic;
        private Button acceptButton;
        private Button rejectButton;

        public MessageRequestViewHolder(View itemView){
            super(itemView);

            followerName = (TextView) itemView.findViewById(R.id.followerName);
            followerProfilePic = (ImageView) itemView.findViewById(R.id.followerProfilePic);
            acceptButton = (Button) itemView.findViewById(R.id.acceptButton);
            rejectButton = (Button) itemView.findViewById(R.id.rejectButton);

        }
    }
}
