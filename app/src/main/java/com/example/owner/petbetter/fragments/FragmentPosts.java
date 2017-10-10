package com.example.owner.petbetter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.owner.petbetter.R;

/**
 * Created by owner on 7/10/2017.
 */

public class FragmentPosts extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts,container, false);
        return view;
    }
}
