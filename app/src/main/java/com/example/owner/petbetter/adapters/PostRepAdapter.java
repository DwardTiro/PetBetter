package com.example.owner.petbetter.adapters;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
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

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.MessageRep;
import com.example.owner.petbetter.classes.PostRep;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.interfaces.PlaceInfoListener;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;


/**
 * Created by owner on 14/10/2017.
 */

public class PostRepAdapter extends RecyclerView.Adapter<PostRepAdapter.PostRepViewHolder> implements PlaceInfoListener {


    public interface OnItemClickListener {
        void onItemClick(PostRep item);
    }

    private LayoutInflater inflater;
    private ArrayList<PostRep> postRepList;
    private final OnItemClickListener listener;
    private User user;
    private DataAdapter petBetterDb;
    private Context context;
    private PopupWindow popUpConfirmationWindow;
    private ArrayList<PostRep> postReps;
    private int type;
    private PlaceInfoListener placeInfoListener;
    final private int VIEW_TYPE_EMPTYLIST_PLACEHOLDER = 0;
    final private int VIEW_TYPE_RECYCLERVIEW = 1;
    HerokuService service;


    public PostRepAdapter(Context context, ArrayList<PostRep> postRepList, User user, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.postRepList = postRepList;
        this.listener = listener;
        this.user = user;
        this.type = type;
        this.placeInfoListener = this;
    }
/*
    @Override
    public int getItemViewType(int position) {
        if (postRepList.isEmpty()) {
            return VIEW_TYPE_EMPTYLIST_PLACEHOLDER;
        } else if (!postRepList.isEmpty()) {
            return VIEW_TYPE_RECYCLERVIEW;
        }
        return 0;

    }
*/

