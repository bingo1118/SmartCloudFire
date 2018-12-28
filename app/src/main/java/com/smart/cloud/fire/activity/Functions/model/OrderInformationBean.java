package com.smart.cloud.fire.activity.Functions.model;

public class OrderInformationBean {
    //{date=31.0, day=4.0, hours=14.0, minutes=58.0, month=7.0, nanos=2.17E8,
    // seconds=21.0, time=1.504162701217E12, timezoneOffset=-480.0, year=117.0}
    private String date;
    private String day;
    private String hours;
    private String minutes;
    private String month;
    private String nanos;
    private String seconds;
    private String time;
    private String timezoneOffset;
    private String year;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getNanos() {
        return nanos;
    }

    public void setNanos(String nanos) {
        this.nanos = nanos;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(String timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "OrderInformationBean{" +
                "date='" + date + '\'' +
                ", day='" + day + '\'' +
                ", hours='" + hours + '\'' +
                ", minutes='" + minutes + '\'' +
                ", month='" + month + '\'' +
                ", nanos='" + nanos + '\'' +
                ", seconds='" + seconds + '\'' +
                ", time='" + time + '\'' +
                ", timezoneOffset='" + timezoneOffset + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
