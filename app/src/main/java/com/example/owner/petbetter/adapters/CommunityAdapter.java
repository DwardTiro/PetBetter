package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.View.MeasureSpec;
import android.view.Gravity;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.interfaces.PlaceInfoListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by owner on 2/10/2017.
 */

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> implements PlaceInfoListener {

    public interface OnItemClickListener {
        void onItemClick(Topic item);
    }

    private LayoutInflater inflater;
    private ArrayList<Topic> topicList;
    private final OnItemClickListener listener;
    private User user;
    private Context context;
    private DataAdapter petBetterDb;
    private ArrayList<Post> topicPosts;
    private PopupWindow popUpConfirmationWindow;
    private PlaceInfoListener placeInfoListener;

    public CommunityAdapter(Context context, ArrayList<Topic> topicList, User user, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.topicList = topicList;
        this.listener = listener;
        this.user = user;
        this.placeInfoListener = this;
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_community_item, parent, false);
        System.out.println("Are we even reaching this?");
        CommunityViewHolder holder = new CommunityViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final CommunityViewHolder holder, final int position) {
        final Topic thisTopic = topicList.get(position);
        initializeDatabase();
        User topicUser = getUserWithId(thisTopic.getCreatorId());

        int topicPosts = getTopicPosts(thisTopic.getId()).size();
        holder.topicName.setText(thisTopic.getTopicName());
        holder.topicDescription.setText(thisTopic.getTopicDesc());
        if(topicUser!=null && topicUser.getUserType() == 1){
            holder.topicUser.setText("Dr. "+thisTopic.getCreatorName()+", DVM.");
            holder.userIdentifier.setImageResource(R.drawable.ic_local_hospital_black_18dp);
            //holder.topicUser.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_circle_black_18dp,0);
            //holder.topicUser.setCompoundDrawablePadding(5);
        }else if(topicUser != null && topicUser.getUserType() == 4){
            holder.topicUser.setText(thisTopic.getCreatorName());
            holder.userIdentifier.setImageResource(R.drawable.ic_business_center_black_18dp);
        }
        else if(topicUser !=null){
            holder.topicUser.setText(thisTopic.getCreatorName());
            holder.userIdentifier.setVisibility(View.GONE);
        }
        else {
            holder.topicUser.setVisibility(View.GONE);
            holder.userIdentifier.setVisibility(View.GONE);
        }
        holder.textviewFollowers.setText(Integer.toString(thisTopic.getFollowerCount()));
        holder.textViewPosts.setText(Integer.toString(topicPosts));

        holder.bind(thisTopic, listener);

        if (user.getUserId() == thisTopic.getCreatorId()) {
            holder.optionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    placeInfoListener.onPopupMenuClicked(view, position);
                }
            });
            /*
            holder.deleteTopicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popUpConfirmation = inflater.inflate(R.layout.popup_confirmation_delete_topic, null);


                    popUpConfirmation.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));


                    popUpConfirmationWindow = new PopupWindow(popUpConfirmation, 750, 360, true);
                    popUpConfirmationWindow.showAtLocation(popUpConfirmation, Gravity.CENTER, 0, 0);

                    Button cancelButton = (Button) popUpConfirmationWindow.getContentView().findViewById(R.id.popUpTopicCancelButton);

                    Button deleteButton = (Button) popUpConfirmationWindow.getContentView().findViewById(R.id.popUpTopicDeleteButton);

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
                            topicPosts = getTopicPosts(thisTopic.getId());
                            deleteTopic(thisTopic.getId());
                            for (int i = 0; i < topicPosts.size(); i++) {
                                deletePost(topicPosts.get(i).getId());
                            }
                            update(position);
                            popUpConfirmationWindow.dismiss();
                        }
                    });


                }
            });*/
        }else {
            //holder.deleteTopicButton.setVisibility(View.INVISIBLE);
            //holder.deleteTopicButton.setEnabled(false);
            holder.optionsButton.setVisibility(View.INVISIBLE);
        }

    }

    public void update(int position) {
        topicList.remove(position);
        this.notifyDataSetChanged();
    }

    public void updateList(ArrayList<Topic> newList){
        topicList.clear();
        topicList.addAll(newList);
        this.notifyDataSetChanged();
        //recyclerView.setAdapter(this);
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(context);

        try {
            petBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private long deleteTopic(long topicId) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.deleteTopic(topicId);
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Post> getTopicPosts(long topicId) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Post> result = petBetterDb.getTopicPosts(topicId);
        petBetterDb.closeDatabase();

        return result;
    }

    private long deletePost(long postId) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.deletePost(postId);
        petBetterDb.closeDatabase();

        return result;
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
    public User getUserWithId(long _id){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        User result = petBetterDb.getUserWithId((int)_id);
        petBetterDb.closeDatabase();

        return result;
    }



    public void deletePosts(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<Post> unsyncedPosts = getUnsyncedPosts();

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedPosts);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.deletePosts(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    dataSynced(9);

                    final Call<ArrayList<Post>> call2 = service2.getPosts();
                    call2.enqueue(new Callback<ArrayList<Post>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                            if(response.isSuccessful()){
                                setPosts(response.body());
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


    private ArrayList<Topic> getUnsyncedTopics(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Topic> result = petBetterDb.getUnsyncedTopics();
        petBetterDb.closeDatabase();

        return result;
    }

    private ArrayList<Post> getUnsyncedPosts(){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Post> result = petBetterDb.getUnsyncedPosts();
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



    public void deleteTopics(){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        System.out.println("WE HERE BOOIII");
        ArrayList<Topic> unsyncedTopics = getUnsyncedTopics();

        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonArray = gson.toJson(unsyncedTopics);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
        final Call<Void> call = service.deleteTopics(body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    dataSynced(13);

                    final Call<ArrayList<Topic>> call2 = service2.getTopics();
                    call2.enqueue(new Callback<ArrayList<Topic>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Topic>> call, Response<ArrayList<Topic>> response) {
                            if(response.isSuccessful()){
                                setTopics(response.body());
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


    @Override
    public void onPopupMenuClicked(final View view, final int pos) {
        final Topic thisTopic = topicList.get(pos);
        PopupMenu options = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = options.getMenuInflater();
        inflater.inflate(R.menu.post_topic_menu, options.getMenu());
        System.out.println("Options menu clicked");
        options.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_delete_post_topic:
                        LayoutInflater layoutInflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View popUpConfirmation = layoutInflater.inflate(R.layout.popup_confirmation_delete_topic, null);

                        popUpConfirmation.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));


                        popUpConfirmationWindow = new PopupWindow(popUpConfirmation, 750, 360, true);
                        popUpConfirmationWindow.showAtLocation(popUpConfirmation, Gravity.CENTER, 0, 0);

                        Button cancelButton = (Button) popUpConfirmationWindow.getContentView().findViewById(R.id.popUpTopicCancelButton);

                        Button deleteButton = (Button) popUpConfirmationWindow.getContentView().findViewById(R.id.popUpTopicDeleteButton);

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
                                topicPosts = getTopicPosts(thisTopic.getId());
                                deleteTopic(thisTopic.getId());
                                deleteTopics();
                                for (int i = 0; i < topicPosts.size(); i++) {
                                    //deletePost(topicPosts.get(i).getId());
                                }
                                update(pos);
                                deletePosts();
                                popUpConfirmationWindow.dismiss();

                            }
                        });
                        return true;

                    default: return false;
                }

            }
        });

        options.show();

    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class CommunityViewHolder extends RecyclerView.ViewHolder {

        private TextView topicName;
        private TextView topicDescription;
        private TextView topicUser;
        private TextView textviewFollowers;
        private TextView textViewPosts;
        private ImageButton deleteTopicButton;
        private ImageButton optionsButton;
        private ImageView userIdentifier;

        public CommunityViewHolder(View itemView) {
            super(itemView);

            topicName = (TextView) itemView.findViewById(R.id.topicComName);
            topicDescription = (TextView) itemView.findViewById(R.id.topicComDescription);
            topicUser = (TextView) itemView.findViewById(R.id.topicComUser);
            textviewFollowers = (TextView) itemView.findViewById(R.id.textViewFollowers);
            //deleteTopicButton = (ImageButton) itemView.findViewById(R.id.deleteTopicButton);
            optionsButton = (ImageButton) itemView.findViewById(R.id.topicOptionsButton);
            textViewPosts = (TextView) itemView.findViewById(R.id.textViewPosts);
            userIdentifier = (ImageView) itemView.findViewById(R.id.iconIdentifier);
        }

        public void bind(final Topic item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
