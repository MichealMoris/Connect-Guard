package com.genius.connectguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.genius.constants.constants;

public class RegisterActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        constants.initFireBase();

        if (!constants.getUId(this).equals("empty"))
        {
            startFragment(new HomeFragment());

        }else
        {
            startFragment(new SignInFragment());
        }

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