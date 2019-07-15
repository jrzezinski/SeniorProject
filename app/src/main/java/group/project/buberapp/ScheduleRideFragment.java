// By Justin Rzezinski

package group.project.buberapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.opencensus.internal.StringUtil;

public class ScheduleRideFragment extends Fragment implements OnMapReadyCallback, AdapterView.OnItemSelectedListener
{
    // Declare Variables
    private GoogleMap mMap;
    private LatLng location;
    private LatLng currentLocation;
    private ScheduleRideFragment.FragmentScheduleListener listener;
    private Button launchPayButton;
    private Button switchButton;
    private TextView finalCost;
    private Spinner hourSelect;
    private String hour;
    private EditText pickupTime;
    private TimePickerDialog pickTime;
    private int hourlyRate;
    private Calendar calendar;
    private int currentHour;
    private int currentMinute;
    private Date pickupTimeStamp;

    // Database stuff
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference jobList = db.collection("Rides");

    public interface FragmentScheduleListener
    {
        void onInputScheduleSent(LatLng input, LatLng inputSecond);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // initialize variables
        final View view = inflater.inflate(R.layout.fragment_schedule_ride, container, false);
        launchPayButton = view.findViewById(R.id.pay_now_button);
        switchButton = view.findViewById(R.id.schedule_button);
        hourSelect = view.findViewById(R.id.time_spinner);
        pickupTime = view.findViewById(R.id.pickup_val);
        hourlyRate = 150;

        // lock app navigation pullout
        DrawerLayout drawer = getActivity().findViewById(R.id.user_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // Fill in the spinner with the String Array in strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.timeBlocks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSelect.setAdapter(adapter);

        // Set spinner to listen for change
        hourSelect.setOnItemSelectedListener(this);

        // Pickup time user prompt onclick
        pickupTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR);
                currentMinute = calendar.get(Calendar.MINUTE);

                pickTime = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        pickupTime.setText((hourOfDay < 12 ? hourOfDay : hourOfDay - 12) + ":" + (minute < 10 ? "0" + minute : minute) + " " + (hourOfDay < 12 ? "AM" : "PM"));
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm", Locale.US);
                        String myDateStr = "" + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "_" + hourOfDay + ":" + minute;
                        try
                        {
                            pickupTimeStamp = simpleDateFormat.parse(myDateStr);
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, currentHour, currentMinute, false);

                pickTime.show();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_schedule);
        mapFragment.getMapAsync(this);

        finalCost = view.findViewById(R.id.val_estimate);

        switchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // get user info
                int payout = Integer.parseInt(finalCost.getText().toString());
                String pickup = pickupTime.getText().toString();
                int hoursChosen = Integer.parseInt(hourSelect.getSelectedItem().toString());

                if (!TextUtils.isEmpty(pickup))
                {
                    switchButton.setVisibility(View.GONE);
                    launchPayButton.setVisibility(View.VISIBLE);

                    // Store user info
                    Map<String, Object> myRide = new HashMap<String, Object>();
                    myRide.put("payout", payout);
                    myRide.put("pickupTime", pickupTimeStamp);
                    myRide.put("rideTime", hoursChosen);

                    myRide.put("RideEndLocLat", location.latitude);
                    myRide.put("RideEndLocLong", location.longitude);
                    myRide.put("RideStartLocLat", currentLocation.latitude);
                    myRide.put("RideStartLocLong", currentLocation.longitude);

                    myRide.put("SeekerID", UserHome.userId);
                    myRide.put("OffererID", null);

                    // Send info to Database
                    jobList.add(myRide).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getContext(), "Ride Scheduled! Please wait for a pickup.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Ride scheduling failed, try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(getContext(), "You must specify a pickup time!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        launchPayButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                intent.putExtra("EXTRA_Final_COST", finalCost.getText().toString());
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    // update search on checkout of fragment_schedule_ride
    public void updateSearchText(LatLng locationText, LatLng userLocation)
    {
        location = locationText;
        currentLocation = userLocation;
    }

    // initialize listener on attach
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        // check to make sure underlying activity implements FragmentMapListener
        if(context instanceof ScheduleRideFragment.FragmentScheduleListener)
        {
            listener = (ScheduleRideFragment.FragmentScheduleListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement FragmentScheduleListener");
        }
    }

    // wipe listener as it is not needed
    @Override
    public void onDetach()
    {
        super.onDetach();

        listener = null;
    }

    // Stores the spinner value
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        hour = parent.getItemAtPosition(position).toString();
        TextView cost = getActivity().findViewById(R.id.val_estimate);
        cost.setText(Integer.toString(Integer.parseInt(hour) * hourlyRate));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

        // if user search location is given mark that instead
        if(location != null)
        {
            MarkerOptions options = new MarkerOptions().position(location);
            mMap.addMarker(options);
        }

        // if user current location is given mark that additionally
        if(currentLocation != null)
        {
            MarkerOptions options = new MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(options);
        }

        // create bounds od the map markers (north, south, ...)
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(location);
        builder.include(currentLocation);
        LatLngBounds bounds = builder.build();

        // create the padding to keep markers away from map edge
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (height * 0.20);

        // move the camera too the correct zoom for the points
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
    }
}
