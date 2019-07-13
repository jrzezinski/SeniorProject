package group.project.buberapp;

import com.google.firebase.Timestamp;

import java.util.Map;

public class JobCard
{
    private int rideTime;
    private Timestamp pickupTime;
    private int payout;
    private Map<Integer, Integer> locationStart;
    private Map<Integer, Integer> locationEnd;

    public JobCard()
    {
        // empty constructor needed
    }

    public JobCard(int rideTime, Timestamp pickupTime, int payout, Map<Integer, Integer> locationStart, Map<Integer, Integer> locationEnd)
    {
        this.rideTime = rideTime;
        this.pickupTime = pickupTime;
        this.payout = payout;
        this.locationStart = locationStart;
        this.locationEnd = locationEnd;
    }

    public int getRideTime()
    {
        return rideTime;
    }

    public Timestamp getPickupTime()
    {
        return pickupTime;
    }

    public int getPayout()
    {
        return payout;
    }

    public Map<Integer, Integer> getLocationStart() { return locationStart; }

    public Map<Integer, Integer> getLocationEnd() { return locationEnd; }
}
