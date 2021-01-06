package com.example.gogogal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private  String title;
    private int ex_count;    //운동카운트
    private int ex_set;     //운동세트


    public Todo(String title) {
        this.title = title;
    }


    public Todo(String title, int ex_count, int ex_set) {
        this.title = title;
        this.ex_count = ex_count;
        this.ex_set = ex_set;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
