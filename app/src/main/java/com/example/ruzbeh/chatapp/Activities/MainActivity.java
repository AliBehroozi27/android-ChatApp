package com.example.ruzbeh.chatapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.ruzbeh.chatapp.R;

public class MainActivity extends AppCompatActivity {

    String user_id;
    Boolean logged_in ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        logged_in = intent.getBooleanExtra("logged_in" , false);


        if (user_id == null) {
            Toast.makeText(this , "IF" , Toast.LENGTH_LONG).show();
            Log.e("IIIFFF", "If");
            startActivity(new Intent(this, SignInActivity.class));
        } else {
            Toast.makeText(this , "ELSE" , Toast.LENGTH_LONG).show();
            Log.e("IIIFFF", "Else");
            startActivity(new Intent(this , ChatListActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("iiifff" , "resume");
    }
}
