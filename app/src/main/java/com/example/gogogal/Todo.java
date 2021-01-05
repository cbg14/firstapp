package com.example.gogogal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private  String plan_name;

    public Todo(String plan_name    ) {
        this.plan_name = plan_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }


    //내용 확인할수 있도록
    @Override
    public String toString()   {
        return "Todo{" +
                "id=" + id +
                ", plan_name='" + plan_name + '\'' +
                '}';
    }
}