    @Override
    public PostRepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_home_listing_comment, parent, false);
        return new PostRepViewHolder(view);

    }

    @Override
    public void onBindViewHolder(PostRepViewHolder holder, final int position) {

        //if (postRepList.isEmpty()) {


        //}
        final PostRep thisComment = postRepList.get(position);

        initializeDatabase();
        final User user = getUserWithId(thisComment.getUserId());
        if(user.getUserType() == 1){
            holder.postRepName.setText("Dr. "+thisComment.getUserName()+", DVM.");
        }else
            holder.postRepName.setText(thisComment.getUserName());
        holder.postRepTime.setText(thisComment.getDatePerformed());
        holder.postRepContent.setText(thisComment.getRepContent());
        holder.bind(thisComment, listener);
        holder.postRepTime.setVisibility(View.GONE);

        postReps = getPostRepsFromParent(thisComment.getId());

        if(postReps.size() == 0){
            holder.sentenceEnd.setVisibility(View.GONE);
            holder.postRepCounter.setVisibility(View.GONE);
            holder.sentenceStart.setText("No replies yet");
        }
        else if(postRepList.size() == 1){
            holder.sentenceStart.setText("View");
            holder.sentenceEnd.setText("reply");
            holder.postRepCounter.setVisibility(View.VISIBLE);
            holder.sentenceEnd.setVisibility(View.VISIBLE);
            holder.postRepCounter.setText(postRepList.size());
        }
        else if(postRepList.size() > 1){
            holder.sentenceStart.setText("View");
            holder.sentenceEnd.setText("replies");
            holder.postRepCounter.setVisibility(View.VISIBLE);
            holder.sentenceEnd.setVisibility(View.VISIBLE);
            holder.postRepCounter.setText(postRepList.size());
        }

        if(user.getUserType() == 1){
            holder.postRepName.setText("Dr. "+user.getName()+", DVM.");
            holder.userIdentifier.setImageResource(R.drawable.ic_local_hospital_black_18dp);
            holder.userIdentifier.setVisibility(View.VISIBLE);
            holder.postRepName.setVisibility(View.VISIBLE);
            holder.postRepImage.setVisibility(View.VISIBLE);
            //holder.topicUser.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_circle_black_18dp,0);
            //holder.topicUser.setCompoundDrawablePadding(5);
        }else if(user.getUserType() == 4){
            holder.postRepName.setText(user.getName());
            holder.postRepName.setVisibility(View.VISIBLE);
            holder.userIdentifier.setImageResource(R.drawable.ic_business_center_black_18dp);
            holder.userIdentifier.setVisibility(View.VISIBLE);
            holder.postRepImage.setVisibility(View.VISIBLE);
        }else if(user.getUserType() == 3){
            holder.postRepName.setVisibility(View.GONE);
            holder.userIdentifier.setVisibility(View.GONE);
            holder.postRepImage.setVisibility(View.GONE);
        }
        else {
            holder.postRepName.setText(user.getName());
            holder.postRepName.setVisibility(View.VISIBLE);
            holder.userIdentifier.setVisibility(View.GONE);
            holder.postRepImage.setVisibility(View.VISIBLE);
        }

        if(user.getUserPhoto()!=null){

            String newFileName = BASE_URL + user.getUserPhoto();
            System.out.println(newFileName);
            //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
            Glide.with(inflater.getContext()).load(newFileName).error(R.drawable.petbetter_logo_final).into(holder.postRepImage);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            holder.postRepImage.setVisibility(View.VISIBLE);
            holder.postRepImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(inflater.getContext(), com.example.owner.petbetter.activities.UserProfileActivity.class);
                    intent.putExtra("UserProfile", user.getUserId());
                    inflater.getContext().startActivity(intent);
                }
            });
        }

        if (user.getUserId() == thisComment.getUserId()) {
            holder.postRepOptionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    placeInfoListener.onPopupMenuClicked(v, position);
                }
            });
            /*
            holder.deletePostRepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // RelativeLayout mainLayout = (RelativeLayout) v.findViewById(R.id.postContentLayout);
                    //inflate a popup confirmation window before deleting

                    LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popUpConfirmation = inflater.inflate(R.layout.popup_confirmation_delete_comment, null);

                    popUpConfirmation.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

                    //int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

                    popUpConfirmationWindow = new PopupWindow(popUpConfirmation, 750, 360, true);
                    popUpConfirmationWindow.showAtLocation(popUpConfirmation, Gravity.CENTER, 0, 0);

                    Button cancelButton = (Button) popUpConfirmationWindow.getContentView().findViewById(R.id.popUpCancelButton);

                    Button deleteButton = (Button) popUpConfirmationWindow.getContentView().findViewById(R.id.popUpDeleteButton);
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
                            deletePostRep(thisComment.getId());
                            service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
                            final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                            final Call<Void> call = service.deletePostRep(thisComment.getUserId(), thisComment.getDatePerformed());
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    //User thisUser = response.body();
                                    if(response.isSuccessful()){
                                        dataSynced(10);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.d("onFailure", t.getLocalizedMessage());
                                }
                            });

                            popUpConfirmationWindow.dismiss();
                            updateData(position);
                        }
                    });

//                    initializeDatabase();

                    //                  deletePostRep(thisComment.getId());


                }
            });*/
        } else {
            //holder.deletePostRepButton.setVisibility(View.INVISIBLE);
            //holder.deletePostRepButton.setEnabled(false);
            holder.postRepOptionsButton.setVisibility(View.INVISIBLE);
        }
        /*
        Veterinarian thisVet = vetList.get(position);
        holder.vetListName.setText(thisVet.getName());
        System.out.println("The vet's name is "+holder.vetListName.getText());
        holder.vetListSpecialty.setText(thisVet.getSpecialty());
        holder.vetListRating.setText(Integer.toString(thisVet.getRating()));
        holder.bind(thisVet, listener);
        */

    }

    private void updateData(int position) {
        postRepList.remove(position);
        this.notifyDataSetChanged();
    }

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(context);

        try {
            petBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    private long deletePostRep(long postRepId) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long result = petBetterDb.deletePostRep(postRepId);
        petBetterDb.closeDatabase();

        return result;
    }

    private User getUserWithId(long userId) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getUserWithId((int) userId);
        petBetterDb.closeDatabase();

        return result;
    }


    public ArrayList<PostRep> getPostRepsFromParent(long parentId) {
        try {
            petBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<PostRep> result = petBetterDb.getPostRepsFromParent((int) parentId);
        petBetterDb.closeDatabase();
        return result;
    }

    public void updateList(ArrayList<PostRep> newList){
        postRepList.clear();
        postRepList.addAll(newList);
        this.notifyDataSetChanged();
        //recyclerView.setAdapter(this);
    }



    @Override
    public int getItemCount() {

        //return (postRepList.size() > 0 ? postRepList.size() : 1);
        return postRepList.size();
    }

    @Override
    public void onPopupMenuClicked(final View view, final int pos) {
        final PostRep thisComment = postRepList.get(pos);
        PopupMenu options = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = options.getMenuInflater();
        inflater.inflate(R.menu.post_topic_menu, options.getMenu());
        System.out.println("Options menu clicked");

        options.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_delete_post_topic:
                        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View popUpConfirmation = inflater.inflate(R.layout.popup_confirmation_delete_comment, null);

                        popUpConfirmation.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

                        //int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

                        popUpConfirmationWindow = new PopupWindow(popUpConfirmation, 750, 360, true);
                        popUpConfirmationWindow.showAtLocation(popUpConfirmation, Gravity.CENTER, 0, 0);

                        Button cancelButton = (Button) popUpConfirmationWindow.getContentView().findViewById(R.id.popUpCancelButton);

                        Button deleteButton = (Button) popUpConfirmationWindow.getContentView().findViewById(R.id.popUpDeleteButton);
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
                                deletePostRep(thisComment.getId());
                                service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
                                final HerokuService service2 = ServiceGenerator.getServiceGenerator().create(HerokuService.class);

                                final Call<Void> call = service.deletePostRep(thisComment.getUserId(), thisComment.getDatePerformed());
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        //User thisUser = response.body();
                                        if(response.isSuccessful()){
                                            dataSynced(10);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.d("onFailure", t.getLocalizedMessage());
                                    }
                                });

                                popUpConfirmationWindow.dismiss();
                                updateData(pos);
                            }
                        });

