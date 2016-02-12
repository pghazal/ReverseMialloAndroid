package com.pghazal.reversemiallo.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pghazal.reversemiallo.fragment.FriendsFragment;
import com.pghazal.reversemiallo.fragment.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"Friends", "Settings"};
    private Context context;
    private List<Fragment> fragmentList;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

        fragmentList = new ArrayList<>();
        fragmentList.add(FriendsFragment.newInstance());
        fragmentList.add(SettingsFragment.newInstance());
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}