package com.example.owner.petbetter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.petbetter.R;
import com.google.android.gms.vision.text.Text;

/**
 * Created by Kristian on 8/4/2017.
 */

public class FragmentVetListing extends Fragment {

    private TextView nameTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View RootView = inflater.inflate(R.layout.fragment_vet_listings,container, false);

        nameTextView = (TextView) RootView.findViewById(R.id.vetListName);
        return RootView;
    }

    public void setNameTextView(String text)
    {

      //  TextView nameTextView = (TextView) getView().findViewById(R.id.vetListName);

        nameTextView.setText(text);
    }
    public TextView getVetNameTextView(){
        return nameTextView;
    }
}

