package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Services;

import java.util.ArrayList;

/**
 * Created by Kristian on 3/25/2018.
 */

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceListingHolder>{

    private LayoutInflater inflater;
    private ArrayList<Services> serviceList;

    HerokuService service;

    public ServiceAdapter(Context context, LayoutInflater inflater,ArrayList<Services> serviceList){
        this.inflater = LayoutInflater.from(context);
        this.serviceList=serviceList;
    }
    @Override
    public ServiceListingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_service_row, parent, false);
        ServiceListingHolder holder = new ServiceListingHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ServiceListingHolder holder, int position) {
        final Services thisService = serviceList.get(position);

        holder.serviceNameText.setText(thisService.getServiceName());
        holder.priceText.setText(Float.toString(thisService.getServicePrice()));


    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    class ServiceListingHolder extends RecyclerView.ViewHolder{

        private TextView serviceNameText;
        private TextView priceText;

        public ServiceListingHolder(View itemView){
            super(itemView);
            serviceNameText = (TextView) itemView.findViewById(R.id.serviceNameTextView);
            priceText = (TextView) itemView.findViewById(R.id.priceNameTextView);
        }
    }
}