//                    initializeDatabase();

                }

                return false;
            }
        });
        options.show();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

   class PostRepViewHolder extends RecyclerView.ViewHolder {

        //edit this later
        private ImageView postRepImage;
        private TextView postRepName;
        private TextView postRepContent;
        private TextView postRepTime;
        private ImageButton postRepOptionsButton;
        private TextView postRepCounter;
        private TextView sentenceStart;
        private TextView sentenceEnd;
        private ImageView userIdentifier;

        public PostRepViewHolder(View itemView) {
            super(itemView);

            postRepImage = (ImageView) itemView.findViewById(R.id.commentUserProfilePicture);
            postRepName = (TextView) itemView.findViewById(R.id.commentUserProfileName);
            postRepContent = (TextView) itemView.findViewById(R.id.commentContent);
            postRepTime = (TextView) itemView.findViewById(R.id.commentTimeStamp);
            postRepOptionsButton = (ImageButton) itemView.findViewById(R.id.topicOptionsButton);
            postRepCounter = (TextView) itemView.findViewById(R.id.postRepCounter);
            sentenceStart = (TextView) itemView.findViewById(R.id.replySentenceStart);
            sentenceEnd = (TextView) itemView.findViewById(R.id.replySentenceEnd);
            userIdentifier = (ImageView) itemView.findViewById(R.id.iconIdentifier);
        }

        public void bind(final PostRep item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    private class emptyPostRepViewHolder extends RecyclerView.ViewHolder {

        private TextView emptyTextView;

        public emptyPostRepViewHolder(View itemView) {
            super(itemView);

            TextView emptyTextView = (TextView) itemView.findViewById(R.id.emptyView);
        }
    }
}
