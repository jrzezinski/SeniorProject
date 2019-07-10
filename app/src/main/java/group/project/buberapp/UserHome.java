// By Justin Rzezinski

package group.project.buberapp;

import android.content.ClipData;
import android.content.Intent;
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
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class UserHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MapFragment.FragmentMapListener, ScheduleRideFragment.FragmentScheduleListener {

    private DrawerLayout drawer;
    private MapFragment mapFragment;
    ScheduleRideFragment scheduleRideFragment;
    HelpFragment helpFragment;
    RideHistoryFragment rideHistoryFragment;

    public static String userEmail;
    public static String userPass;
    public static String userName;
    public static String userPhone;
    public static String userType;
    public static String userboatId;
    public static String userDL;
    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // initialize variables
        drawer = findViewById(R.id.user_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mapFragment = new MapFragment();
        scheduleRideFragment = new ScheduleRideFragment();
        helpFragment = new HelpFragment();
        rideHistoryFragment = new RideHistoryFragment();
        userEmail = getIntent().getStringExtra("EXTRA_Final_email");
        userPass = getIntent().getStringExtra("EXTRA_Final_pass");
        userName = getIntent().getStringExtra("EXTRA_Final_name");
        userPhone = getIntent().getStringExtra("EXTRA_Final_phone");
        userboatId = getIntent().getStringExtra("EXTRA_Final_boatId");
        userDL = getIntent().getStringExtra("EXTRA_Final_userDL");
        userType = getIntent().getStringExtra("EXTRA_Final_userType");
        userId = getIntent().getStringExtra("EXTRA_Final_userId");

        // Set the toolbar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // open map fragment initially not on rotate
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
            navigationView.setCheckedItem(R.id.map_view);
        }

        // Add top left nav drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // set navigation user info
        View navHeader = navigationView.getHeaderView(0);

        TextView navName = navHeader.findViewById(R.id.nav_name);
        navName.setText(userName);

        TextView navEmail = navHeader.findViewById(R.id.nav_email);
        navEmail.setText(userEmail);

        // add menu selectRide if captain logged in
        Menu menu = navigationView.getMenu();
        MenuItem selectRide = menu.findItem(R.id.schdule_ride);

        if (userType.equals("captain"))
        {
            selectRide.setVisible(true);
        }
    }

    // search user input location on the map
    private LatLng searchLocation(String searchString)
    {
        // user text
        LatLng location;

        // init geolocation and get user text address lat and long
        Geocoder geocoder = new Geocoder(this);
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
            location = new LatLng(address.getLatitude(), address.getLongitude());
            MarkerOptions options = new MarkerOptions().position(location);

            return location;
        }

        return null;
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        // set navigation option depending on user type
        if (userType.equals("captain"))
        {
            MenuItem changeThisItem = menu.findItem(R.id.schdule_ride);
            changeThisItem.setTitle("Select a Job");
        }

        return true;
    }
    */

    // send lat and long data and open schedule a ride fragment
    @Override
    public void onInputMapSent(CharSequence input)
    {
        scheduleRideFragment.updateSearchText(searchLocation(input.toString()));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, scheduleRideFragment).commit();
    }

    @Override
    public void onInputScheduleSent(LatLng input)
    {

    }

    // Redirect navigation item selection to correct fragment
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        switch(menuItem.getItemId())
        {
            case R.id.map_view:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
                break;
            case R.id.schdule_ride:
                if (userType.equals("captain"))
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new JobSelectFragment()).commit();
                }
                else
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScheduleRideFragment()).commit();
                }
                break;
            case R.id.ride_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RideHistoryFragment()).commit();
                break;
            case R.id.help:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HelpFragment()).commit();
                break;
            case R.id.account_info:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountInfo()).commit();
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
}
