package com.genius.connectguard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

public class MainFragment extends Fragment {

    TabLayout tabLayout;
    HomeViewPager homeViewPager;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
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
        homeViewPager.addFragment(new AdminFragment(), "Admin Panel");
        homeViewPager.addFragment(new SettingsFragment(), "Settings");

        viewPager.setAdapter(homeViewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.home_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.cart_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.admin_icon);
        tabLayout.getTabAt(3).setIcon(R.drawable.settings_icon);

        return view;
    }
}