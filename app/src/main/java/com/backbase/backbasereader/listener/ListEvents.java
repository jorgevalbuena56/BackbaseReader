package com.backbase.backbasereader.listener;

import com.google.android.gms.maps.model.LatLng;

/**
 * Callback Interface to communicate click events on the recycler view items
 */

public interface ListEvents {
    void onCityClicked(String name, LatLng position);
    void hideProgressBar();
    void showProgressBar();
}
