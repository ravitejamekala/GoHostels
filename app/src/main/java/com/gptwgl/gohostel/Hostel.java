package com.gptwgl.gohostel;

public class Hostel {
    private String Name, Location, Rent, Address;
    private String Facilites;
    private String Roomsharing;
    private String Hostel_type;
    private Double Longitude;
    private  Double Latitude;

    public Hostel() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getRent() {
        return Rent;
    }

    public void setRent(String rent) {
        Rent = rent;
    }

    public String getLocationid() {
        return Address;
    }

    public void setLocationid(String locationid) {
        this.Address = locationid;
    }

    public String getFacilites() {
        return Facilites;
    }

    public void setFacilites(String facilites) {
        Facilites = facilites;
    }

    public String getRoomsharing() {
        return Roomsharing;
    }

    public void setRoomsharing(String roomsharing) {
        Roomsharing = roomsharing;
    }

    public String getHostel_type() {
        return Hostel_type;
    }

    public void setHostel_type(String hostel_type) {
        Hostel_type = hostel_type;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }
}
