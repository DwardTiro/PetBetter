package com.example.owner.petbetter.adapters;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.sql.SQLException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;


/**
 * Created by owner on 14/10/2017.
 */

public class PostRepAdapter extends RecyclerView.Adapter<PostRepAdapter.PostRepViewHolder> {
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
        User user = getUserWithId(thisComment.getUserId());
        holder.postRepName.setText(thisComment.getUserName());
        holder.postRepTime.setText(thisComment.getDatePerformed());
        holder.postRepContent.setText(thisComment.getRepContent());
        holder.bind(thisComment, listener);

        postReps = getPostRepsFromParent(thisComment.getId());

        if (postReps.size() > 0)
            holder.postRepCounter.setText(Integer.toString(postReps.size()));
        else {
            holder.postRepCounter.setVisibility(View.INVISIBLE);
            holder.postRepCounter.setEnabled(false);
        }

        if(user.getUserPhoto()!=null){

            String newFileName = BASE_URL + user.getUserPhoto();
            System.out.println(newFileName);
            //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
            Glide.with(inflater.getContext()).load(newFileName).error(R.drawable.back_button).into(holder.postRepImage);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            holder.postRepImage.setVisibility(View.VISIBLE);
        }

        if (user.getUserId() == thisComment.getUserId()) {
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
            });
        } else {
            holder.deletePostRepButton.setVisibility(View.INVISIBLE);
            holder.deletePostRepButton.setEnabled(false);
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
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

   class PostRepViewHolder extends RecyclerView.ViewHolder {

        //edit this later
        private ImageView postRepImage;
        private TextView postRepName;
        private TextView postRepContent;
        private TextView postRepTime;
        private ImageButton deletePostRepButton;
        private TextView postRepCounter;

        public PostRepViewHolder(View itemView) {
            super(itemView);

            postRepImage = (ImageView) itemView.findViewById(R.id.commentUserProfilePicture);
            postRepName = (TextView) itemView.findViewById(R.id.commentUserProfileName);
            postRepContent = (TextView) itemView.findViewById(R.id.commentContent);
            postRepTime = (TextView) itemView.findViewById(R.id.commentTimeStamp);
            deletePostRepButton = (ImageButton) itemView.findViewById(R.id.deletePostRepButton);
            postRepCounter = (TextView) itemView.findViewById(R.id.postRepCounter);
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
