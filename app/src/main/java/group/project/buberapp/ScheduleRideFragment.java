package group.project.buberapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ScheduleRideFragment extends Fragment implements OnMapReadyCallback
{
    private GoogleMap mMap;
    private LatLng location;
    private ScheduleRideFragment.FragmentScheduleListener listener;

    public interface FragmentScheduleListener
    {
        void onInputScheduleSent(LatLng input);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // initialize variables
        View view = inflater.inflate(R.layout.fragment_schedule_ride, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_schedule);
        mapFragment.getMapAsync(this);

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
