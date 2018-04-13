package com.example.owner.petbetter.adapters;

import android.content.Context;
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
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.classes.Veterinarian;
import com.example.owner.petbetter.database.DataAdapter;

import java.sql.SQLException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

/**
 * Created by owner on 24/3/2018.
 */

public class MonitorAdapter extends RecyclerView.Adapter<MonitorAdapter.MonitorViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<User> userList;
    private ArrayList<Facility> faciList;
    private ArrayList<Topic> topicList;
    private ArrayList<Post> postList;
    private int type;
    private OnItemClickListener listener;
    private DataAdapter petBetterDb;
    private Context context;
    private HerokuService service;

    public interface OnItemClickListener {
        void onItemClick(User item);
        void onItemClick(Facility item);
        void onItemClick(Topic item);
        void onItemClick(Post item);
    }

    public MonitorAdapter(Context context, ArrayList list, int type, OnItemClickListener listener){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.type = type;
        this.listener = listener;
        if(type==1){
            this.userList = list;
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
    public void onBindViewHolder(final MonitorViewHolder holder, int position) {
        initializeDatabase();
        if(type==1){
            //change to user
            final User thisUser = userList.get(position);
            holder.monitorName.setText(thisUser.getName());
            if(thisUser.getUserPhoto()!=null){
                String newFileName = BASE_URL + thisUser.getUserPhoto();
                System.out.println(newFileName);
                //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
                Glide.with(inflater.getContext()).load(newFileName).error(R.drawable.app_icon_yellow).into(holder.monitorProfilePic);
                holder.monitorProfilePic.setVisibility(View.VISIBLE);
            }
            if(thisUser.getIsDisabled()!=1){
                holder.banButton.setText("Disable");
                holder.banButton.setBackgroundResource(R.drawable.rounded_button_red);
            }
            else{
                holder.banButton.setText("Enable");
                holder.banButton.setBackgroundResource(R.drawable.rounded_button_green);
            }
            holder.banButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.banButton.getText().toString()=="Disable"){
                        setDisableUser(1, thisUser);
                    }
                    else{
                        setDisableUser(0, thisUser);
                    }
                }
            });
            holder.bind(thisUser, listener);
        }
        if(type==2){
            final Facility thisFaci = faciList.get(position);
            holder.monitorName.setText(thisFaci.getFaciName());
            if(thisFaci.getFaciPhoto()!=null){
                String newFileName = BASE_URL + thisFaci.getFaciPhoto();
                System.out.println(newFileName);
                //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
                Glide.with(inflater.getContext()).load(newFileName).error(R.drawable.app_icon_yellow).into(holder.monitorProfilePic);
                holder.monitorProfilePic.setVisibility(View.VISIBLE);
            }

            if(thisFaci.getIsDisabled()!=1){
                holder.banButton.setText("Disable");
                holder.banButton.setBackgroundResource(R.drawable.rounded_button_red);
            }
            else{
                holder.banButton.setText("Enable");
                holder.banButton.setBackgroundResource(R.drawable.rounded_button_green);
            }


            holder.banButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(holder.banButton.getText().toString()=="Disable"){
                        setDisableFacility(1, thisFaci);

                    }
                    else{
                        setDisableFacility(0, thisFaci);
                    }
                }
            });
            holder.bind(thisFaci, listener);
        }
        /*
        if(type==3){
            final Topic thisTopic = topicList.get(position);
            holder.monitorName.setText(thisTopic.getTopicName());
            holder.monitorProfilePic.setVisibility(View.GONE);

            if(thisTopic.getIsDeleted()!=1){
                holder.unbanButton.setEnabled(false);
                holder.unbanButton.setVisibility(View.INVISIBLE);
                holder.banButton.setVisibility(View.VISIBLE);
                holder.banButton.setEnabled(true);
            }
            else{
                holder.unbanButton.setEnabled(true);
                holder.unbanButton.setVisibility(View.VISIBLE);
                holder.banButton.setVisibility(View.INVISIBLE);
                holder.banButton.setEnabled(false);
            }

            holder.unbanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDisableTopic(0, thisTopic);
                }
            });
            holder.banButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDisableTopic(1, thisTopic);
                }
            });
            holder.bind(thisTopic, listener);
        }
        if(type==4){
            final Post thisPost = postList.get(position);
            holder.monitorName.setText(thisPost.getTopicName());
            holder.monitorProfilePic.setVisibility(View.GONE);

            if(thisPost.getIsDeleted()!=1){
                holder.unbanButton.setEnabled(false);
                holder.unbanButton.setVisibility(View.INVISIBLE);
                holder.banButton.setEnabled(true);
                holder.banButton.setVisibility(View.VISIBLE);
            }
            else{
                holder.unbanButton.setEnabled(true);
                holder.unbanButton.setVisibility(View.VISIBLE);
                holder.banButton.setVisibility(View.INVISIBLE);
                holder.banButton.setEnabled(false);
            }
            holder.unbanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDisablePost(0, thisPost);
                }
            });
            holder.banButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDisablePost(1, thisPost);
                }
            });
            holder.bind(thisPost, listener);
        }*/
    }

    public void updateList(ArrayList newList){
        if(type==1){
            userList.clear();
            userList.addAll(newList);
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

    public void setDisableTopic(int isDisabled, Topic thisTopic) {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<Void> call = service.disableTopic(isDisabled, thisTopic.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){


                    final Call<ArrayList<Topic>> call2 = service2.getAllTopics();
                    call2.enqueue(new Callback<ArrayList<Topic>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Topic>> call, Response<ArrayList<Topic>> response) {
                            if(response.isSuccessful()){
                                setTopics(response.body());
                                dataSynced(13);
                                updateList(response.body());


                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Topic>> call, Throwable t) {
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

    public void setDisablePost(int isDisabled, Post thisPost) {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<Void> call = service.disablePost(isDisabled, thisPost.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){


                    final Call<ArrayList<Post>> call2 = service2.getAllPosts();
                    call2.enqueue(new Callback<ArrayList<Post>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                            if(response.isSuccessful()){
                                setPosts(response.body());
                                dataSynced(9);
                                updateList(response.body());


                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
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

    public long setPosts(ArrayList<Post> postList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setPosts(postList);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setTopics(ArrayList<Topic> topicList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setTopics(topicList);
        petBetterDb.closeDatabase();

        return result;
    }

    public void setDisableUser(int isDisabled, User thisUser) {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<Void> call = service.disableUser(isDisabled, thisUser.getUserId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){


                    final Call<ArrayList<User>> call2 = service2.getUsers();
                    call2.enqueue(new Callback<ArrayList<User>>() {
                        @Override
                        public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                            if(response.isSuccessful()){
                                setUsers(response.body());
                                dataSynced(12);
                                updateList(response.body());


                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<User>> call, Throwable t) {
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

    public void setDisableFacility(int isDisabled, Facility facility) {

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);


        final Call<Void> call = service.disableFacility(isDisabled, facility.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){


                    final Call<ArrayList<Facility>> call2 = service2.getClinics(0);
                    call2.enqueue(new Callback<ArrayList<Facility>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Facility>> call, Response<ArrayList<Facility>> response) {
                            if(response.isSuccessful()){
                                setFacilities(response.body());
                                dataSynced(2);
                                updateList(response.body());


                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Facility>> call, Throwable t) {
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

    public long setFacilities(ArrayList<Facility> faciList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setFacilities(faciList);
        petBetterDb.closeDatabase();

        return result;
    }

    public long setUsers(ArrayList<User> userList){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        long result = petBetterDb.setUsers(userList);
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

    @Override
    public int getItemCount() {
        if(type==1){
            return userList.size();
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
        public void bind(final User item, final MonitorAdapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
        public void bind(final Facility item, final MonitorAdapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

        public void bind(final Post item, final MonitorAdapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

        public void bind(final Topic item, final MonitorAdapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
