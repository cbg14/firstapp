package com.example.gogogal;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private  String title; //제목
    private  int ex_all_count=0;
    private  int ex_set=0;
    private int  progr=0;
    private int count_check=0;

    public int getProgr() {
        return progr;
    }

    public void setProgr(int progr) {
        this.progr = progr;
    }

    public int getCount_check() {
        return count_check;
    }

    public void setCount_check(int count_check) {
        this.count_check = count_check;
    }

    //생성자 아이템추가할떄 제목만 받았을때 쓰는 생성자
    public Todo(String title, int ex_all_count, int ex_set,int progr,int count_check) {
        this.title = title;
        this.ex_all_count = ex_all_count;
        this.ex_set =ex_set;
        this.progr = progr;
        this.count_check = count_check;
    }


    public int getEx_all_count() {
        return ex_all_count;
    }

    public void setEx_all_count(int ex_all_count) {
        this.ex_all_count = ex_all_count;
    }

    public int getEx_set() {
        return ex_set;
    }

    public void setEx_set(int ex_set) {
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
                ", ex_all_count=" + ex_all_count +
                ", ex_set=" + ex_set +
                ", progr=" + progr +
                ", count_check=" + count_check +
                '}';
    }
}
