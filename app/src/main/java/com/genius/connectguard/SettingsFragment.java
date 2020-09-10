package com.genius.connectguard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.genius.constants.constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.Locale;

import io.paperdb.Paper;

public class SettingsFragment extends Fragment {

    TextView accountSettings;
    private View view ;
    private ImageView profile_image_in_settings;
    private TextView profile_name_in_settings;
    private TextView profile_email_in_settings;
    private TextView myOrdersTextView;
    private LinearLayout cover_in_settings;
    private TextView sign_in_text_in_settings;
    private Button logOutBtn ;
    private RadioButton englishLanguage;
    private RadioButton arabicLanguage;
    private RadioGroup languagesGroup;
    private TextView lowSdkText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out_anim);
                    fragmentTransaction.replace(R.id.register_framelayout, new MainFragment());
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });

        final SharedPreferences preferences = getActivity().getSharedPreferences("appLanguage", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        if (preferences.getString("langCode", "en").equals("ar")){

            view.setRotationY(180f);

        }

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {

            languagesGroup = view.findViewById(R.id.languageGroup);
            lowSdkText = view.findViewById(R.id.low_sdk_text);
            languagesGroup.setVisibility(View.GONE);
            lowSdkText.setVisibility(View.VISIBLE);

        }else{

            languagesGroup = view.findViewById(R.id.languageGroup);
            lowSdkText = view.findViewById(R.id.low_sdk_text);
            languagesGroup.setVisibility(View.VISIBLE);
            lowSdkText.setVisibility(View.GONE);

        }

        englishLanguage = view.findViewById(R.id.english_radio_button);
        englishLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("langCode", "en");
                editor.apply();
                restartApp(view.getContext());

            }
        });

        arabicLanguage = view.findViewById(R.id.arabic_radio_button);
        arabicLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("langCode", "ar");
                editor.apply();
                restartApp(view.getContext());

            }
        });

        if (preferences.getString("langCode", "en").equals("en")){

            englishLanguage.setChecked(true);

        }else if (preferences.getString("langCode", "en").equals("ar")){

            arabicLanguage.setChecked(true);

        }

        cover_in_settings = view.findViewById(R.id.cover_in_settings);

        accountSettings = view.findViewById(R.id.tv_accountSettings);
        accountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragemnt(new AccountSettingFragment(), R.id.register_framelayout);
            }
        });

        myOrdersTextView = view.findViewById(R.id.tv_my_orders);
        myOrdersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragemnt(new MyOrdersFragment(), R.id.register_framelayout);

            }
        });

        if(!constants.getUId(getActivity()).equals("empty")){

            setData(view);
            cover_in_settings.setVisibility(View.GONE);

        }else {

            cover_in_settings.setVisibility(View.VISIBLE);
            sign_in_text_in_settings = view.findViewById(R.id.sign_in_text_in_settings);
            sign_in_text_in_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setFragemnt(new SignInFragment(), R.id.register_framelayout);

                }
            });

        }


        return view;
    }


    private void setFragemnt(Fragment fragment, int repTo) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out_anim);
        fragmentTransaction.replace(repTo, fragment);
        fragmentTransaction.commit();

    }

    private void setData(View view){

        profile_image_in_settings = view.findViewById(R.id.profile_image_in_settings);
        profile_name_in_settings = view.findViewById(R.id.profile_name_in_settings);
        profile_email_in_settings = view.findViewById(R.id.profile_email_in_settings);
        constants.getDatabaseReference().child("Users").child(constants.getUId(getActivity())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String profile_image_uri = snapshot.child("userImage").getValue().toString();
                String profile_name = snapshot.child("name").getValue().toString();
                String profile_email = snapshot.child("email").getValue().toString();

                Picasso.get().load(profile_image_uri).into(profile_image_in_settings);
                profile_name_in_settings.setText(profile_name);
                profile_email_in_settings.setText(profile_email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        }
    }

    private void initViews()
    {
        logOutBtn = view.findViewById(R.id.logOut_btn);
        logOutBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                constants.getAuth().signOut();
                constants.saveUId(requireActivity(),"empty");
                constants.replaceFragment(SettingsFragment.this,new SignInFragment(),false);
            }
        });


    }

    private void restartApp(Context context) {

        Intent intent = new Intent(context, RegisterActivity.class);
        getActivity().overridePendingTransition(R.anim.fade_out_anim, R.anim.fade_in_anim);
        startActivity(intent);
        getActivity().finish();
    }

}