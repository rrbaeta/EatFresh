package com.example.eatfresh.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatfresh.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddItemFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private TextView expiryDateTextView;
    private Button addItemButton;
    private EditText itemName;

    private static final String TAG = "YOUR-TAG-NAME";

    private String uid;
    private String itemNameCloud;
    private Timestamp expiryDate = null;
    private String KEY_UID = "uid";
    private String KEY_ITEM_NAME = "itemName";
    private String KEY_EXPIRY_DATE = "expiryDate";

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

                //This creates a Timestamp which can be used to save as a number in the database and to subtract dates
                expiryDate = new Timestamp(new Date(year - 1900, month - 1, dayOfMonth));

                String date = dayOfMonth + "/" + month + "/" + year;
                expiryDateTextView.setText(date);
            }
        };

        return rootView;
    }

    private void addItem()
    {
        //Write weight value to Cloud Firestore
        Map<String, Object> foodItem = new HashMap<>();
        foodItem.put(KEY_UID, uid);
        foodItem.put(KEY_ITEM_NAME, itemNameCloud);
        foodItem.put(KEY_EXPIRY_DATE, expiryDate.getSeconds());

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

                if(itemName.getText().toString().trim().length() > 0 && expiryDate != null)
                {
                    itemNameCloud = itemName.getText().toString();
                    addItem();
                    Navigation.findNavController(view).navigate(R.id.action_addItemFragment_to_foodListFragment);
                }
                else if (itemName.getText().toString().trim().length() == 0 && expiryDate != null)
                {
                    Toast.makeText(getContext(), "Insert an item name.", Toast.LENGTH_SHORT).show();
                }
                else if (itemName.getText().toString().trim().length() > 0 && expiryDate == null)
                {
                    Toast.makeText(getContext(), "Select an expiry date", Toast.LENGTH_SHORT).show();
                }
                else if (itemName.getText().toString().trim().length() == 0 && expiryDate == null)
                {
                    Toast.makeText(getContext(), "Insert an item name and an expiry date", Toast.LENGTH_SHORT).show();
                }

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