package com.example.intelligenttourist;

import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

public class TripsHelper {
    String TripTitle,Description,Address,StartTime,EndTime,Pickup_Dropoff,Price,TripType,image;
    List Places;
    public TripsHelper(String triptitle, String des, String addres, String starttime, String endtime, String drop_pick, String price, String triptype, List places , String image) {
        this.TripTitle = triptitle;
        this.image=image;
        this.Description = des;
        this.Address = addres;
        this.StartTime = starttime;
        this.EndTime = endtime;
        this.Pickup_Dropoff = drop_pick;
        this.Price = price;
        this.TripType = triptype;
        this.Places = places;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTriptitle() {
        return TripTitle;
    }

    public void setTriptitle(String triptitle) {
        this.TripTitle = triptitle;
    }

    public String getDes() {
        return Description;
    }

    public void setDes(String des) {
        this.Description = des;
    }

    public String getAddres() {
        return Address;
    }

    public void setAddres(String addres) {
        this.Address = addres;
    }

    public String getStarttime() {
        return StartTime;
    }

    public void setStarttime(String starttime) {
        this.StartTime = starttime;
    }

    public String getEndtime() {
        return EndTime;
    }

    public void setEndtime(String endtime) {
        this.EndTime = endtime;
    }

    public String getDrop_pick() {
        return Pickup_Dropoff;
    }

    public void setDrop_pick(String drop_pick) {
        this.Pickup_Dropoff = drop_pick;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        this.Price = price;
    }

    public String getTriptype() {
        return TripType;
    }

    public void setTriptype(String triptype) {
        this.TripType = triptype;
    }

    public List getPlaces() {
        return Places;
    }

    public void setPlaces(List places) {
        this.Places = places;
    }

    public TripsHelper() {
    }


}
