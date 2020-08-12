package com.genius.connectguard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.genius.constants.constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SettingsFragment extends Fragment {

    TextView accountSettings;
    private View view ;
    private ImageView profile_image_in_settings;
    private TextView profile_name_in_settings;
    private TextView profile_email_in_settings;
    private LinearLayout cover_in_settings;
    private TextView sign_in_text_in_settings;
    private Button logOutBtn ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        cover_in_settings = view.findViewById(R.id.cover_in_settings);

        accountSettings = view.findViewById(R.id.tv_accountSettings);
        accountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragemnt(new AccountSettingFragment(), R.id.settings_framlayout);
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

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
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
}