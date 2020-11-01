package com.example.eatfresh.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eatfresh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AddItemFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;

    public AddItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);

        mAuth = FirebaseAuth.getInstance();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    //all onClicks
    @Override
    public void onClick(View view)
    {
//        switch (view.getId())
//        {
//            case R.id.signupBtn2:
//
//                break;
//        }

    }

}