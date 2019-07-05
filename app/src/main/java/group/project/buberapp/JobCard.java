package group.project.buberapp;

public class JobCard
{
    private int rideTime;
    private String pickupTime;
    private int payout;

    public JobCard()
    {
        // empty constructor needed
    }

    public JobCard(int rideTime, String pickupTime, int payout)
    {
        this.rideTime = rideTime;
        this.pickupTime = pickupTime;
        this.payout = payout;
    }

    public int getRideTime()
    {
        return rideTime;
    }

    public String getPickupTime()
    {
        return pickupTime;
    }

    public int getPayout()
    {
        return payout;
    }
}
