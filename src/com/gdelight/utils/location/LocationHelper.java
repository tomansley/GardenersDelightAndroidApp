package com.gdelight.utils.location;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class LocationHelper {

	public static Location getCurrentLocation(Activity activity, LocationListener listener) {
		
		/* Use the LocationManager class to obtain GPS locations */
		LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

		//get providers
        List<String> providers = locationManager.getProviders(true);

		Location location = null;
        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/        
        for (int i=providers.size()-1; i>=0; i--) {
                location = locationManager.getLastKnownLocation(providers.get(i));
                if (location != null) break;
        }

        return location;

	}
	
}
