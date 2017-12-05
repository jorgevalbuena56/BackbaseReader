package com.backbase.backbasereader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.backbase.backbasereader.adapter.CityAdapter;
import com.backbase.backbasereader.listener.EndlessRecyclerViewScrollListener;
import com.backbase.backbasereader.model.City;
import com.backbase.backbasereader.parser.CitiesReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    private List<City> citiesLoaded = new ArrayList<>();
    private CitiesReader citiesReader = new CitiesReader();
    private RecyclerView.Adapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.cities_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        cityAdapter = new CityAdapter(citiesLoaded);
        loadNextPage(1);
        recyclerView.setAdapter(cityAdapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager){
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                loadNextPage(page);
            }
        });
    }

    // Append the next page of data into the adapter
    public void loadNextPage(int pageNumber) {
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        try {
            int positionStart = citiesLoaded.size();
            Map<String, TreeSet<City>> queryCities = citiesReader.read(this, pageNumber, 50);
            for (String key : queryCities.keySet()) {
                TreeSet<City> cityNames = queryCities.get(key);
                citiesLoaded.addAll(new ArrayList<>(cityNames));
            }
            cityAdapter.notifyItemRangeChanged(positionStart + 1, citiesLoaded.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
