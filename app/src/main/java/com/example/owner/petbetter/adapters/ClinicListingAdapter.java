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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by owner on 21/8/2017.
 */

public class ClinicListingAdapter extends RecyclerView.Adapter<ClinicListingAdapter.ClinicListingViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(Facility item);
    }

    private LayoutInflater inflater;
    private ArrayList<Facility> faciList;
    private final OnItemClickListener listener;

    public ClinicListingAdapter(Context context, ArrayList<Facility> faciList, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.faciList = faciList;
        this.listener = listener;
    }

    @Override
    public ClinicListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_petcare_item, parent, false);
        System.out.println("Clicked petcare item");
        ClinicListingViewHolder holder = new ClinicListingViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ClinicListingViewHolder holder, int position) {

        Facility thisClinic = faciList.get(position);
        holder.clinicListName.setText(thisClinic.getFaciName());
        holder.clinicListAddress.setText(thisClinic.getLocation());
        holder.clinicListRating.setText(String.format(Locale.getDefault(),"%.1f",thisClinic.getRating()));
        if(thisClinic.getRating() == 0.0){
            holder.clinicListRating.setBackgroundResource(R.color.teal_blue);
        }
        else if (thisClinic.getRating() <= 5.0 && thisClinic.getRating() >=4.5){
            holder.clinicListRating.setBackgroundResource(R.color.colorYellow);

        }
        else if (thisClinic.getRating() < 4.5 && thisClinic.getRating() >=4.0){
            holder.clinicListRating.setBackgroundResource(R.color.peridot);
        }
        else if (thisClinic.getRating() < 4.0 && thisClinic.getRating() >=3.5){
            holder.clinicListRating.setBackgroundResource(R.color.main_Color);
        }
        else if (thisClinic.getRating() < 3.5 && thisClinic.getRating() >=3.0){
            holder.clinicListRating.setBackgroundResource(R.color.orange);
        }
        else if (thisClinic.getRating() < 3.0 && thisClinic.getRating() >=2.5){
            holder.clinicListRating.setBackgroundResource(R.color.dark_orange);
        }
        else if (thisClinic.getRating() < 2.5 && thisClinic.getRating() >=2.0){
            holder.clinicListRating.setBackgroundResource(R.color.fiery_red);
        }
        else if (thisClinic.getRating() < 2.0 && thisClinic.getRating() >=1.5){
            holder.clinicListRating.setBackgroundResource(R.color.flame_red);
        }
        else{
            holder.clinicListRating.setBackgroundResource(R.color.dark_candy_red);
        }
        holder.clinicOpenTime.setText(thisClinic.getHoursOpen());
        holder.clinicClosetime.setText(thisClinic.getHoursClose());
        holder.bind(thisClinic, listener);

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
        private TextView clinicOpenTime;
        private TextView clinicClosetime;

        public ClinicListingViewHolder(View itemView) {
            super(itemView);
            clinicListImage = (ImageView) itemView.findViewById(R.id.clinicListImage);
            clinicListName = (TextView) itemView.findViewById(R.id.clinicListName);
            clinicListAddress = (TextView) itemView.findViewById(R.id.clinicListAddress);
            clinicListRating = (TextView) itemView.findViewById(R.id.clinicListRating);
            clinicOpenTime = (TextView) itemView.findViewById(R.id.clinicListOpenTime);
            clinicClosetime = (TextView) itemView.findViewById(R.id.clinicListCloseTime);
        }

        public void bind(final Facility item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
