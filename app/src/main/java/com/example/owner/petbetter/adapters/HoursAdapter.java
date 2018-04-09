package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.WorkHours;

import java.util.ArrayList;

/**
 * Created by owner on 9/4/2018.
 */

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.HoursListingHolder>{

    private LayoutInflater inflater;
    private ArrayList<WorkHours> hoursList;

    HerokuService service;

    public HoursAdapter(Context context, LayoutInflater inflater, ArrayList<WorkHours> hoursList){
        this.inflater = LayoutInflater.from(context);
        this.hoursList = hoursList;
    }
    @Override
    public HoursListingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_hours_row, parent, false);
        HoursListingHolder holder = new HoursListingHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(HoursListingHolder holder, int position) {
        final WorkHours thisHour = hoursList.get(position);

        holder.daysTextField.setText(thisHour.getDays());
        holder.timeTextField.setText(thisHour.getHoursOpen()+" - "+thisHour.getHoursClose());
        /*
        holder.serviceNameText.setText(thisService.getServiceName());
        holder.priceText.setText(Float.toString(thisService.getServicePrice()));
        */

    }

    @Override
    public int getItemCount() {
        return hoursList.size();
    }

    class HoursListingHolder extends RecyclerView.ViewHolder{

        private TextView daysTextField;
        private TextView timeTextField;

        public HoursListingHolder(View itemView){
            super(itemView);
            daysTextField = (TextView) itemView.findViewById(R.id.daysTextField);
            timeTextField = (TextView) itemView.findViewById(R.id.timeTextField);
        }
    }
}
