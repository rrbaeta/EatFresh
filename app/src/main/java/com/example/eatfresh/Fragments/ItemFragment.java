package com.example.eatfresh.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eatfresh.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class ItemFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;

    public Button cancelBtn;
    public Button deleteBtn;

    private static final String TAG = "YOUR-TAG-NAME";
    private String uid;
    private String itemId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_item, container, false);

        mAuth = FirebaseAuth.getInstance();

        cancelBtn = rootView.findViewById(R.id.cancelBtn);
        deleteBtn = rootView.findViewById(R.id.deleteBtn);

        itemId = getArguments().getString("itemClickedId");

        //TODO onStart() probably shouldn't be called here
        onStart();

        deleteBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        return rootView;
    }


    //all onClicks
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.cancelBtn:
                Navigation.findNavController(view).navigate(R.id.action_itemFragment_to_foodListFragment);
                break;

            case R.id.deleteBtn:
                db.collection("foodItems").document(itemId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
                Navigation.findNavController(view).navigate(R.id.action_itemFragment_to_foodListFragment);
                break;

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void updateUI(FirebaseUser user){
        if(user != null)
        {

            //get user ID
            uid = user.getUid();

        }
        else
        {

        }

    }

}