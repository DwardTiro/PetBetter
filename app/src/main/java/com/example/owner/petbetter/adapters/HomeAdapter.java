package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.activities.EditProfileActivity;
import com.example.owner.petbetter.classes.Marker;
import com.example.owner.petbetter.classes.Post;
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

/**
 * Created by owner on 2/10/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(Post item);
    }

    private LayoutInflater inflater;
    private ArrayList<Post> postList;
    private final OnItemClickListener listener;
    private User user;
    private DataAdapter petBetterDb;
    private Context context;
    private PopupWindow popUpConfirmationWindow;

    HerokuService service;


    public HomeAdapter(Context context, ArrayList<Post> postList, User user, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.postList = postList;
        this.listener = listener;
        this.context = context;
        this.user = user;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_home_item, parent, false);
        System.out.println("Are we even reaching this?");
        HomeViewHolder holder = new HomeViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, final int position) {
        final Post thisPost = postList.get(position);

        holder.topicName.setText(thisPost.getTopicName());
        holder.topicDescription.setText(thisPost.getTopicContent());
        holder.topicUser.setText(thisPost.getTopicUser());
        holder.bind(thisPost, listener);

        if(user.getUserId()==thisPost.getUserId()){
            holder.deletePostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popUpConfirmation = inflater.inflate(R.layout.popup_confirmation_delete_post, null);

                    popUpConfirmation.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                    popUpConfirmationWindow = new PopupWindow(popUpConfirmation, 750, 360, true);
                    popUpConfirmationWindow.showAtLocation(popUpConfirmation, Gravity.CENTER, 0, 0);

                    Button cancelButton = (Button) popUpConfirmationWindow.getContentView().findViewById(R.id.popUpPostCancelButton);

                    Button deleteButton = (Button) popUpConfirmationWindow.getContentView().findViewById(R.id.popUpPostDeleteButton);
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popUpConfirmationWindow.dismiss();
                        }
                    });
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            initializeDatabase();
                            deletePost(thisPost.getId());

                            service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
                            final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                            final Call<Void> call = service.deletePost(thisPost.getUserId(), thisPost.getDateCreated());
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    //User thisUser = response.body();
                                    if(response.isSuccessful()){
                                        dataSynced(9);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.d("onFailure", t.getLocalizedMessage());
                                }
                            });

                            update(position);
                            popUpConfirmationWindow.dismiss();
                        }
                    });

                }
            });
        }
        else{
            holder.deletePostButton.setVisibility(View.INVISIBLE);
            holder.deletePostButton.setEnabled(false);
        }
        /*
        holder.bookmarkListName.setText(thisBookmark.getBldgName());
        System.out.println("Bldg name is: " +holder.bookmarkListName.getText());
        holder.bookmarkListAddress.setText(thisBookmark.getLocation());
        */
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(context);

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    private long deletePost(long postId){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.deletePost(postId);
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

    public void update(int position){
        postList.remove(position);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    class HomeViewHolder extends RecyclerView.ViewHolder{

        private TextView topicName;
        private TextView topicDescription;
        private TextView topicUser;
        private ImageButton deletePostButton;

        public HomeViewHolder(View itemView) {
            super(itemView);

            topicName = (TextView) itemView.findViewById(R.id.topicName);
            topicDescription = (TextView) itemView.findViewById(R.id.topicDescription);
            topicUser = (TextView) itemView.findViewById(R.id.topicUser);
            deletePostButton = (ImageButton) itemView.findViewById(R.id.deletePostButton);
        }

        public void bind(final Post item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
