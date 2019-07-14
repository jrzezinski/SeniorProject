package group.project.buberapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private EditText searchText;

    private FragmentMapListener listener;
    private Button toScheduleButton;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng userLocation;


    public interface FragmentMapListener {
        void onInputMapSent(CharSequence input, LatLng inputLocation);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // initialize variables
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        searchText = view.findViewById(R.id.search_text);
        toScheduleButton = view.findViewById(R.id.map_checkout_button);

        // Hide schedule button
        toScheduleButton.setVisibility(View.INVISIBLE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // GPS stuff
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                displayUserLocation(mMap, userLocation);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 8);
            }
            else
            {
                locationManager.requestLocationUpdates("gps", 0, 10, locationListener);
            }
        }

        // send info to underlying activity
        toScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence input = searchText.getText();
                LatLng validLoc = searchLocation(input.toString());

                if (validLoc != null)
                {
                    listener.onInputMapSent(input, userLocation);
                }
                else
                {
                    toScheduleButton.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "You must have a valid search location first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    // update search on checkout of fragment_schedule_ride
    public void updateSearchText(CharSequence locationText) {
        searchText.setText(locationText);
    }

    // initialize listener on attach
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // check to make sure underlying activity implements FragmentMapListener
        if (context instanceof FragmentMapListener) {
            listener = (FragmentMapListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentMapListener");
        }
    }

    // wipe listener as it is not needed
    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    private void initMapSearch() {
        // catch user enter for searchText input
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    // run search function
                    searchLocation(searchText.getText().toString());
                    toScheduleButton.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });
    }

    // search user input location on the map
    private LatLng searchLocation(String searchString) {
        // user text
        LatLng location;

        // init geolocation and get user text address lat and long
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if there is at least one address get the first
        if (list.size() > 0) {
            // get lat and long info and map marker
            Address address = list.get(0);
            location = new LatLng(address.getLatitude(), address.getLongitude());
            MarkerOptions options = new MarkerOptions().position(location);

            // move camera to lat and long and set pin
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
            mMap.addMarker(options);

            // hide keyboard
            this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            // return the searched location
            return location;
        }

        return null;
    }

    // Google maps added method for map load response
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // zoom functionality
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        initMapSearch();
    }

    private void displayUserLocation(GoogleMap mMap, LatLng returnLocation)
    {
        // set marker
        MarkerOptions options = new MarkerOptions().position(returnLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        // move camera to lat and long and set pin
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(returnLocation, 15.0f));
        mMap.addMarker(options);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 8:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    locationManager.requestLocationUpdates("gps", 0, 10, locationListener);
                }
        }
    }
}