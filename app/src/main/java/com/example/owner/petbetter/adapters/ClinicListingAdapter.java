package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Facility;

import java.util.ArrayList;

/**
 * Created by owner on 21/8/2017.
 */

public class ClinicListingAdapter extends RecyclerView.Adapter<ClinicListingAdapter.ClinicListingViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<Facility> faciList;

    public ClinicListingAdapter(Context context, ArrayList<Facility> faciList) {
        inflater = LayoutInflater.from(context);
        this.faciList = faciList;
    }

    @Override
    public ClinicListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_petcare_item, parent, false);
        System.out.println("We here bro");
        ClinicListingViewHolder holder = new ClinicListingViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ClinicListingViewHolder holder, int position) {

        Facility thisClinic = faciList.get(position);
        holder.clinicListName.setText(thisClinic.getFaciName());
        holder.clinicListAddress.setText(thisClinic.getLocation());
        holder.clinicListRating.setText(Integer.toString(thisClinic.getRating()));

    }

    @Override
    public int getItemCount() {
        return (faciList.size() > 0 ? faciList.size() : 1);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ClinicListingViewHolder extends RecyclerView.ViewHolder{

        private ImageView clinicListImage;
        private TextView clinicListName;
        private TextView clinicListAddress;
        private TextView clinicListRating;

        public ClinicListingViewHolder(View itemView) {
            super(itemView);
            clinicListImage = (ImageView) itemView.findViewById(R.id.clinicListImage);
            clinicListName = (TextView) itemView.findViewById(R.id.clinicListName);
            clinicListAddress = (TextView) itemView.findViewById(R.id.clinicListAddress);
            clinicListRating = (TextView) itemView.findViewById(R.id.clinicListRating);
        }
    }
}
