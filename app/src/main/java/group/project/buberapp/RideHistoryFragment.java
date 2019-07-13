package group.project.buberapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static com.firebase.ui.auth.AuthUI.TAG;

public class RideHistoryFragment extends Fragment
{
    // Cloud Firestore Instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rideList = db.collection("Rides");
    private RideCardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_ride_history, container, false);

        setUpRecyclerView(v);

        return v;
    }

    private void setUpRecyclerView(View view)
    {
        Query query;

        // checks if captain or rider to query correct rides
        if (UserHome.userType.equals("captain")) {
            query = rideList.whereEqualTo("OffererID",UserHome.userId).orderBy("pickupTime", Query.Direction.DESCENDING);
        } else {
            query = rideList.whereEqualTo("SeekerID",UserHome.userId).orderBy("pickupTime", Query.Direction.DESCENDING);
        }

        FirestoreRecyclerOptions<RideCard> options = new FirestoreRecyclerOptions.Builder<RideCard>().setQuery(query, RideCard.class).build();
        adapter = new RideCardAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.rides_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}














































