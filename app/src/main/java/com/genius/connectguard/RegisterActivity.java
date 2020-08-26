package com.genius.connectguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.genius.constants.constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SharedPreferences preferences = getSharedPreferences("appLanguage", Context.MODE_PRIVATE);

        Locale locale = new Locale(preferences.getString("langCode", "en"));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

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