package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Veterinarian;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        holder.vetListSpecialty.setText(thisVet.getSpecialty());

        if(thisVet.getUserPhoto()!=null){

            String newFileName = BASE_URL + thisVet.getUserPhoto();
            System.out.println("USER PHOTO WHERE "+newFileName);
            Glide.with(inflater.getContext()).load(newFileName).error(R.drawable.app_icon_yellow).into(holder.vetListImage);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);

            holder.vetListImage.setVisibility(View.VISIBLE);
            holder.vetListImage.setAdjustViewBounds(true);
        }

        getRatingWithVetId(holder, thisVet.getId());

        holder.bind(thisVet, listener);
    }

    public void getRatingWithVetId(final VetListingViewHolder holder, long vetId){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<Float> call = service.getRatingWithId(vetId,1);
        call.enqueue(new Callback<Float>() {
            @Override
            public void onResponse(Call<Float> call, Response<Float> response) {
                if(response.isSuccessful()){
                    float rating = response.body();
                    holder.vetListRating.setText(String.format(Locale.getDefault(),"%.1f",rating));
                    if(rating == 0.0){
                        holder.vetListRating.setBackgroundResource(R.color.dark_candy_red);
                    }
                    else if (rating <= 5.0 && rating >=4.5){
                        holder.vetListRating.setBackgroundResource(R.color.colorYellow);

                    }
                    else if (rating < 4.5 && rating >=4.0){
                        holder.vetListRating.setBackgroundResource(R.color.peridot);
                    }
                    else if (rating < 4.0 && rating >=3.5){
                        holder.vetListRating.setBackgroundResource(R.color.main_Color);
                    }
                    else if (rating < 3.5 && rating >=3.0){
                        holder.vetListRating.setBackgroundResource(R.color.orange);
                    }
                    else if (rating < 3.0 && rating >=2.5){
                        holder.vetListRating.setBackgroundResource(R.color.dark_orange);
                    }
                    else if (rating < 2.5 && rating >=2.0){
                        holder.vetListRating.setBackgroundResource(R.color.fiery_red);
                    }
                    else if (rating < 2.0 && rating >=1.5){
                        holder.vetListRating.setBackgroundResource(R.color.flame_red);
                    }
                    else{
                        holder.vetListRating.setBackgroundResource(R.color.dark_candy_red);
                    }
                }
            }

            @Override
            public void onFailure(Call<Float> call, Throwable t) {
                Log.d("onFailure", t.getLocalizedMessage());
            }
        });
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
