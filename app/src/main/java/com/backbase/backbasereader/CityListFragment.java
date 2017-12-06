package com.backbase.backbasereader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.backbase.backbasereader.adapter.CityAdapter;
import com.backbase.backbasereader.listener.EndlessRecyclerViewScrollListener;
import com.backbase.backbasereader.listener.ListEvents;
import com.backbase.backbasereader.model.City;
import com.backbase.backbasereader.model.Trie;
import com.backbase.backbasereader.model.TrieNode;
import com.backbase.backbasereader.parser.CitiesReader;
import com.backbase.backbasereader.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Fragment displaying the list of cities
 */

public class CityListFragment extends android.support.v4.app.Fragment {
    private CityAdapter cityAdapter;
    private RecyclerView mainRV;
    private ConcurrentSkipListSet<City> originalCityList;
    private ListEvents mItemClickCallback;

    private static final String TAG = CityListFragment.class.getName();
    private static final int GLOBAL_PAGE_SIZE = 500;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.city_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainRV = (RecyclerView) view;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        mainRV.setLayoutManager(linearLayoutManager);

        cityAdapter = new CityAdapter(mItemClickCallback);
        originalCityList = new ConcurrentSkipListSet<>();
        mItemClickCallback.showProgressBar();
        loadNextPage(mainRV.getContext(), 1);

        mainRV.setAdapter(cityAdapter);
        mainRV.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager){
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextPage(view.getContext(), page);
            }
        });
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mItemClickCallback = (ListEvents) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement CityClick ");
        }
    }
    // Append the next page of data into the adapter
    private void loadNextPage(Context context, int pageNumber) {
        try {
            new DisplayAsynchronous(context).execute(pageNumber);
        } catch (Exception e) {
            Log.e(TAG, "Error fetching city's information: " + e.getMessage());
        }
    }

    public void search(String query) {
        new SearchAsynchronous().execute(query);
    }
    /**
     * Asynchronous Task to load efficiently the infinity scroll results
     *
     * Context is passed as a parameter and store as a WeakReference to avoid memory leaks
     */
    @SuppressLint("StaticFieldLeak")
    public class DisplayAsynchronous extends AsyncTask<Integer, Void, Map<String, TreeSet<City>>> {
        private final String TAG = DisplayAsynchronous.class.getName();
        private CitiesReader citiesReader;
        private WeakReference<Context> context;

        DisplayAsynchronous(Context context) {
            this.citiesReader = new CitiesReader();
            this.context = new WeakReference<>(context);
        }

        @Override
        protected Map<String, TreeSet<City>> doInBackground(Integer... params) {
            try {
                return citiesReader.read(context.get(), params[0], GLOBAL_PAGE_SIZE);
            } catch (Exception e) {
                Log.e(TAG, "Error Loading the cities: " + e.getMessage());
            }
            return new HashMap<>();
        }

        @Override
        protected void onPostExecute(Map<String, TreeSet<City>> result) {
            List<City> cities = Utils.getListByTree(result);
            originalCityList.addAll(cities);
            Log.d(TAG, "Size of the full List is: " + originalCityList.size());
            cityAdapter.add(cities);
            mItemClickCallback.hideProgressBar();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    /**
     * Asynchronous Task to search efficiently
     *
     * Context is passed as a parameter and store as a WeakReference to avoid memory leaks
     */
    @SuppressLint("StaticFieldLeak")
    public class SearchAsynchronous extends AsyncTask<String, Void, List<City>> {
        private final String TAG = DisplayAsynchronous.class.getName();

        @Override
        protected List<City> doInBackground(String... params) {
            if (params[0].isEmpty()) {
                return new ArrayList<>(originalCityList);
            } else {
                return CitiesReader.filter(new ArrayList<>(originalCityList), params[0]);
            }
        }

        @Override
        protected void onPostExecute(List<City> result) {
            cityAdapter.replaceAll(result);
            mainRV.scrollToPosition(0);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
