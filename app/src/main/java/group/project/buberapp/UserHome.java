// By Justin Rzezinski

package group.project.buberapp;

import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserHome extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private GoogleMap mMap;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // initialize variables
        searchText = findViewById(R.id.search_text);
        drawer = findViewById(R.id.user_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set the toolbar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add top left nav drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        // open map fragment initially not on rotate
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
            navigationView.setCheckedItem(R.id.map_view);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        switch(menuItem.getItemId())
        {
            case R.id.map_view:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
                break;
            case R.id.schdule_ride:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScheduleRideFragment()).commit();
                break;
            case R.id.ride_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RideHistoryFragment()).commit();
                break;
            case R.id.help:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HelpFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    // override back button behavior
    @Override
    public void onBackPressed()
    {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
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
        Geocoder geocoder = new Geocoder(UserHome.this);
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
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
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
