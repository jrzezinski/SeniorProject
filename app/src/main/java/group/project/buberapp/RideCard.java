package group.project.buberapp;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.Timestamp;

public class RideCard {
    private Timestamp pickupTime;
    private String SeekerID;
    private String OffererID;
    private int rideTime;
//    private int BegLatLon;
//    private int EndLatLon;
    private int payout;

    public RideCard()
    {
        // empty constructor needed
    }

    public RideCard(Timestamp pickupTime, String SeekerID, String OffererID, int rideTime, int payout)
//, int BegLatLon, int EndLatLon
    {
        this.pickupTime = pickupTime;
        this.SeekerID = SeekerID;
        this.OffererID = OffererID;
        this.rideTime = rideTime;
//        TODO: figure out how to assign from the ride start/end loc maps
//        this.BegLatLon = RideStartLoc;
//        this.EndLatLon = RideEndLoc;
        this.payout = payout;
    }

    public Timestamp getPickupTime()
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

//    public int getBegLat()
//    {
//        return BegLat;
//    }
//
//    public int getBegLon()
//    {
//        return BegLon;
//    }
//
//    public int getEndLat()
//    {
//        return EndLat;
//    }
//
//    public int getEndLon()
//    {
//        return EndLon;
//    }

    public int getPayout()
    {
        return payout;
    }
}