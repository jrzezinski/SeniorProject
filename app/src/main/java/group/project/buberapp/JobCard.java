package group.project.buberapp;

import com.google.firebase.Timestamp;

public class JobCard
{
    private int rideTime;
    private Timestamp pickupTime;
    private int payout;

    public JobCard()
    {
        // empty constructor needed
    }

    public JobCard(int rideTime, Timestamp pickupTime, int payout)
    {
        this.rideTime = rideTime;
        this.pickupTime = pickupTime;
        this.payout = payout;
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
}
