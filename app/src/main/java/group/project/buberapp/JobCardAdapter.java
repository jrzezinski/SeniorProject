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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class JobCardAdapter extends FirestoreRecyclerAdapter<JobCard, JobCardAdapter.JobHolder>
{
    private onItemClickListener listener;

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
        holder.textViewPickupTime.setText(model.getPickupTime());
        holder.textViewPayout.setText(String.valueOf(model.getPayout()));
    }

    @NonNull
    @Override
    public JobHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.job_card, viewGroup, false);
        return new JobHolder(view);
    }

    class JobHolder extends RecyclerView.ViewHolder
    {
        TextView textViewRideTime;
        TextView textViewPickupTime;
        TextView textViewPayout;

        public JobHolder(final View itemView)
        {
            super(itemView);
            textViewRideTime = itemView.findViewById(R.id.ride_time);
            textViewPickupTime = itemView.findViewById(R.id.pickup_time);
            textViewPayout = itemView.findViewById(R.id.payout);

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
