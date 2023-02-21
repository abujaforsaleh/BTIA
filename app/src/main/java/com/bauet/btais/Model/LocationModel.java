package com.bauet.btais.Model;

public class LocationModel {
    private String lid;
    private String date;
    private String time;
    private String image;
    private String video;
    private String division;
    private String district;
    private String upazila;
    private String placeName;
    private String costFromDhaka;
    private String travelCost;
    private String information;

    public LocationModel() {

    }

    public LocationModel(String lid, String date, String time, String image, String video, String division, String district, String upazila, String placeName, String costFromDhaka, String travelCost, String information) {
        this.lid = lid;
        this.date = date;
        this.time = time;
        this.image = image;
        this.video = video;
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.placeName = placeName;
        this.costFromDhaka = costFromDhaka;
        this.travelCost = travelCost;
        this.information = information;
    }

    public String getLid() {
        return lid;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getImage() {
        return image;
    }


    public String getVideo() {
        return video;
    }

    public String getDivision() {
        return division;
    }

    public String getDistrict() {
        return district;
    }

    public String getUpazila() {
        return upazila;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getCostFromDhaka() {
        return costFromDhaka;
    }

    public String getTravelCost() {
        return travelCost;
    }

    public String getInformation() {
        return information;
    }
}

