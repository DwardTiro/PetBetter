package com.example.owner.petbetter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.owner.petbetter.R;

/**
 * Created by Kristian on 8/8/2017.
 */

public class FragmentPetClinicListing extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        return inflater.inflate(R.layout.fragment_petcare_listing,container, false);
    }

}