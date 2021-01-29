package com.example.gogogal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Todo_calendar {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int year;
    private int month;
    private int week_day;
    private  String title;
    private int progr;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProgr() {
        return progr;
    }

    public void setProgr(int progr) {
        this.progr = progr;
    }

    public Todo_calendar(int year, int month, int week_day, String title, int progr) {
        this.year = year;
        this.month = month;
        this.week_day = week_day;
        this.title = title;
        this.progr = progr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getWeek_day() {
        return week_day;
    }

    public void setWeek_day(int week_day) {
        this.week_day = week_day;
    }

    @Override
    public String toString() {
        return "Todo_calendar{" +
                "id=" + id +
                ", year=" + year +
                ", month=" + month +
                ", week_day=" + week_day +
                ", title='" + title + '\'' +
                ", progr=" + progr +
                '}';
    }
}
