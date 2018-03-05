package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.LocationMarker;

import java.util.ArrayList;

/**
 * Created by owner on 25/9/2017.
 */

public class FaciListingAdapter extends RecyclerView.Adapter<FaciListingAdapter.FaciListingViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(LocationMarker item);
    }

    private LayoutInflater inflater;
    private ArrayList<LocationMarker> faciList;
    private final OnItemClickListener listener;

    public FaciListingAdapter(Context context, ArrayList<LocationMarker> faciList, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.faciList = faciList;
        this.listener = listener;
    }

    @Override
    public FaciListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_facility_item, parent, false);
        System.out.println("We here bro");
        FaciListingViewHolder holder = new FaciListingViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(FaciListingAdapter.FaciListingViewHolder holder, int position) {

        LocationMarker thisFaci = faciList.get(position);
        holder.faciListName.setText(thisFaci.getBldgName());
        System.out.println("Bldg name is: " +holder.faciListName.getText());
        holder.faciListAddress.setText(thisFaci.getLocation());
        holder.bind(thisFaci, listener);

    }

    public void update(ArrayList<LocationMarker> list){
        faciList.clear();
        faciList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return faciList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    class FaciListingViewHolder extends RecyclerView.ViewHolder{

        private TextView faciListName;
        private TextView faciListAddress;
        private TextView faciRating;

        public FaciListingViewHolder(View itemView) {
            super(itemView);

            faciListName = (TextView) itemView.findViewById(R.id.faciListName);
            faciListAddress = (TextView) itemView.findViewById(R.id.faciListAddress);

        }

        public void bind(final LocationMarker item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }
}
