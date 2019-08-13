package com.indramahkota.moviecatalogue.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.indramahkota.moviecatalogue.R;
import com.indramahkota.moviecatalogue.data.source.locale.entity.TvShowEntity;
import com.indramahkota.moviecatalogue.factory.ViewModelFactory;
import com.indramahkota.moviecatalogue.ui.main.adapter.TvShowAdapter;
import com.indramahkota.moviecatalogue.ui.main.fragment.pagination.PaginationScrollListener;
import com.indramahkota.moviecatalogue.ui.main.fragment.viewmodel.TvShowFragmentViewModel;
import com.indramahkota.moviecatalogue.ui.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class TvShowFragment extends Fragment {
    private static final String STATE_PAGE = "state_page";
    private static final String STATE_SCROLL = "state_scroll";
    private static final String STATE_DISCOVER_TV_SHOW_RESPONSE = "state_discover_tv_show_response";

    @Inject
    ViewModelFactory viewModelFactory;

    private boolean isLoading;
    private Long currentPage = 1L;
    private Integer scrollPosition = 0;
    private RecyclerView rvFragmentTvShows;
    private List<TvShowEntity> discoverTvShows;
    private ShimmerFrameLayout mShimmerViewContainer;
    private LinearLayoutManager linearLayoutManager;
    private SearchView searchView;
    private View rootView;

    public TvShowFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getLong(STATE_PAGE);
            scrollPosition = savedInstanceState.getInt(STATE_SCROLL);
            discoverTvShows = savedInstanceState.getParcelableArrayList(STATE_DISCOVER_TV_SHOW_RESPONSE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_tv_container, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = view.findViewById(R.id.rv_fragment_category_container);

        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_fragment_container);

        TvShowFragmentViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(TvShowFragmentViewModel.class);

        linearLayoutManager = new LinearLayoutManager(view.getContext());
        rvFragmentTvShows = view.findViewById(R.id.rv_fragment_category);
        rvFragmentTvShows.setLayoutManager(linearLayoutManager);
        rvFragmentTvShows.setHasFixedSize(true);

        rvFragmentTvShows.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public boolean isLastPage() {
                return viewModel.isLastPage();
            }

            @Override
            public void loadMore() {
                currentPage++;
                isLoading = true;
                viewModel.loadMoreTvShows(currentPage);
                Toast.makeText(getContext(), "Page: " + currentPage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        TvShowAdapter listTvShowAdapter = new TvShowAdapter(new ArrayList<>(), getContext());
        listTvShowAdapter.notifyDataSetChanged();
        rvFragmentTvShows.setAdapter(listTvShowAdapter);

        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent moveToSearchActivity = new Intent(getContext(), SearchActivity.class);
                String[] extraData = {"Tv Show", query};
                moveToSearchActivity.putExtra(SearchActivity.EXTRA_SEARCH_QUERY, extraData);
                startActivity(moveToSearchActivity);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1L;
            listTvShowAdapter.clear();
            viewModel.loadMoreTvShows(currentPage);
            mShimmerViewContainer.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Page: " + currentPage, Toast.LENGTH_SHORT).show();
        });

        viewModel.listDiscoverTvShow.observe(this, discoverTvShowResponseResource -> {
            switch (discoverTvShowResponseResource.status) {
                case SUCCESS:
                    //show data
                    swipeRefreshLayout.setRefreshing(false);
                    rvFragmentTvShows.setVisibility(View.VISIBLE);
                    mShimmerViewContainer.setVisibility(View.GONE);
                    if (discoverTvShowResponseResource.data != null) {
                        if(discoverTvShows == null) {
                            discoverTvShows = new ArrayList<>(discoverTvShowResponseResource.data.getResults());
                        } else {
                            discoverTvShows.addAll(discoverTvShowResponseResource.data.getResults());
                        }
                        listTvShowAdapter.addAll(discoverTvShowResponseResource.data.getResults());
                        isLoading = false;
                    }
                    break;
                case ERROR:
                    //show error
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        if(discoverTvShows != null) {
            listTvShowAdapter.addAll(discoverTvShows);
            linearLayoutManager.scrollToPosition(scrollPosition);
            mShimmerViewContainer.setVisibility(View.GONE);
        } else {
            viewModel.loadMoreTvShows(currentPage);
            Toast.makeText(getContext(), "Page: " + currentPage, Toast.LENGTH_SHORT).show();
        }
    }

    public void scrollToTop() {
        rvFragmentTvShows.smoothScrollToPosition(0);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        scrollPosition = linearLayoutManager.findFirstVisibleItemPosition();
        outState.putInt(STATE_SCROLL, scrollPosition);
        outState.putLong(STATE_PAGE, currentPage);

        if(discoverTvShows != null) {
            ArrayList<TvShowEntity> helper = new ArrayList<>();
            int len = discoverTvShows.size();
            for(int i = 0; i<len; ++i) {
                helper.add(discoverTvShows.get(i));
            }
            outState.putParcelableArrayList(STATE_DISCOVER_TV_SHOW_RESPONSE, helper);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
        searchView.setQuery("", false);
        rootView.requestFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmer();
    }
}
