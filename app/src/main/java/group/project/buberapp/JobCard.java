package group.project.buberapp;

import com.google.firebase.Timestamp;

import java.util.Map;

public class JobCard
{
    private int rideTime;
    private Timestamp pickupTime;
    private int payout;
    private double RideEndLocLat;
    private double RideEndLocLong;
    private double RideStartLocLat;
    private double RideStartLocLong;

    public JobCard()
    {
        // empty constructor needed
    }

    public JobCard(int rideTime, Timestamp pickupTime, int payout, double RideEndLocLat, double RideEndLocLong, double RideStartLocLat, double RideStartLocLong)
    {
        this.rideTime = rideTime;
        this.pickupTime = pickupTime;
        this.payout = payout;
        this.RideStartLocLat = RideStartLocLat;
        this.RideStartLocLong = RideStartLocLong;
        this.RideEndLocLat = RideEndLocLat;
        this.RideEndLocLong = RideEndLocLong;
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

    public double getRideEndLocLat() {
        return RideEndLocLat;
    }

    public double getRideEndLocLong() {
        return RideEndLocLong;
    }

    public double getRideStartLocLat() {
        return RideStartLocLat;
    }

    public double getRideStartLocLong() {
        return RideStartLocLong;
    }
}
