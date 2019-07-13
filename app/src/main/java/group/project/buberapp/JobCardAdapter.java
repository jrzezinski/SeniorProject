package group.project.buberapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class JobCardAdapter extends FirestoreRecyclerAdapter<JobCard, JobCardAdapter.JobHolder>
{
    private onItemClickListener listener;

    private Map<Integer, Integer> startLoc;
    private Map<Integer, Integer> endLoc;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference jobList = db.collection("Rides");
    public JobCardAdapter(@NonNull FirestoreRecyclerOptions<JobCard> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull JobHolder holder, int position, @NonNull JobCard model)
    {
        holder.textViewRideTime.setText(String.valueOf(model.getRideTime()));
        holder.textViewPickupTime.setText(model.getPickupTime().toDate().toString());
        holder.textViewPayout.setText(String.valueOf(model.getPayout()));
        startLoc = model.getLocationStart();
        startLoc = model.getLocationEnd();

        GoogleMap tripMap = holder.tripMap;

        if (tripMap != null)
        {
            double startLat = 28.056999 ;
            double startLong = -82.425987;
            double endLat = 28.056999;
            double endLong = -82.425987;

            if (startLoc != null)
            {
                startLat = startLoc.get("latitude");
                startLong = startLoc.get("longitude");
            }

            if (endLoc != null)
            {
                endLat = endLoc.get("latitude");
                endLong = endLoc.get("longitude");
            }

            LatLng currentLocation = new LatLng(startLat, startLong);
            LatLng location = new LatLng(endLat, endLong);

            MarkerOptions optionsStart = new MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            MarkerOptions optionsEnd = new MarkerOptions().position(new LatLng(endLat, endLong));

            tripMap.addMarker(optionsStart);
            tripMap.addMarker(optionsEnd);

            // create bounds od the map markers (north, south, ...)
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(location);
            builder.include(currentLocation);
            LatLngBounds bounds = builder.build();

            // create the padding to keep markers away from map edge
            int width = holder.mapView.getContext().getResources().getDisplayMetrics().widthPixels;
            int height = holder.mapView.getContext().getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (height * 0.20);

            // move the camera too the correct zoom for the points
            tripMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
        }
    }

    @NonNull
    @Override
    public JobHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.job_card, viewGroup, false);
        return new JobHolder(view);
    }

    class JobHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback
    {
        TextView textViewRideTime;
        TextView textViewPickupTime;
        TextView textViewPayout;

        GoogleMap tripMap;
        MapView mapView;

        public JobHolder(final View itemView)
        {
            super(itemView);
            textViewRideTime = itemView.findViewById(R.id.ride_time);
            textViewPickupTime = itemView.findViewById(R.id.pickup_time);
            textViewPayout = itemView.findViewById(R.id.payout);

            mapView = (MapView) itemView.findViewById(R.id.job_card_map);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int jobPosition = getAdapterPosition();

                    if(jobPosition != RecyclerView.NO_POSITION && listener != null)
                    {
                        listener.onItemClick(getSnapshots().getSnapshot(jobPosition), jobPosition);
                        itemView.setBackgroundColor(Color.parseColor("#78a6f0"));
                    }
                }
            });

            // map stuff
            if (mapView != null)
            {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap)
        {
            MapsInitializer.initialize(mapView.getContext());
            tripMap = googleMap;



            //tripMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startLat, startLong), 10));
        }
    }

    public interface onItemClickListener
    {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(onItemClickListener listener)
    {
        this.listener = listener;
    }
}
