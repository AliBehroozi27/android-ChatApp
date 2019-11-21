package com.example.ruzbeh.chatapp.Activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.ruzbeh.chatapp.Adapters.SignUpVPAdapter;
import com.example.ruzbeh.chatapp.R;

public class SignInActivity extends AppCompatActivity {


    private ViewPager vpSignUp;
    SignUpVPAdapter signUpVPAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        vpSignUp = findViewById(R.id.vp_signUp);
        signUpVPAdapter = new SignUpVPAdapter(getSupportFragmentManager());
        vpSignUp.setCurrentItem(2);
        vpSignUp.setAdapter(signUpVPAdapter);
    }

    public void pageViewPager(){
        vpSignUp.setCurrentItem(1);
    }

}
