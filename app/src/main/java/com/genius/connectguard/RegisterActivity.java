package com.genius.connectguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.genius.constants.constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        constants.initFireBase();
        startFragment(new MainFragment());


    }

    //This Method Is To Toggle Between SignUp, SignIn And Home Fragments
    private void startFragment(Fragment fragment)
    {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.register_framelayout,fragment)
                .disallowAddToBackStack()
                .commit();
    }
}