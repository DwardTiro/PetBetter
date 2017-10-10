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
import com.example.owner.petbetter.classes.Marker;

import java.util.ArrayList;

/**
 * Created by owner on 24/9/2017.
 */

public class BookmarkListingAdapter extends RecyclerView.Adapter<BookmarkListingAdapter.BookmarkListingViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(Marker item);
    }

    private LayoutInflater inflater;
    private ArrayList<Marker> bookmarkList;
    private final OnItemClickListener listener;

    public BookmarkListingAdapter(Context context, ArrayList<Marker> bookmarkList, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.bookmarkList = bookmarkList;
        this.listener = listener;
    }

    @Override
    public BookmarkListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_bookmark_item, parent, false);
        System.out.println("Are we even reaching this?");
        BookmarkListingViewHolder holder = new BookmarkListingViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(BookmarkListingViewHolder holder, int position) {
        Marker thisBookmark = bookmarkList.get(position);
        holder.bookmarkListName.setText(thisBookmark.getBldgName());
        System.out.println("Bldg name is: " +holder.bookmarkListName.getText());
        holder.bookmarkListAddress.setText(thisBookmark.getLocation());
        holder.bind(thisBookmark, listener);

    }

    public void update(ArrayList<Marker> list){
        bookmarkList.clear();
        bookmarkList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }



    class BookmarkListingViewHolder extends RecyclerView.ViewHolder{

        private TextView bookmarkListName;
        private TextView bookmarkListAddress;

        public BookmarkListingViewHolder(View itemView) {
            super(itemView);

            bookmarkListName = (TextView) itemView.findViewById(R.id.bookmarkListName);
            bookmarkListAddress = (TextView) itemView.findViewById(R.id.bookmarkListAddress);
        }

        public void bind(final Marker item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
