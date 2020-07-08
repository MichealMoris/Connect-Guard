package com.genius.connectguard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {

    TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.home_tab);
        setCustomTabView();

        return view;
    }

    public void setCustomTabView(){

        final int[] tabIcon = {R.drawable.home_icon, R.drawable.cart_icon, R.drawable.admin_icon, R.drawable.settings_icon};

        for (int i = 0; i < tabIcon.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.custom_tab_view,null);
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            view.findViewById(R.id.tab_icon).setBackgroundResource(tabIcon[i]);
            if(tab!=null) {tab.setCustomView(view);}
        }

    }

}