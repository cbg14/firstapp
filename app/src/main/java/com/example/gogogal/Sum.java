package com.example.gogogal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Sum {
    @PrimaryKey(autoGenerate = true)
    private  int id;
    private String title;
    private int sum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public Sum(String title, int sum) {
        this.title = title;
        this.sum = sum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }


    @Override
    public String toString() {
        return "Sum{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", sum='" + sum + '\'' +
                '}';
    }
}
