package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.classes.Veterinarian;

import java.util.ArrayList;

/**
 * Created by owner on 30/3/2018.
 */

public class VetRowAdapter extends RecyclerView.Adapter<VetRowAdapter.VetRowHolder>{
    private LayoutInflater inflater;
    private ArrayList<Veterinarian> vetList;

    HerokuService service;

    public VetRowAdapter(Context context, LayoutInflater inflater, ArrayList<Veterinarian> vetList){
        this.inflater = LayoutInflater.from(context);
        this.vetList = vetList;
    }
    @Override
    public VetRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_vets_row, parent, false);
        VetRowHolder holder = new VetRowHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(VetRowHolder holder, int position) {
        final Veterinarian thisVet = vetList.get(position);

        holder.tvVetRow.setText(thisVet.getName());
        //holder.priceText.setText(Float.toString(thisS.getServicePrice()));


    }

    @Override
    public int getItemCount() {
        return vetList.size();
    }

    class VetRowHolder extends RecyclerView.ViewHolder{

        private TextView tvVetRow;

        public VetRowHolder(View itemView){
            super(itemView);
            tvVetRow = (TextView) itemView.findViewById(R.id.tvVetRow);
        }
    }
}
