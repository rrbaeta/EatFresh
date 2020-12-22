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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.eatfresh.Model.ItemData;
import com.example.eatfresh.Model.ItemDataListAdapter;
import com.example.eatfresh.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FoodListFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private Button addItemBtn;
    private ListView foodListView;

    private static final String TAG = "YOUR-TAG-NAME";

    private String uid;
    private ItemData itemData;
    private ArrayAdapter<ItemData> itemDataArrayAdapter;
    private ArrayList<ItemData> itemDataArrayList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FoodListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_food_list, container, false);

        mAuth = FirebaseAuth.getInstance();

        addItemBtn = rootView.findViewById(R.id.addItemBtn);
        foodListView = rootView.findViewById(R.id.foodListView);

        addItemBtn.setOnClickListener(this);

        //TODO onStart() probably shouldn't be called here
        onStart();

        itemData = new ItemData();

        if(itemDataArrayList != null)
        {
            itemDataArrayList.clear();
        }


        //Cloud Firestore foodItems to array adapter
        db.collection("foodItems")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                ItemData itemData = document.toObject(ItemData.class);
                                itemData.setItemId(document.getId());

                                itemDataArrayList.add(itemData);

                                //Custom Array Adapter
                                ItemDataListAdapter itemDataListAdapter = new ItemDataListAdapter(getContext(), R.layout.row, itemDataArrayList);
                                foodListView.setAdapter(itemDataListAdapter);

                                //Long dateTs = itemData.getExpiryDate();
                                //itemDataArrayAdapter = new ArrayAdapter(getContext(), R.layout.row, R.id.list_content, itemDataArrayList);
                                //foodListView.setAdapter(itemDataArrayAdapter);

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
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

    private void updateUI(FirebaseUser user){
        if(user != null)
        {

            //get user ID
            uid = user.getUid();

        }
        else
        {
            //If no user is signed in
        }

    }

}