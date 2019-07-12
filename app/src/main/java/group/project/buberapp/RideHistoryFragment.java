package group.project.buberapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class RideHistoryFragment extends Fragment
{
    // Cloud Firestore Instance
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rideList = db.collection("Rides");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_ride_history, container, false);

//        setUpRecyclerView(v);

        TextView historyTitle = v.findViewById(R.id.historyTitle);
        TextView historyDescription = v.findViewById(R.id.historyDescription);





        return v;
    }
//
//    private void setUpRecyclerView(View view)
//    {
//        // load 10 rides if available
//        if (UserHome.userType.equals("captain")) {
//            // TODO: load rides w/fields relevant to CAPTAINS
//
//        } else {
//            // TODO: load rides w/fields relevant to USERS
//
//        }
//
//        Query query = rideList.whereEqualTo("","").orderBy("rideTime", Query.Direction.DESCENDING);
//
//        FirestoreRecyclerOptions<JobCard> options = new FirestoreRecyclerOptions.Builder<JobCard>().setQuery(query, JobCard.class).build();
//        adapter = new JobCardAdapter(options);
//
//        RecyclerView recyclerView = view.findViewById(R.id.jobs_recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
//    }
//
//    @Override
//    public void onStart()
//    {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    public void onStop()
//    {
//        super.onStop();
//        adapter.stopListening();
//    }
}














































