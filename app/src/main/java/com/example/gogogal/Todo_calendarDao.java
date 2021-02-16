package com.example.gogogal;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Todo_calendarDao {

    @Query("SELECT * FROM Todo_calendar")
    List<Todo_calendar> getAll();

    @Insert
    void insert(Todo_calendar todo_calendar);

    @Query("SELECT * FROM todo_calendar WHERE year=:year and month=:month and week_day=:week_day")
    public List<Todo_calendar> calendar_find_date(int year, int month , int week_day);

    @Query("UPDATE todo_calendar SET title=:title WHERE id=:id")
    public void UPDATE(String title ,int id);

    @Query("SELECT *FROM Todo_calendar WHERE id=:id")
   List<Todo_calendar> select(int id);
}
