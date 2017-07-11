package com.example.owner.petbetter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
/**
 * Created by owner on 11/7/2017.
 */

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";
    private Button btnTest2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.home_tab1, container, false);
        btnTest2 = (Button) view.findViewById(R.id.btnTest1);

        btnTest2.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),"Testing button 1",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        return view;
    }
}
