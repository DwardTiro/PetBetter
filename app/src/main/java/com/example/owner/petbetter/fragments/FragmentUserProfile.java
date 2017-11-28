package com.example.owner.petbetter.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.owner.petbetter.R;
import com.example.owner.petbetter.adapters.BookmarkListingAdapter;
import com.example.owner.petbetter.classes.Marker;
import com.example.owner.petbetter.classes.User;
import com.example.owner.petbetter.database.DataAdapter;
import com.example.owner.petbetter.sessionmanagers.SystemSessionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class FragmentUserProfile extends Fragment {

    TextView upName;
    TextView upEmail;
    TextView upMobileNum;
    TextView upLandLine;
    TextView upType;

    private DataAdapter petBetterDb;
    private SystemSessionManager systemSessionManager;
    private User user;
    private String email;
    private Button upEditButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_user_profile,container, false);
        //If code above doesn't work inflate homeactivity instead.

        systemSessionManager = new SystemSessionManager(getActivity());
        if(systemSessionManager.checkLogin())
            getActivity().finish();
        HashMap<String, String> userIn = systemSessionManager.getUserDetails();

        initializeDatabase();

        email = userIn.get(SystemSessionManager.LOGIN_USER_NAME);
        user = getUser(email);
        System.out.println("WASSUP BOYS");

        upName = (TextView) view.findViewById(R.id.upName);
        upEmail = (TextView) view.findViewById(R.id.upEmail);
        upMobileNum = (TextView) view.findViewById(R.id.upMobileNum);
        upLandLine = (TextView) view.findViewById(R.id.upLandLine);
        upType = (TextView) view.findViewById(R.id.upType);

        upName.setText(user.getFirstName()+" "+user.getLastName());
        upEmail.setText(user.getEmail());
        upMobileNum.append(user.getMobileNumber());
        upLandLine.append(user.getPhoneNumber());
        if(user.getUserType()==1)
            upType.append("Veterinarian");
        if(user.getUserType()==2)
            upType.append("User");

        upEditButton = (Button) view.findViewById(R.id.upEditButton);
        upEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),com.example.owner.petbetter.activities.EditProfileActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }



    private void initializeDatabase() {

        petBetterDb = new DataAdapter(getActivity());

        try {
            petBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }

    }

    private User getUser(String email){

        try {
            petBetterDb.openDatabase();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        User result = petBetterDb.getUser(email);
        petBetterDb.closeDatabase();

        return result;
    }


}
