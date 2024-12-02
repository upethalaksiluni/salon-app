package com.example.sulochanasalon;

public class UserNotification
{
    private int notificationId;
    private String message;
    private String notificationDate;
    private int userId;
    private String date;

    // Constructor with four parameters
    public UserNotification(int notificationId, String message, String notificationDate, int userId) {
        this.notificationId = notificationId;
        this.message = message;
        this.notificationDate = notificationDate;
        this.userId = userId;
        this.date = date;
    }

    // Getter and Setter methods
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }
}

