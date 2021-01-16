package com.example.gogogal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Todo_Day {
    @PrimaryKey(autoGenerate = true)
    private  int id;
    private String title;
    private int progr;
    private int day;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Todo_Day(String title, int progr,int day) {
        this.title = title;
        this.progr = progr;
        this.day = day;
    }

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

    @Override
    public String toString() {
        return "Todo_Day{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", progr=" + progr +
                ", day=" + day +
                '}';
    }
}
