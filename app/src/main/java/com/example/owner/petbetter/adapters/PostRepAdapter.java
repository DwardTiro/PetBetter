package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.PostRep;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;

import java.sql.SQLException;
import java.util.ArrayList;


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

    public PostRepAdapter(Context context, ArrayList<PostRep> postRepList, User user, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.postRepList = postRepList;
        this.listener = listener;
        this.user = user;
        this.type = type;
    }

    @Override
    public PostRepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_home_listing_comment, parent, false);
        System.out.println("We here mate kek wew");
        PostRepViewHolder holder = new PostRepViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(PostRepViewHolder holder, int position) {
        final PostRep thisComment = postRepList.get(position);

        initializeDatabase();

        holder.postRepName.setText(thisComment.getUserName());
        holder.postRepTime.setText(thisComment.getDatePerformed());
        holder.postRepContent.setText(thisComment.getRepContent());
        holder.bind(thisComment, listener);

        postReps = getPostRepsFromParent(thisComment.getId());

        if(postReps.size()>0)
            holder.postRepCounter.setText(Integer.toString(postReps.size()));
        else{
            holder.postRepCounter.setVisibility(View.INVISIBLE);
            holder.postRepCounter.setEnabled(false);
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

                            deletePostRep(thisComment.getId());
                            popUpConfirmationWindow.dismiss();
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

    private void initializeDatabase() {

        petBetterDb = new DataAdapter(context);

        try {
            petBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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


    public ArrayList<PostRep> getPostRepsFromParent(long parentId){
        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<PostRep> result = petBetterDb.getPostRepsFromParent((int) parentId);
        petBetterDb.closeDatabase();
        return result;
    }


    @Override
    public int getItemCount() {

        return (postRepList.size() > 0 ? postRepList.size() : 1);
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
}
