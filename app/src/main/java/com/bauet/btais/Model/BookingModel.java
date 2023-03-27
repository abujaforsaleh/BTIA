package com.bauet.btais.Model;

public class BookingModel {
    private String bookingId;
    private String date;
    private String discount;
    private String hotel_name;
    private String room_id;
    private String room_rate;
    private String time;
    private String userName;
    private String userPhone;

    public BookingModel() {
    }

    public BookingModel(String bookingId, String date, String discount, String hotelName, String roomId, String roomRate, String time, String userName, String userPhone) {
        this.bookingId = bookingId;
        this.date = date;
        this.discount = discount;
        this.hotel_name = hotelName;
        this.room_id = roomId;
        this.room_rate = roomRate;
        this.time = time;
        this.userName = userName;
        this.userPhone = userPhone;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getDate() {
        return date;
    }

    public String getDiscount() {
        return discount;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public String getRoom_id() {
        return room_id;
    }

    public String getRoom_rate() {
        return room_rate;
    }

    public String getTime() {
        return time;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }
}
