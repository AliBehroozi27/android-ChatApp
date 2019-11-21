package com.example.ruzbeh.chatapp.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruzbeh.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    EditText username, email, password;
    TextView mLoginPageBack;
    Button bSignUp;
    FirebaseAuth mAuth;
    String Name, Email, Password;
    ProgressDialog mDialog;

    private String EMAIL = "email";
    private String USER_ID = "user_id";
    private String USERNAME = "username";
    private String PASSWORD = "password";


    FirebaseFirestore db;


    public SignUpFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(getContext());
        bSignUp = (Button) view.findViewById(R.id.b_su_signUp);
        username = (EditText) view.findViewById(R.id.et_su_username);
        email = (EditText) view.findViewById(R.id.et_su_email);
        password = (EditText) view.findViewById(R.id.et_su_password);
        db = FirebaseFirestore.getInstance();
        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRegister();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    private void UserRegister() {

        Name = username.getText().toString().trim();
        Email = email.getText().toString().trim();
        Password = password.getText().toString().trim();

        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(getActivity(), "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Email)) {
            Toast.makeText(getActivity(), "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Password)) {
            Toast.makeText(getActivity(), "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        } else if (Password.length() < 6) {
            Toast.makeText(getActivity(), "Password must be greater then 6 digit", Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.setMessage("Creating User please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendEmailVerification();
                    createUserInBackend();
                    mDialog.dismiss();
                    //mAuth.signOut();
                } else {
                    mDialog.dismiss();
                    Toast.makeText(getActivity(), task.getException() + "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Check your Email for verification", Toast.LENGTH_SHORT).show();
                        //FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }

    private void createUserInBackend() {
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        String User_id = "kir";
        if (users != null) {
            User_id = users.getUid();
        } else {
            Toast.makeText(getContext(), "ZERESHK",
                    Toast.LENGTH_LONG).show();
        }
        Map<String, Object> user = new HashMap<>();
        user.put(EMAIL, email.getText().toString());
        user.put(USERNAME, username.getText().toString());
        user.put(PASSWORD, password.getText().toString());
        user.put(USER_ID, User_id);

        db.collection("users").document(User_id).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "added",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "ERROR" + e.toString(),
                                Toast.LENGTH_LONG).show();
                        Log.d("TAG", e.toString());
                    }
                });
    }
}


