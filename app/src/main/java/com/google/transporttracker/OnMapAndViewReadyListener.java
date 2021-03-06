package com.google.transporttracker;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class OnMapAndViewReadyListener implements ViewTreeObserver.OnGlobalLayoutListener,OnMapReadyCallback{

    public interface OnGlobalLayoutAndMapReadyListener {
        void onMapReady(GoogleMap googleMap);
    }

    private final SupportMapFragment mapFragment;
    private final View mapView;
    private final OnGlobalLayoutAndMapReadyListener devCallback;

    private boolean isViewReady;
    private boolean isMapReady;
    private GoogleMap googleMap;


    public OnMapAndViewReadyListener(
            SupportMapFragment mapFragment, OnGlobalLayoutAndMapReadyListener devCallback) {
        this.mapFragment = mapFragment;
        mapView = mapFragment.getView();
        this.devCallback = devCallback;
        isViewReady = false;
        isMapReady = false;
        googleMap = null;

        registerListeners();
    }

    private void registerListeners() {
        // View layout.
        if ((mapView.getWidth() != 0) && (mapView.getHeight() != 0)) {
            // View has already completed layout.
            isViewReady = true;
        } else {
            // Map has not undergone layout, register a View observer.
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        // GoogleMap. Note if the GoogleMap is already ready it will still fire the callback later.
        mapFragment.getMapAsync(this);
    }



    @SuppressWarnings("deprecation")  // We use the new method when supported
    @SuppressLint("NewApi")
    @Override
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        } else {
            mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        isViewReady = true;
        fireCallbackIfReady();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        isMapReady = true;
        fireCallbackIfReady();

    }

    private void fireCallbackIfReady() {
        if (isViewReady && isMapReady) {
            devCallback.onMapReady(googleMap);
        }
    }
}
