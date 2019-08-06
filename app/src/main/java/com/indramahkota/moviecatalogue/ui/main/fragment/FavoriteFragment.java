package com.indramahkota.moviecatalogue.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.indramahkota.moviecatalogue.R;
import com.indramahkota.moviecatalogue.ui.main.adapter.TabPagerAdapter;
import com.indramahkota.moviecatalogue.ui.main.fragment.favorite.FavoriteMovieFragment;
import com.indramahkota.moviecatalogue.ui.main.fragment.favorite.FavoriteTvShowFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.android.support.AndroidSupportInjection;

public class FavoriteFragment extends Fragment {
    public FavoriteFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] listTitle = {getResources().getString(R.string.movies), getResources().getString(R.string.tv_shows)};

        List<Fragment> listFragment = new ArrayList<>();
        listFragment.add(FavoriteMovieFragment.newInstance(listTitle[0]));
        listFragment.add(FavoriteTvShowFragment.newInstance(listTitle[1]));

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager(), listFragment, listTitle);
        tabPagerAdapter.notifyDataSetChanged();

        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(tabPagerAdapter);

        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}