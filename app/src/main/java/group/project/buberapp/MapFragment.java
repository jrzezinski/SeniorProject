package group.project.buberapp;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment, Service implements OnMapReadyCallback, LocationListener //now implementing LocationListener and extending Service
{
    private GoogleMap mMap;
    private EditText searchText;

    //to update the gps for every distance change of 10 meters AND every period of time equal to 1 minute
    private static final long distanceUpdate = 10;
    private static final long timeUpdate = 1000 * 60 * 1;

    //declaring a location manager for locationRetrieval()
    protected LocationManager locationManager;
    
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // initialize variables
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        searchText = view.findViewById(R.id.search_text);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    private void initMapSearch()
    {
        // catch user enter for searchText input
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER)
                {
                    // run search function
                    searchLocation();
                }

                return false;
            }
        });
    }

    // search user input location on the map
    private void searchLocation()
    {
        // user text
        String searchString = searchText.getText().toString();

        // init geolocation and get user text address lat and long
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try
        {
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e)
        {

        }

        // if there is at least one address get the first
        if(list.size() > 0)
        {
            // get lat and long info and map marker
            Address address = list.get(0);
            LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
            MarkerOptions options = new MarkerOptions().position(location);

            // move camera to lat and long and set pin
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
            mMap.addMarker(options);

            // hide keyboard
            this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }

    //establish context with user session and call locationRetrieval
    public userSession(Context context) 
    {
        this.mContext = context;
        locationRetrieval();
    }

    public Location locationRetrieval()
    {
       try {

            //for this context obtain location service
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            //obtain GPS status by inquiring on GPS_PROVIDER
            GPSStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //obtain network status by inquiring on NETWORK_PROVIDER
            networkStatus = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            //if networkStatus is true, then we have network status
            if (networkStatus)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                if (locationManager != null)
                {
                    location = locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, timeUpdate, distanceUpdate, this); //if locationManager is still good obtain updates. this is the function: requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener)
                    if (location != null) //get coordinates if location is not blank
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                }
                //opposite case request from GPS string provider
                if (location == null)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    if (locationManager != null)
                    {
                        location = locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER);
                        if (location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                         }
                         else
                         {
                            location = locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER);
                            if (location != null)
                            {
                                 latitude = location.getLatitude();
                                 longitude = location.getLongitude();
                             }
                          }
                      }
                   }
                }
            }
            else
            {
                log.d("No network status")
            }
        catch (IOException e)
        {
            
        }

        return location;
    }


    //obtain coordinates when called in
    public double getLatitude()
        {
        if (location != null)
            {
                latitude = location.getLatitude();
            }
        return latitude;
        }

    public double obtainLongitude()
        {
            if (location != null)
                {
                    longitude = location.getLongitude();
                }

             return longitude;
        }

    // Google maps added method for map load response
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // zoom functionality
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        initMapSearch();
    }
}
