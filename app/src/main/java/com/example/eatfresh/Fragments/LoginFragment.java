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

import com.example.eatfresh.Activity.MainActivity;
import com.example.eatfresh.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private Button signupBtn;
    private Button signinBtn;
    private EditText userEmail;
    private EditText userPassword;

    private String emailInput;
    private String passwordInput;

    private static final String TAG = "YOUR-TAG-NAME";

    public String msg_token_fmt;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();

        signupBtn = rootView.findViewById(R.id.signupBtn);
        signinBtn = rootView.findViewById(R.id.signinBtn);
        userEmail = rootView.findViewById(R.id.userEmail);
        userPassword = rootView.findViewById(R.id.userPassord);

        signupBtn.setOnClickListener(this);
        signinBtn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser, getView());
    }

    //all onClicks
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.signupBtn:
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment);
                break;

            case R.id.signinBtn:

                emailInput = userEmail.getText().toString();
                passwordInput = userPassword.getText().toString();

                signIn(emailInput, passwordInput);

                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_foodListFragment);

                break;
        }

    }

    public void signIn (String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            //Navigation needs to be done in this if statement

                            Toast.makeText(getContext(), "Authentication success.", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user, View view){
        if(user != null)
        {

            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_foodListFragment);

        }
    }

}