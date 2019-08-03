package com.indramahkota.moviecatalogue.ui.main.fragment.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.indramahkota.moviecatalogue.R;

public class FavoriteTvShowFragment extends Fragment {
    private static final String STATE_SCROLL = "state_scroll";

    private LinearLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private Integer scrollPosition = 0;
    private RecyclerView rvTvShows;
    private RelativeLayout relativeLayout;

    public static FavoriteTvShowFragment newInstance(String title) {
        FavoriteTvShowFragment fragment = new FavoriteTvShowFragment();

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt(STATE_SCROLL);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        scrollPosition = linearLayoutManager.findFirstVisibleItemPosition();
        outState.putInt(STATE_SCROLL, scrollPosition);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.setVisibility(View.GONE);

        linearLayoutManager = new LinearLayoutManager(view.getContext());
        rvTvShows = view.findViewById(R.id.rv_category);
        rvTvShows.setLayoutManager(linearLayoutManager);
        rvTvShows.setHasFixedSize(true);

        relativeLayout = view.findViewById(R.id.favorite_empty_indicator);
        relativeLayout.setVisibility(View.GONE);

        /*FavoriteTvShowViewModel favoriteTvShowViewModel = ViewModelProviders.of(this).get(FavoriteTvShowViewModel.class);
        favoriteTvShowViewModel.getListFavoriteTvShow().observe(this, new Observer<List<FavoriteTvShow>>() {
            @Override
            public void onChanged(List<FavoriteTvShow> favoriteTvShows) {
                FavoriteTvShowAdapter favoriteTvShowAdapter = new FavoriteTvShowAdapter(favoriteTvShows, getContext());
                rvTvShows.setAdapter(favoriteTvShowAdapter);
                linearLayoutManager.scrollToPosition(scrollPosition);
                mShimmerViewContainer.setVisibility(View.GONE);

                if(favoriteTvShows.size() > 0) {
                    relativeLayout.setVisibility(View.GONE);
                } else {
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmer();
    }
}