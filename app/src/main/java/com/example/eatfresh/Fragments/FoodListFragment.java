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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FoodListFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private Button addItemBtn;
    private Button logOutBtn;
    private ListView foodListView;

    private static final String TAG = "YOUR-TAG-NAME";

    private String uid;
    private ItemData itemData;
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
        logOutBtn = rootView.findViewById(R.id.logOutBtn);
        foodListView = rootView.findViewById(R.id.foodListView);

        addItemBtn.setOnClickListener(this);
        logOutBtn.setOnClickListener(this);

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
                                itemDataArrayList.sort(Comparator.comparing(ItemData::getExpiryDate)); //sort the items list by the expiry date
                                ItemDataListAdapter itemDataListAdapter = new ItemDataListAdapter(getContext(), R.layout.row, itemDataArrayList);
                                foodListView.setAdapter(itemDataListAdapter);

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();

                //get exercise clicked adapter (it gets the ID from the document of respective item clicked from Cloud Firestore)
                ItemData itemData1 = (ItemData) foodListView.getAdapter().getItem(position);
                String itemClickedId = itemData1.getItemId();

                //This sends the document selected id
                bundle.putString("itemClickedId", itemClickedId);
                Navigation.findNavController(rootView).navigate(R.id.action_foodListFragment_to_itemFragment, bundle);

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
            case R.id.logOutBtn:
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(view).navigate(R.id.action_foodListFragment_to_loginFragment);
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