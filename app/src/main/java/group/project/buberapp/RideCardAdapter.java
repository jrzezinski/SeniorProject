//
//package group.project.buberapp;
//
//        import android.graphics.Color;
//        import android.support.annotation.NonNull;
//        import android.support.v7.widget.RecyclerView;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.TextView;
//
//        import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//        import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//        import com.google.firebase.firestore.CollectionReference;
//        import com.google.firebase.firestore.DocumentSnapshot;
//        import com.google.firebase.firestore.FirebaseFirestore;
//
//public class RideCardAdapter extends FirestoreRecyclerAdapter<RideCard, RideCardAdapter.RideHolder>
//{
//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private CollectionReference RideList = db.collection("Rides");
//    public RideCardAdapter(@NonNull FirestoreRecyclerOptions<RideCard> options)
//    {
//        super(options);
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull RideHolder holder, int position, @NonNull RideCard model)
//    {
////        holder.textViewRideTime.setText(String.valueOf(model.getRideTime()));
////        holder.textViewPickupTime.setText(model.getPickupTime());
////        holder.textViewPayout.setText(String.valueOf(model.getPayout()));
//    }
//
//    @NonNull
//    @Override
//    public RideHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
//    {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ride_card, viewGroup, false);
//        return new RideHolder(view);
//    }
//
//    class RideHolder extends RecyclerView.ViewHolder
//    {
////        TextView textViewRideTime;
////        TextView textViewPickupTime;
////        TextView textViewPayout;
//
//        public RideHolder(final View itemView)
//        {
//            super(itemView);
////            textViewRideTime = itemView.findViewById(R.id.ride_time);
////            textViewPickupTime = itemView.findViewById(R.id.pickup_time);
////            textViewPayout = itemView.findViewById(R.id.payout);
//        }
//    }
//}
