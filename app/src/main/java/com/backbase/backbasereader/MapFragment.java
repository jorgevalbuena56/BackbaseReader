package com.backbase.backbasereader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Map Fragment implementation
 */

public class MapFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {
    private LatLng positionToZoom;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        android.support.v4.app.Fragment mapFragment =
                getChildFragmentManager().findFragmentById(R.id.google_map_fragment);
        if (mapFragment != null) {
            ((SupportMapFragment) mapFragment).getMapAsync(this);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_view, container, false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (positionToZoom != null) {
            CameraPosition cameraPosition =
                    new CameraPosition.Builder().target(positionToZoom).zoom(10.9f).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void setPositionToZoom(LatLng position) {
        this.positionToZoom = position;
    }
}