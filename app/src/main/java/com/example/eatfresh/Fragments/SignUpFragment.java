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
import android.widget.EditText;
import android.widget.Toast;

import com.example.eatfresh.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUpFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private Button signupBtn2;
    private EditText userEmail2;
    private EditText userPassword2;

    private String emailInput;
    private String passwordInput;

    private static final String TAG = "YOUR-TAG-NAME";

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mAuth = FirebaseAuth.getInstance();

        signupBtn2 = rootView.findViewById(R.id.signupBtn2);
        userEmail2 = rootView.findViewById(R.id.userEmail2);
        userPassword2 = rootView.findViewById(R.id.userPassword2);

        signupBtn2.setOnClickListener(this);

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
            case R.id.signupBtn2:

                emailInput = userEmail2.getText().toString();
                passwordInput = userPassword2.getText().toString();

                createAccount(emailInput, passwordInput);

                Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_loginFragment);

                break;
        }

    }

    public void createAccount(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                    //Navigation needs to be done in this if statement
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(getContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }

                // ...
            }
        });

    }

}