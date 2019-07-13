package group.project.buberapp;

        import android.graphics.Color;
        import android.support.annotation.NonNull;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
        import com.firebase.ui.firestore.FirestoreRecyclerOptions;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.firebase.firestore.CollectionReference;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.FirebaseFirestore;

public class RideCardAdapter extends FirestoreRecyclerAdapter<RideCard, RideCardAdapter.RideHolder>
//        implements OnMapReadyCallback
{
    private GoogleMap mMap;
    private LatLng location;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference RideList = db.collection("Rides");
    private CollectionReference userDoc = db.collection("users");
    private CollectionReference capDoc = db.collection("captain");

    public RideCardAdapter(@NonNull FirestoreRecyclerOptions<RideCard> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RideHolder holder, int position, @NonNull RideCard model)
    {
        holder.textViewPickupTime.setText(String.valueOf(model.getPickupTime()));
        if (UserHome.userType.equals("captain")) {
            DocumentReference documentReference = userDoc.document(String.valueOf(model.getSeekerID()));
            String name = String.valueOf(documentReference.get().getResult().get("Name"));
            holder.textViewSeekerID.setText(name);
            holder.textViewOtherIDTitle.setText("Rider: ");
            holder.textViewPayoutTitle.setText("Payout: $");
        } else {
            DocumentReference documentReference = capDoc.document(String.valueOf(model.getOffererID()));
            String name = String.valueOf(documentReference.get().getResult().get("Name"));
            holder.textViewOffererID.setText(name);
            holder.textViewOtherIDTitle.setText("Captain: ");
            holder.textViewPayoutTitle.setText("Cost: $");
        }
        holder.textViewRideTime.setText(model.getRideTime());
        holder.textViewBegLat.setText(model.getBegLat());
        holder.textViewBegLon.setText(model.getBegLon());
        holder.textViewEndLat.setText(model.getEndLat());
        holder.textViewEndLon.setText(model.getEndLon());
        holder.textViewPayout.setText(model.getPayout());
    }

    @NonNull
    @Override
    public RideHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ride_card, viewGroup, false);
        return new RideHolder(view);
    }

    class RideHolder extends RecyclerView.ViewHolder
    {
        GoogleMap mMap;
        TextView textViewPickupTime;
        TextView textViewSeekerID;
        TextView textViewOffererID;
        TextView textViewRideTime;
        TextView textViewBegLat;
        TextView textViewBegLon;
        TextView textViewEndLat;
        TextView textViewEndLon;
        TextView textViewPayout;
        TextView textViewOtherIDTitle;
        TextView textViewPayoutTitle;

        public RideHolder(final View itemView)
        {
            super(itemView);
//            mMap = itemView.findViewById(R.id.history_map);
            textViewPickupTime = itemView.findViewById(R.id.pickup_time);
            textViewPickupTime = itemView.findViewById(R.id.pickup_time);
            if (UserHome.userType.equals("captain")) {
                textViewSeekerID = itemView.findViewById(R.id.other_id);
            } else {
                textViewOffererID = itemView.findViewById(R.id.other_id);
            }
            textViewRideTime = itemView.findViewById(R.id.ride_time);
            textViewBegLat = itemView.findViewById(R.id.beg_lat);
            textViewBegLon = itemView.findViewById(R.id.beg_lon);
            textViewEndLat = itemView.findViewById(R.id.end_lat);
            textViewEndLon = itemView.findViewById(R.id.end_lon);
            textViewPayout = itemView.findViewById(R.id.payout);
            textViewOtherIDTitle = itemView.findViewById(R.id.other_id_title);
            textViewPayoutTitle = itemView.findViewById(R.id.payout_title);
        }
    }

    // Google maps added method for map load response
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // zoom functionality
//        mMap.getUiSettings().setZoomControlsEnabled(false);
//        mMap.getUiSettings().setAllGesturesEnabled(false);
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//
//        if(location != null)
//        {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
//
//            // move camera to lat and long and set pin
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
//            MarkerOptions options = new MarkerOptions().position(location);
//            mMap.addMarker(options);
//        }
//        else
//        {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
//        }
//
//    }
}
