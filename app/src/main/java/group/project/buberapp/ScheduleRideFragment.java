// By Justin Rzezinski

package group.project.buberapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ScheduleRideFragment extends Fragment implements OnMapReadyCallback, AdapterView.OnItemSelectedListener
{
    // Declare Variables
    private GoogleMap mMap;
    private LatLng location;
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

    // Database stuff
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference jobList = db.collection("Rides");

    public interface FragmentScheduleListener
    {
        void onInputScheduleSent(LatLng input);
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

                pickTime = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        pickupTime.setText((hourOfDay < 12 ? hourOfDay : hourOfDay - 12) + ":" + (minute < 10 ? "0" + minute : minute) + " " + (hourOfDay < 12 ? "AM" : "PM"));
                    }
                }, currentHour, currentMinute, false);

                pickTime.show();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_schedule);
        mapFragment.getMapAsync(this);

        finalCost = view.findViewById(R.id.val_estimate);

        switchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switchButton.setVisibility(View.GONE);
                launchPayButton.setVisibility(View.VISIBLE);

                // get user info
                int payout = Integer.parseInt(finalCost.getText().toString());
                String pickup = pickupTime.getText().toString();
                int hoursChosen = Integer.parseInt(hourSelect.getSelectedItem().toString());

                // Store user info
                Map<String,Object> myRide = new HashMap<String,Object>();
                myRide.put("payout", payout);
                myRide.put("pickupTime", pickup);
                myRide.put("rideTime", hoursChosen);
                myRide.put("RideEndLoc", location);
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
    public void updateSearchText(LatLng locationText)
    {
        location = locationText;
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

        if(location != null)
        {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));

            // move camera to lat and long and set pin
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
            MarkerOptions options = new MarkerOptions().position(location);
            mMap.addMarker(options);
        }
        else
        {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
        }

    }
}