package group.project.buberapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RideHistoryFragment extends Fragment
{
    private TextView historyTitle;
    private TextView historyDescription;
    private Button loadMore;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ride_history, container, false);
        historyTitle = v.findViewById(R.id.historyTitle);
        historyDescription = v.findViewById(R.id.historyDescription);

        // call to db for number of rides in history
        final int additionalHistory = 0;

        // load X additional rides when Load More button is clicked
        loadMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // if additional rides exist and are not showing, display X more
                if (additionalHistory > 0) {
                    // check if offerer or rider???
                    // call to db for additional rides
                } else {
                    // if clicked but no more to display then notify user
                    Toast.makeText(getContext(), "No additional rides to display!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }
}