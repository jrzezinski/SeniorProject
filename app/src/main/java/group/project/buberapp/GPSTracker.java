package group.project.buberapp;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;

import android.content.Intent;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;
    private GoogleMap mMap;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude;
    double longitude;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public double getLatitude() { //returning void but it used to be double when returning latitude
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude?

        return latitude;
    }

    public double getLongitude() { //returning void but it used to be double when returning longitude
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;

    }


    public GPSTracker(Context context, GoogleMap mMap) {
        this.mContext = context;
        this.mMap = mMap;
        getLocation();
    }

    @SuppressLint("MissingPermission")
    public LatLng getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = getLatitude();
                            longitude = getLongitude();
                        }
                    }
                }
                if (location == null) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                                latitude = getLatitude();
                                longitude = getLongitude();
                        } else {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = getLatitude();
                                longitude = getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LatLng currentLocation  = new LatLng(latitude, longitude);

        return currentLocation; //where location is returned
    }





    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    
}

//used http://www.codesenior.com/en/tutorial/Android-GPS-Location-Example
