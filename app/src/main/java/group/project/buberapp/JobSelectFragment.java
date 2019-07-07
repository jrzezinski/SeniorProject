package group.project.buberapp;

import android.graphics.Color;
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
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.model.Document;

import java.util.HashMap;
import java.util.Map;

public class JobSelectFragment extends Fragment
{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference jobList = db.collection("Rides");
    private JobCardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_choose_job, container, false);

        setUpRecyclerView(view);

        adapter.setOnItemClickListener(new JobCardAdapter.onItemClickListener()
        {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position)
            {

                // Hold document id
                final String jobID = documentSnapshot.getId();

                // show button
                Button confirm = view.findViewById(R.id.job_select_confirm);
                confirm.setVisibility(View.VISIBLE);

                RecyclerView recyclerView = view.findViewById(R.id.jobs_recycler_view);
                int numOfChildren = recyclerView.getChildCount();
                View child;

                for (int i = 0; i < numOfChildren; i++)
                {

                    child = recyclerView.getChildAt(i);
                    child.setBackgroundColor(Color.parseColor("#faff6e"));
                }

                // on button click update db with captain ID
                confirm.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        // Store user info
                        Map<String,Object> myRide = new HashMap<String,Object>();
                        myRide.put("OffererID", UserHome.userId);

                        DocumentReference job = jobList.document(jobID);

                        // Send info to Database
                        job.update(myRide).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Job Selected", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed to select job, try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        return view;
    }

    private void setUpRecyclerView(View view)
    {
        Query query = jobList.orderBy("rideTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<JobCard> options = new FirestoreRecyclerOptions.Builder<JobCard>().setQuery(query, JobCard.class).build();
        adapter = new JobCardAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.jobs_recycler_view);
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
