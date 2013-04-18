/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gdelight.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gdelight.R;
import com.gdelight.domain.item.ItemGroup;
import com.gdelight.domain.user.UserBean;
import com.gdelight.utils.constants.Constants;
import com.gdelight.widget.adapter.MapWindowAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nullwire.trace.ExceptionHandler;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Toast;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class WantMapActivity extends FragmentActivity implements OnMarkerClickListener, OnInfoWindowClickListener {

	private GoogleMap mMap;
	private UserBean user = null;
	private List<ItemGroup> groups = null;
	private Map<Marker, ItemGroup> mappedItems = new HashMap<Marker, ItemGroup>();

	public WantMapActivity() {
	}

	/** Called with the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//send trace back to base to be able to track issues
		ExceptionHandler.register(this, "http://www.tomansley.com/gdelight/trace.php"); 

		//get the user
		Bundle bundle = this.getIntent().getExtras();
		user = (UserBean) bundle.getSerializable(Constants.USER_BEAN);
		groups = (List<ItemGroup>) bundle.getSerializable(Constants.FIND_ITEMS);

		setContentView(R.layout.map);

		setUpMapIfNeeded();
	}


	/**
	 * Called when the activity is about to start interacting with the user.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		// show the zoom controls.
		mMap.getUiSettings().setZoomControlsEnabled(true);

		// Add markers to the map.
		addMarkersToMap();

		// Set listeners for marker events.
		mMap.setOnMarkerClickListener(this);
		
		//set listener for info window events (info window = box opened when marker clicked.)
		mMap.setOnInfoWindowClickListener(this);
		
		MapWindowAdapter mapInfoAdapter = new MapWindowAdapter(this, mappedItems);
		
		mMap.setInfoWindowAdapter(mapInfoAdapter);

		// Pan to see all markers in view.
		// Cannot zoom to bounds until the map has a size.
		final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
		if (mapView.getViewTreeObserver().isAlive()) {
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@SuppressLint("NewApi") // We check which build version we are using.
				@Override
				public void onGlobalLayout() {
					Builder b = LatLngBounds.builder();
					for (ItemGroup group: groups) {
						b.include(new LatLng(group.getLatitude(), group.getLongitude()));
					}
					LatLngBounds bounds = b.build();
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
						mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					} else {
						mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					}
					mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
				}
			});
		}
	}

	private void addMarkersToMap() {
		
		MarkerOptions markerOptions = null;
		Marker marker = null;
		for (ItemGroup group: groups) {
			
			markerOptions = new MarkerOptions();
			markerOptions.position(new LatLng(group.getLatitude(), group.getLongitude()));
			markerOptions.draggable(false);

			marker = mMap.addMarker(markerOptions);

			mappedItems.put(marker, group);
			
		}
	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		// We return false to indicate that we have not consumed the event and that we wish
		// for the default behavior to occur (which is for the camera to move such that the
		// marker is centered and for the marker's info window to open, if it has one).
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		Toast.makeText(this, "THE MARKER HAS BEEN CLICKED", Toast.LENGTH_LONG).show();
		
	}

}
