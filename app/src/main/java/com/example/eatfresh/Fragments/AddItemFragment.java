package com.example.eatfresh.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eatfresh.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AddItemFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private TextView expiryDateTextView;
    private Button addItemButton;
    private EditText itemName;

    private static final String TAG = "YOUR-TAG-NAME";

    private int dayCloud, monthCloud, yearCloud;
    private String uid;
    private String itemNameCloud;
    private String KEY_UID = "uid";
    private String KEY_ITEM_NAME = "itemName";
    private String KEY_DAY = "day";
    private String KEY_MONTH = "month";
    private String KEY_YEAR = "year";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AddItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);

        mAuth = FirebaseAuth.getInstance();

        expiryDateTextView = rootView.findViewById(R.id.expiryDateTextView);
        addItemButton = rootView.findViewById(R.id.addItemButton);
        itemName = rootView.findViewById(R.id.itemName);

        //TODO onStart() probably shouldn't be called here
        onStart();

        addItemButton.setOnClickListener(this);

        expiryDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), R.style.ThemeOverlay_MaterialComponents_Dialog, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;

                Log.d(TAG, "onDateSet: dd/mm/yyyy :" + dayOfMonth + "/" + month + "/" + year);

                String date = dayOfMonth + "/" + month + "/" + year;
                expiryDateTextView.setText(date);

                dayCloud = dayOfMonth;
                monthCloud = month;
                yearCloud = year;
            }
        };

        return rootView;
    }

    private void addItem()
    {
        //Write weight value to Cloud Firestore
        Map<String, Object> foodItem = new HashMap<>();
        //user.put(KEY_WEIGHT, Double.valueOf(measurement.getWeight()));
        foodItem.put(KEY_UID, uid);
        foodItem.put(KEY_ITEM_NAME, itemNameCloud);
        foodItem.put(KEY_DAY, dayCloud);
        foodItem.put(KEY_MONTH, monthCloud);
        foodItem.put(KEY_YEAR, yearCloud);

        db.collection("foodItems")
                .add(foodItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

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
            case R.id.addItemButton:

                itemNameCloud = itemName.getText().toString();
                addItem();

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