package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Veterinarian;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

/**
 * Created by owner on 15/8/2017.
 */

public class VetListingAdapter extends RecyclerView.Adapter<VetListingAdapter.VetListingViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(Veterinarian item);
    }

    private LayoutInflater inflater;
    private ArrayList<Veterinarian> vetList;
    private final OnItemClickListener listener;

    public VetListingAdapter(Context context, ArrayList<Veterinarian> vetList, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.vetList = vetList;
        this.listener = listener;
    }

    @Override
    public VetListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_vet_item, parent, false);
        System.out.println("We here mate kek wew");
        VetListingViewHolder holder = new VetListingViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(VetListingViewHolder holder, int position) {
        Veterinarian thisVet = vetList.get(position);
        holder.vetListName.setText(thisVet.getName());

        System.out.println("The vet's name is "+holder.vetListName.getText());
        holder.vetListSpecialty.setText("");
        holder.vetListRating.setText(String.format(Locale.getDefault(),"%.1f",thisVet.getRating()));

        if(thisVet.getUserPhoto()!=null){

            String newFileName = BASE_URL + thisVet.getUserPhoto();
            System.out.println("USER PHOTO WHERE "+newFileName);
            Glide.with(inflater.getContext()).load(newFileName).error(R.drawable.back_button).into(holder.vetListImage);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            holder.vetListImage.setVisibility(View.VISIBLE);
            holder.vetListImage.setAdjustViewBounds(true);
        }

        if(thisVet.getRating() == 0.0){
            holder.vetListRating.setBackgroundResource(R.color.teal_blue);
        }
        else if (thisVet.getRating() <= 5.0 && thisVet.getRating() >=4.5){
            holder.vetListRating.setBackgroundResource(R.color.colorYellow);

        }
        else if (thisVet.getRating() < 4.5 && thisVet.getRating() >=4.0){
            holder.vetListRating.setBackgroundResource(R.color.peridot);
        }
        else if (thisVet.getRating() < 4.0 && thisVet.getRating() >=3.5){
            holder.vetListRating.setBackgroundResource(R.color.main_Color);
        }
        else if (thisVet.getRating() < 3.5 && thisVet.getRating() >=3.0){
            holder.vetListRating.setBackgroundResource(R.color.orange);
        }
        else if (thisVet.getRating() < 3.0 && thisVet.getRating() >=2.5){
            holder.vetListRating.setBackgroundResource(R.color.dark_orange);
        }
        else if (thisVet.getRating() < 2.5 && thisVet.getRating() >=2.0){
            holder.vetListRating.setBackgroundResource(R.color.fiery_red);
        }
        else if (thisVet.getRating() < 2.0 && thisVet.getRating() >=1.5){
            holder.vetListRating.setBackgroundResource(R.color.flame_red);
        }
        else{
            holder.vetListRating.setBackgroundResource(R.color.dark_candy_red);
        }

        holder.bind(thisVet, listener);
    }

    @Override
    public int getItemCount() {

        return (vetList.size() > 0 ? vetList.size() : 1);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    class VetListingViewHolder extends RecyclerView.ViewHolder{

        private ImageView vetListImage;
        private TextView vetListName;
        private TextView vetListSpecialty;
        private TextView vetListRating;

        public VetListingViewHolder(View itemView) {
            super(itemView);
            vetListImage = (ImageView) itemView.findViewById(R.id.vetListImage);
            vetListName = (TextView) itemView.findViewById(R.id.vetListName);
            vetListSpecialty = (TextView) itemView.findViewById(R.id.vetListSpecialty);
            vetListRating = (TextView) itemView.findViewById(R.id.vetListRating);
        }

        public void bind(final Veterinarian item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
