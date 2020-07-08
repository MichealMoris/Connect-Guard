package com.genius.connectguard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeViewPager extends FragmentPagerAdapter {

    final List<Fragment> homeFragments = new ArrayList<>();
    final List<String> homeFragmentsTitle = new ArrayList<>();

    public HomeViewPager(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return homeFragments.get(position);
    }

    @Override
    public int getCount() {
        return homeFragmentsTitle.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    public void addFragment(Fragment fragment, String title){

        homeFragments.add(fragment);
        homeFragmentsTitle.add(title);

    }

}
