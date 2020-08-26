package com.genius.connectguard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.service.autofill.VisibilitySetterAction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.genius.constants.constants;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainFragment extends Fragment {

    TabLayout tabLayout;
    HomeViewPager homeViewPager;
    ViewPager viewPager;
    ProgressBar homeProgressBar;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);

        final SharedPreferences preferences = getActivity().getSharedPreferences("appLanguage", Context.MODE_PRIVATE);

        ConnectivityManager connectivityManager = (ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo dataInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (!wifiInfo.isConnectedOrConnecting() && !dataInfo.isConnectedOrConnecting()) {

            setFragemnt(new NoInternetConnectionFragment());

        } else {

            homeProgressBar = view.findViewById(R.id.homeProgressBar);

            if (!constants.getUId(getActivity()).equals("empty")) {

                constants.getDatabaseReference().child("Users").child(constants.getUId(getActivity())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot snapshot) {

                        if (snapshot.child("email").getValue().toString().equals("mesho.moris@gmail.com")) {

                            final int[] tabIcon = {R.drawable.home_icon, R.drawable.cart_icon, R.drawable.admin_icon, R.drawable.settings_icon};

                            tabLayout = view.findViewById(R.id.home_tab);
                            for (int i = 0; i < tabIcon.length; i++) {
                                View view2 = getLayoutInflater().inflate(R.layout.custom_tab_view, null);
                                TabLayout.Tab tab = tabLayout.getTabAt(i);
                                view2.findViewById(R.id.tab_icon).setBackgroundResource(tabIcon[i]);
                                if (tab != null) {
                                    tab.setCustomView(view2);
                                }
                            }

                            viewPager = view.findViewById(R.id.home_viewpager);
                            homeViewPager = new HomeViewPager(getActivity().getSupportFragmentManager());

                            homeViewPager.addFragment(new HomeFragment(), "Home");
                            homeViewPager.addFragment(new CartFragment(), "Cart");
                            homeViewPager.addFragment(new SettingsFragment(), "Settings");
                            homeViewPager.addFragment(new AdminFragment(), "Admin Panel");

                            viewPager.setAdapter(homeViewPager);
                            tabLayout.setupWithViewPager(viewPager);


                            tabLayout.getTabAt(0).setIcon(R.drawable.home_icon);
                            tabLayout.getTabAt(1).setIcon(R.drawable.cart_icon);
                            tabLayout.getTabAt(2).setIcon(R.drawable.settings_icon);
                            tabLayout.getTabAt(3).setIcon(R.drawable.admin_icon);

                        }else {

                            final int[] tabIcon = {R.drawable.home_icon, R.drawable.cart_icon, R.drawable.admin_icon, R.drawable.settings_icon};

                            tabLayout = view.findViewById(R.id.home_tab);
                            for (int i = 0; i < tabIcon.length; i++) {
                                View view2 = getLayoutInflater().inflate(R.layout.custom_tab_view, null);
                                TabLayout.Tab tab = tabLayout.getTabAt(i);
                                view2.findViewById(R.id.tab_icon).setBackgroundResource(tabIcon[i]);
                                if (tab != null) {
                                    tab.setCustomView(view2);
                                }
                            }

                            viewPager = view.findViewById(R.id.home_viewpager);
                            homeViewPager = new HomeViewPager(getActivity().getSupportFragmentManager());

                            homeViewPager.addFragment(new HomeFragment(), "Home");
                            homeViewPager.addFragment(new CartFragment(), "Cart");
                            homeViewPager.addFragment(new SettingsFragment(), "Settings");

                            viewPager.setAdapter(homeViewPager);
                            tabLayout.setupWithViewPager(viewPager);


                            tabLayout.getTabAt(0).setIcon(R.drawable.home_icon);
                            tabLayout.getTabAt(1).setIcon(R.drawable.cart_icon);
                            tabLayout.getTabAt(2).setIcon(R.drawable.settings_icon);

                            homeProgressBar.setVisibility(View.GONE);

                            if (preferences.getString("langCode", "en").equals("ar")){

                                viewPager.setRotationY(180f);

                            }


                        }



                        if (snapshot.exists()) {

                            homeProgressBar.setVisibility(View.GONE);

                        }


                        if (preferences.getString("langCode", "en").equals("ar")){

                            viewPager.setRotationY(180f);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } else {


                final int[] tabIcon = {R.drawable.home_icon, R.drawable.cart_icon, R.drawable.admin_icon, R.drawable.settings_icon};

                tabLayout = view.findViewById(R.id.home_tab);
                for (int i = 0; i < tabIcon.length; i++) {
                    View view2 = getLayoutInflater().inflate(R.layout.custom_tab_view, null);
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    view2.findViewById(R.id.tab_icon).setBackgroundResource(tabIcon[i]);
                    if (tab != null) {
                        tab.setCustomView(view2);
                    }
                }

                viewPager = view.findViewById(R.id.home_viewpager);
                homeViewPager = new HomeViewPager(getActivity().getSupportFragmentManager());

                homeViewPager.addFragment(new HomeFragment(), "Home");
                homeViewPager.addFragment(new CartFragment(), "Cart");
                homeViewPager.addFragment(new SettingsFragment(), "Settings");

                viewPager.setAdapter(homeViewPager);
                tabLayout.setupWithViewPager(viewPager);


                tabLayout.getTabAt(0).setIcon(R.drawable.home_icon);
                tabLayout.getTabAt(1).setIcon(R.drawable.cart_icon);
                tabLayout.getTabAt(2).setIcon(R.drawable.settings_icon);

                homeProgressBar.setVisibility(View.GONE);

                if (preferences.getString("langCode", "en").equals("ar")){

                    viewPager.setRotationY(180f);

                }

            }

        }


        return view;
    }



    private void setFragemnt(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out_anim);
        fragmentTransaction.replace(R.id.register_framelayout, fragment);
        fragmentTransaction.commit();

    }


}