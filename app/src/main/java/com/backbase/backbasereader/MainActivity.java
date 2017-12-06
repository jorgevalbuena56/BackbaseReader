package com.backbase.backbasereader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.backbase.backbasereader.listener.ListEvents;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, ListEvents {

    private CityListFragment cityListFragment;
    private MapFragment mapFragment;
    private MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureHomeToolbar();
        cityListFragment = new CityListFragment();
        swapFragments(cityListFragment);
    }

    /**
     * Change back and forth the CityListFragment and the Map Fragment
     * @param fragment
     */
    private void swapFragments(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Configures the CityList toolbar to display the search
     */
    private void configureHomeToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            if (searchItem != null) {
                searchItem.setVisible(true);
                SearchView searchView = (SearchView)searchItem.getActionView();
                searchView.setQuery("",false); //clear the text
                searchView.setIconified(true);//close the search editor and make search icon again
            }
        }
    }

    /**
     * Configures the Map Toolbar with the back button and the title of the city
     * @param title
     */
    private void configureMapToolbar(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(title);
            searchItem.setVisible(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView)searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        cityListFragment.search(newText);
        return true;
    }

    @Override
    public void onCityClicked(String name, LatLng position) {
        if (mapFragment == null) {
            mapFragment = new MapFragment();
        }
        mapFragment.setPositionToZoom(position);
        configureMapToolbar(name);
        swapFragments(mapFragment);
    }

    @Override
    public void hideProgressBar() {
        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgressBar() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
            configureHomeToolbar();
        } else if (fm.getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
