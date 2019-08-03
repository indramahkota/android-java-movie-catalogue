package com.indramahkota.moviecatalogue.ui.main.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private final String[] listTitle;
    private final List<Fragment> listFragment;

    public TabPagerAdapter(FragmentManager fm, List<Fragment> listFragment, String[] listTitle) {
        super(fm);
        this.listTitle = listTitle;
        this.listFragment = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle[position];
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }
}