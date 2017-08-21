package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Veterinarian;

import java.util.ArrayList;

/**
 * Created by owner on 15/8/2017.
 */

public class VetListingAdapter extends RecyclerView.Adapter<VetListingAdapter.VetListingViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<Veterinarian> vetList;
    public VetListingAdapter(Context context, ArrayList<Veterinarian> vetList) {
        inflater = LayoutInflater.from(context);
        this.vetList = vetList;
    }

    @Override
    public VetListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_vet_item, parent, false);
        System.out.println("We here mate");
        VetListingViewHolder holder = new VetListingViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(VetListingViewHolder holder, int position) {
        Veterinarian thisVet = vetList.get(position);
        holder.vetListName.setText(thisVet.getName());
        System.out.println("The vet's name is "+holder.vetListName.getText());
        holder.vetListSpecialty.setText(thisVet.getSpecialty());
        holder.vetListRating.setText(Integer.toString(thisVet.getRating()));
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
    }
}
