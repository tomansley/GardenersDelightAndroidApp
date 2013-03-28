package com.gdelight.utils.location;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class LocationHelper {

	public static Location getCurrentLocation(Activity activity, LocationListener listener) {
		
		/* Use the LocationManager class to obtain GPS locations */
		LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

		String mlocProvider;
		Criteria hdCrit = new Criteria();
		hdCrit.setAccuracy(Criteria.ACCURACY_COARSE);
		mlocProvider = locationManager.getBestProvider(hdCrit, true);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1000, listener);
		Location location = locationManager.getLastKnownLocation(mlocProvider);
		locationManager.removeUpdates(listener);

		return location;

	}
	
}
