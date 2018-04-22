package com.example.owner.petbetter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.owner.petbetter.HerokuService;
import com.example.owner.petbetter.R;
import com.example.owner.petbetter.ServiceGenerator;
import com.example.owner.petbetter.classes.Facility;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.owner.petbetter.ServiceGenerator.BASE_URL;

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

        if(thisClinic.getFaciPhoto()!=null){

            String newFileName = BASE_URL + thisClinic.getFaciPhoto();


            //String newFileName = "http://192.168.0.19/petbetter/"+thisMessageRep.getMessagePhoto();
            Glide.with(inflater.getContext()).load(newFileName).error(R.drawable.app_icon_yellow).into(holder.clinicListImage);
            /*
            Picasso.with(inflater.getContext()).load("http://".concat(newFileName))
                    .error(R.drawable.back_button).into(holder.messageRepImage);*/
            //setImage(holder.messageRepImage, newFileName);
            holder.clinicListImage.setVisibility(View.VISIBLE);
        }

        getRatingWithFaciId(holder, thisClinic.getId());

        //holder.clinicOpenTime.setText(thisClinic.getHoursOpen());
        //holder.clinicClosetime.setText(thisClinic.getHoursClose());
        holder.bind(thisClinic, listener);

    }

    public void getRatingWithFaciId(final ClinicListingViewHolder holder, long faciId){

        final HerokuService service = ServiceGenerator.getServiceGenerator().create(HerokuService.class);
        final Call<Float> call = service.getRatingWithId(faciId,2);
        call.enqueue(new Callback<Float>() {
            @Override
            public void onResponse(Call<Float> call, Response<Float> response) {
                if(response.isSuccessful()){
                    float rating = response.body();
                    holder.clinicListRating.setText(String.format(Locale.getDefault(),"%.1f",rating));
                    if(rating == 0.0){
                        holder.clinicListRating.setBackgroundResource(R.color.dark_candy_red);
                    }
                    else if (rating <= 5.0 && rating >=4.5){
                        holder.clinicListRating.setBackgroundResource(R.color.colorYellow);

                    }
                    else if (rating < 4.5 && rating >=4.0){
                        holder.clinicListRating.setBackgroundResource(R.color.main_Color);
                    }
                    else if (rating < 4.0 && rating >=3.5){
                        holder.clinicListRating.setBackgroundResource(R.color.peridot);
                    }
                    else if (rating < 3.5 && rating >=3.0){
                        holder.clinicListRating.setBackgroundResource(R.color.orange);
                    }
                    else if (rating < 3.0 && rating >=2.5){
                        holder.clinicListRating.setBackgroundResource(R.color.dark_orange);
                    }
                    else if (rating < 2.5 && rating >=2.0){
                        holder.clinicListRating.setBackgroundResource(R.color.fiery_red);
                    }
                    else if (rating < 2.0 && rating >=1.5){
                        holder.clinicListRating.setBackgroundResource(R.color.flame_red);
                    }
                    else{
                        holder.clinicListRating.setBackgroundResource(R.color.dark_candy_red);
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
