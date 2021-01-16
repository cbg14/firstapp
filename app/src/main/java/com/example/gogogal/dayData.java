package com.example.gogogal;

public class dayData {
    private String day_todo_name;
    private String day_todo_percent;

    public dayData(String day_todo_name, String day_todo_percent) {
        this.day_todo_name = day_todo_name;
        this.day_todo_percent = day_todo_percent;
    }

    public String getDay_todo_name() {
        return day_todo_name;
    }

    public void setDay_todo_name(String day_todo_name) {
        this.day_todo_name = day_todo_name;
    }

    public String getDay_todo_percent() {
        return day_todo_percent;
    }

    public void setDay_todo_percent(String day_todo_percent) {
        this.day_todo_percent = day_todo_percent;
    }
}
