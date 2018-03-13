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

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.PostRep;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.interfaces.PlaceInfoListener;

import java.sql.SQLException;
import java.util.ArrayList;

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
    private PlaceInfoListener placeInfoListener;

    HerokuService service;


    public HomeAdapter(Context context, ArrayList<Post> postList, User user, OnItemClickListener listener, PlaceInfoListener placeInfoListener) {
        inflater = LayoutInflater.from(context);
        this.postList = postList;
        this.listener = listener;
        this.context = context;
        this.user = user;
        this.placeInfoListener = placeInfoListener;
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
        initializeDatabase();
        holder.topicName.setText(thisPost.getTopicName());
        holder.topicDescription.setText(thisPost.getTopicContent());
        holder.topicUser.setText(thisPost.getTopicUser());
        holder.replyCounter.setText(Integer.toString(getPostReps(thisPost.getId()).size()));
        int result = getVoteCount(thisPost.getId(), 1);
        holder.upvoteCounter.setText(String.valueOf(result));
        holder.upvoteCounter.setVisibility(View.INVISIBLE);
        holder.upvotePostButton.setVisibility(View.INVISIBLE);
        holder.downvotePostButton.setVisibility(View.INVISIBLE);


        holder.bind(thisPost, listener);



        if(user.getUserId()==thisPost.getUserId()){
            holder.optionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    placeInfoListener.onPopupMenuClicked(view, position);
                }
            });
            /*
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
            });*/
        }
        else{
            holder.deletePostButton.setVisibility(View.INVISIBLE);
            holder.optionsButton.setVisibility(View.INVISIBLE);
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

    private int getVoteCount(long postId, int type){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        int result = petBetterDb.getVoteCount((int) postId, type);
        petBetterDb.closeDatabase();

        return result;
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

    public void updateList(ArrayList<Post> newList){
        postList.clear();
        postList.addAll(newList);
        this.notifyDataSetChanged();
        //recyclerView.setAdapter(this);
    }

    public ArrayList<PostRep> getPostReps(long postId){
        try{
            petBetterDb.openDatabase();
        } catch (SQLException e){
            e.printStackTrace();
        }

        ArrayList<PostRep> results = petBetterDb.getPostReps(postId);

        petBetterDb.closeDatabase();
        return results;
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
        private ImageButton upvotePostButton;
        private ImageButton downvotePostButton;
        private TextView upvoteCounter;
        private ImageButton optionsButton;
        private TextView replyCounter;

        public HomeViewHolder(View itemView) {
            super(itemView);

            topicName = (TextView) itemView.findViewById(R.id.topicName);
            topicDescription = (TextView) itemView.findViewById(R.id.topicDescription);
            topicUser = (TextView) itemView.findViewById(R.id.topicUser);
            deletePostButton = (ImageButton) itemView.findViewById(R.id.deletePostButton);
            upvotePostButton = (ImageButton) itemView.findViewById(R.id.upvotePostButton);
            downvotePostButton = (ImageButton) itemView.findViewById(R.id.downvotePostButton);
            upvoteCounter = (TextView) itemView.findViewById(R.id.upvoteCounter);
            optionsButton = (ImageButton) itemView.findViewById(R.id.postOptionsButton);
            replyCounter = (TextView) itemView.findViewById(R.id.textViewReplies);
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
