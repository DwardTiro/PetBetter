package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Facility;
import com.example.owner.petbetter.classes.Post;
import com.example.owner.petbetter.classes.Topic;
import com.example.owner.petbetter.classes.Veterinarian;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kristian on 11/7/2017.
 */

public class SearchListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener{
        void OnItemClick(Object item);
    }

    private LayoutInflater inflater;
    private final OnItemClickListener listener;
    private static final int ITEM_VET_RESULT = 1;
    private static final int ITEM_CLINIC_RESULT = 2;
    private static final int ITEM_POST_RESULT = 3;
    private static final int ITEM_TOPIC_RESULT = 4;
    private ArrayList<Topic> topicList;
    private ArrayList<Veterinarian> vetList;
    private ArrayList<Facility> clinicList;
    private ArrayList<Post> postList;
    private List<Object> searchResults;

    public SearchListingAdapter(Context context, List<Object> searchResults, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        //this.topicList = topicList;
        //this.vetList = vetList;
        //this.clinicList = clinicList;
        //this.postList = postList;
        this.searchResults = searchResults;
        this.listener = listener;

    }

    //used to get what item type the object is
    @Override
    public int getItemViewType(int position) {
        if (searchResults.get(position) instanceof Veterinarian) {
            return ITEM_VET_RESULT;
        } else if (searchResults.get(position) instanceof Facility) {
            return ITEM_CLINIC_RESULT;
        } else if (searchResults.get(position) instanceof Post) {
            return ITEM_POST_RESULT;
        } else if (searchResults.get(position) instanceof Topic) {
            return ITEM_TOPIC_RESULT;
        }
        return 0;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ITEM_VET_RESULT) {
            View view = inflater.inflate(R.layout.fragment_vet_item, parent, false);
            return new VetViewHolder(view);
        } else if (viewType == ITEM_CLINIC_RESULT) {
            View view = inflater.inflate(R.layout.fragment_petcare_item, parent, false);
            return new ClinicViewHolder(view);
        } else if (viewType == ITEM_POST_RESULT) {
            View view = inflater.inflate(R.layout.fragment_community_item, parent, false);
            return new PostViewHolder(view);
        } else if (viewType == ITEM_TOPIC_RESULT) {
            View view = inflater.inflate(R.layout.fragment_community_item, parent, false);
            return new TopicViewHolder(view);
        }


        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object searchResult = searchResults.get(position);


        if (holder instanceof VetViewHolder) {
            //holder and search result must be typecasted in order to get values of specific classes
            ((VetViewHolder) holder).vetListName.setText(((Veterinarian) searchResult).getFirstName()+" "+(((Veterinarian) searchResult).getLastName()));
            ((VetViewHolder) holder).vetListSpecialty.setText(((Veterinarian) searchResult).getSpecialty());
            ((VetViewHolder) holder).vetListRating.setText(String.format(Locale.getDefault(),"%.1f", ((Veterinarian) searchResult).getRating()));
            if(((Veterinarian) searchResult).getRating() == 0.0){
                ((VetViewHolder) holder).vetListRating.setBackgroundResource(R.color.teal_blue);
            }
            else if (((Veterinarian) searchResult).getRating() <= 5.0 && ((Veterinarian) searchResult).getRating() >=4.5){
                ((VetViewHolder) holder).vetListRating.setBackgroundResource(R.color.colorYellow);

            }
            else if (((Veterinarian) searchResult).getRating() < 4.5 && ((Veterinarian) searchResult).getRating() >=4.0){
                ((VetViewHolder) holder).vetListRating.setBackgroundResource(R.color.peridot);
            }
            else if (((Veterinarian) searchResult).getRating() < 4.0 && ((Veterinarian) searchResult).getRating() >=3.5){
                ((VetViewHolder) holder).vetListRating.setBackgroundResource(R.color.main_Color);
            }
            else if (((Veterinarian) searchResult).getRating() < 3.5 && ((Veterinarian) searchResult).getRating() >=3.0){
                ((VetViewHolder) holder).vetListRating.setBackgroundResource(R.color.orange);
            }
            else if (((Veterinarian) searchResult).getRating() < 3.0 && ((Veterinarian) searchResult).getRating() >=2.5){
                ((VetViewHolder) holder).vetListRating.setBackgroundResource(R.color.dark_orange);
            }
            else if (((Veterinarian) searchResult).getRating() < 2.5 && ((Veterinarian) searchResult).getRating() >=2.0){
                ((VetViewHolder) holder).vetListRating.setBackgroundResource(R.color.fiery_red);
            }
            else if (((Veterinarian) searchResult).getRating() < 2.0 && ((Veterinarian) searchResult).getRating() >=1.5){
                ((VetViewHolder) holder).vetListRating.setBackgroundResource(R.color.flame_red);
            }
            else{
                ((VetViewHolder) holder).vetListRating.setBackgroundResource(R.color.dark_candy_red);
            }

            ((VetViewHolder) holder).bind(((Veterinarian) searchResult),listener);
        } else if (holder instanceof ClinicViewHolder) {
            ((ClinicViewHolder) holder).clinicListName.setText(((Facility) searchResult).getFaciName());
            ((ClinicViewHolder) holder).clinicListAddress.setText(((Facility) searchResult).getLocation());
            ((ClinicViewHolder) holder).clinicListRating.setText(String.format(Locale.getDefault(),"%.1f",
                    ((Facility) searchResult).getRating()));
            ((ClinicViewHolder) holder).clinicOpenTime.setText(((Facility) searchResult).getHoursOpen());
            ((ClinicViewHolder) holder).clinicClosetime.setText(((Facility) searchResult).getHoursClose());
            if(((Facility) searchResult).getRating() == 0.0){
                ((ClinicViewHolder) holder).clinicListRating.setBackgroundResource(R.color.teal_blue);
            }
            else if (((Facility) searchResult).getRating() <= 5.0 && ((Facility) searchResult).getRating() >=4.5){
                ((ClinicViewHolder) holder).clinicListRating.setBackgroundResource(R.color.colorYellow);

            }
            else if (((Facility) searchResult).getRating() < 4.5 && ((Facility) searchResult).getRating() >=4.0){
                ((ClinicViewHolder) holder).clinicListRating.setBackgroundResource(R.color.peridot);
            }
            else if (((Facility) searchResult).getRating() < 4.0 && ((Facility) searchResult).getRating() >=3.5){
                ((ClinicViewHolder) holder).clinicListRating.setBackgroundResource(R.color.main_Color);
            }
            else if (((Facility) searchResult).getRating() < 3.5 && ((Facility) searchResult).getRating() >=3.0){
                ((ClinicViewHolder) holder).clinicListRating.setBackgroundResource(R.color.orange);
            }
            else if (((Facility) searchResult).getRating() < 3.0 && ((Facility) searchResult).getRating() >=2.5){
                ((ClinicViewHolder) holder).clinicListRating.setBackgroundResource(R.color.dark_orange);
            }
            else if (((Facility) searchResult).getRating() < 2.5 && ((Facility) searchResult).getRating() >=2.0){
                ((ClinicViewHolder) holder).clinicListRating.setBackgroundResource(R.color.fiery_red);
            }
            else if (((Facility) searchResult).getRating() < 2.0 && ((Facility) searchResult).getRating() >=1.5){
                ((ClinicViewHolder) holder).clinicListRating.setBackgroundResource(R.color.flame_red);
            }
            else{
                ((ClinicViewHolder) holder).clinicListRating.setBackgroundResource(R.color.dark_candy_red);
            }

            ((ClinicViewHolder) holder).bind(((Facility) searchResult),listener);

        } else if (holder instanceof TopicViewHolder) {
            ((TopicViewHolder) holder).topicName.setText(((Topic) searchResult).getTopicName());
            ((TopicViewHolder) holder).topicDescription.setText(((Topic) searchResult).getTopicDesc());
            ((TopicViewHolder) holder).topicUser.setText(((Topic) searchResult).getCreatorName());
            ((TopicViewHolder) holder).textviewFollowers.setText(String.format(Locale.getDefault(),"%d",((Topic) searchResult).getFollowerCount()));
            ((TopicViewHolder) holder).bind(((Topic) searchResult),listener);
        }
        //else if (holder instanceof PostViewHolder) {

        //}

    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    private class VetViewHolder extends RecyclerView.ViewHolder {

        private ImageView vetListImage;
        private TextView vetListName;
        private TextView vetListSpecialty;
        private TextView vetListRating;

        public VetViewHolder(View itemView) {
            super(itemView);
            vetListImage = (ImageView) itemView.findViewById(R.id.vetListImage);
            vetListName = (TextView) itemView.findViewById(R.id.vetListName);
            vetListSpecialty = (TextView) itemView.findViewById(R.id.vetListSpecialty);
            vetListRating = (TextView) itemView.findViewById(R.id.vetListRating);
        }
        /*public void bind(final Veterinarian item, final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }*/

        private void bind(final Veterinarian item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(item);
                }
            });

        }
    }

    private class ClinicViewHolder extends RecyclerView.ViewHolder {

        private ImageView clinicListImage;
        private TextView clinicListName;
        private TextView clinicListAddress;
        private TextView clinicListRating;
        private TextView clinicOpenTime;
        private TextView clinicClosetime;

        public ClinicViewHolder(View itemView) {
            super(itemView);

            clinicListImage = (ImageView) itemView.findViewById(R.id.clinicListImage);
            clinicListName = (TextView) itemView.findViewById(R.id.clinicListName);
            clinicListAddress = (TextView) itemView.findViewById(R.id.clinicListAddress);
            clinicListRating = (TextView) itemView.findViewById(R.id.clinicListRating);
            clinicOpenTime = (TextView) itemView.findViewById(R.id.clinicListOpenTime);
            clinicClosetime = (TextView) itemView.findViewById(R.id.clinicListCloseTime);
        }

        private void bind(final Facility item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(item);
                }
            });

        }
    }

    private class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView topicName;
        private TextView topicDescription;
        private TextView topicUser;
        private ImageButton deletePostButton;

        public PostViewHolder(View itemView) {
            super(itemView);
            topicName = (TextView) itemView.findViewById(R.id.topicName);
            topicDescription = (TextView) itemView.findViewById(R.id.topicDescription);
            topicUser = (TextView) itemView.findViewById(R.id.topicUser);
            deletePostButton = (ImageButton) itemView.findViewById(R.id.deletePostButton);
        }

        private void bind(final Post item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(item);
                }
            });

        }
    }

    private class TopicViewHolder extends RecyclerView.ViewHolder {

        private TextView topicName;
        private TextView topicDescription;
        private TextView topicUser;
        private TextView textviewFollowers;
        private ImageButton deleteTopicButton;

        public TopicViewHolder(View itemView) {
            super(itemView);
            topicName = (TextView) itemView.findViewById(R.id.topicComName);
            topicDescription = (TextView) itemView.findViewById(R.id.topicComDescription);
            topicUser = (TextView) itemView.findViewById(R.id.topicComUser);
            textviewFollowers = (TextView) itemView.findViewById(R.id.textViewFollowers);
            deleteTopicButton = (ImageButton) itemView.findViewById(R.id.deleteTopicButton);
        }

        private void bind(final Topic item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(item);
                }
            });

        }
    }
}
