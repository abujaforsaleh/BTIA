package com.bauet.btais.Model;

import java.io.Serializable;

public class HotelModel implements Serializable {

    private String hid;
    private String date;
    private String time;
    private String image;

    private String division;
    private String district;
    private String upazila;

    private String hotelName;
    private String roomType;
    private String costPerNight;
    private String bonus;
    private String hotelInformation;

    public HotelModel() {
    }

    public HotelModel(String hid, String date, String time, String image, String division, String district, String upazila, String hotelName, String roomType, String costPerNight, String bonus, String hotelInformation) {
        this.hid = hid;
        this.date = date;
        this.time = time;
        this.image = image;
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.hotelName = hotelName;
        this.roomType = roomType;
        this.costPerNight = costPerNight;
        this.bonus = bonus;
        this.hotelInformation = hotelInformation;
    }

    public String getHid() {
        return hid;
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

    public String getDivision() {
        return division;
    }

    public String getDistrict() {
        return district;
    }

    public String getUpazila() {
        return upazila;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getCostPerNight() {
        return costPerNight;
    }

    public String getBonus() {
        return bonus;
    }

    public String getHotelInformation() {
        return hotelInformation;
    }


    @Override
    public String toString() {
        return "HotelModel{" +
                "hid='" + hid + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", image='" + image + '\'' +
                ", division='" + division + '\'' +
                ", district='" + district + '\'' +
                ", upazila='" + upazila + '\'' +
                ", hotelName='" + hotelName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", costPerNight='" + costPerNight + '\'' +
                ", bonus='" + bonus + '\'' +
                ", hotelInformation='" + hotelInformation + '\'' +
                '}';
    }
}
