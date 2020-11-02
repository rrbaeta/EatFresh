package com.example.eatfresh.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eatfresh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class FoodListFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private Button addItemBtn;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FoodListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_food_list, container, false);

        mAuth = FirebaseAuth.getInstance();

        addItemBtn = rootView.findViewById(R.id.addItemBtn);

        addItemBtn.setOnClickListener(this);

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
        switch (view.getId())
        {
            case R.id.addItemBtn:
                Navigation.findNavController(view).navigate(R.id.action_foodListFragment_to_addItemFragment);
                break;
        }

    }

}