package group.project.buberapp;

import com.google.android.gms.maps.GoogleMap;

public class RideCard {
    private GoogleMap mMap;
    private String pickupTime;
    private String SeekerID;
    private String OffererID;
    private int rideTime;
    private int BegLat;
    private int BegLon;
    private int EndLat;
    private int EndLon;
    private int payout;

    public RideCard()
    {
        // empty constructor needed
    }

    public RideCard(String pickupTime, String SeekerID, String OffererID, int rideTime, int payout)
//            , int BegLat, int BegLon, int EndLat, int EndLon
    {
//        this.mMap = mMap;
        this.pickupTime = pickupTime;
        this.SeekerID = SeekerID;
        this.OffererID = OffererID;
        this.rideTime = rideTime;
//        TODO: figure out how to assign from the ride start/end loc maps
//        this.BegLat = RideStartLoc.latitude;
//        this.BegLon = RideStartLoc.longitude;
//        this.EndLat = RideEndLoc.latitude;
//        this.EndLon = RideEndLoc.longitude;
        this.payout = payout;
    }

    public String getPickupTime()
    {
        return pickupTime;
    }

    public String getSeekerID()
    {
        return SeekerID;
    }

    public String getOffererID()
    {
        return OffererID;
    }

    public int getRideTime()
    {
        return rideTime;
    }

    public int getBegLat()
    {
        return BegLat;
    }

    public int getBegLon()
    {
        return BegLon;
    }

    public int getEndLat()
    {
        return EndLat;
    }

    public int getEndLon()
    {
        return EndLon;
    }

    public int getPayout()
    {
        return payout;
    }
}