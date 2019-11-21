package com.example.ruzbeh.chatapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
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
import android.widget.Toast;

import com.example.ruzbeh.chatapp.Activities.MainActivity;
import com.example.ruzbeh.chatapp.Activities.SignInActivity;
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
import java.util.jar.Attributes;

public class LoginFragment extends Fragment {
    EditText Email, Password;
    Button LogInButton, RegisterButton;
    FirebaseAuth mAuth;
    String email, password;
    ProgressDialog dialog;



    public LoginFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogInButton = (Button) view.findViewById(R.id.b_login);
        RegisterButton = (Button) view.findViewById(R.id.b_signUp);
        Email = (EditText) view.findViewById(R.id.et_email);
        Password = (EditText) view.findViewById(R.id.et_password);
        dialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();



        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSign();
            }
        });
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SignInActivity) getActivity()).pageViewPager();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    private void userSign() {
        email = Email.getText().toString().trim();
        password = Password.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Enter the correct Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Enter the correct password", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.setMessage("Logging in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    checkIfEmailVerified();
                }
            }
        });
    }

    private void checkIfEmailVerified() {
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        boolean emailVerified = users.isEmailVerified();
        if (!emailVerified) {
            Toast.makeText(getActivity(), "Verify the Email Id", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            ((SignInActivity)getActivity()).finish();
        } else {
            Email.getText().clear();
            Password.getText().clear();

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("user_id", users.getUid() + "");
            intent.putExtra("logged_in" , true);
            startActivity(intent);
        }
    }


}
