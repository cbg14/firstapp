package com.example.gogogal;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface Todo_dayDao {

    @Query("SELECT * FROM Todo_Day")
    List<Todo_Day> getAll();

    @Insert
    void insert(Todo_Day todo_day);

    @Query("DELETE FROM Todo_Day")
    void deleteAll();

    @Query("UPDATE Todo_Day SET title=:title, progr=:progr WHERE id=:id")
    public void UPDATE(String title,int progr,int id);

    @Query("DELETE FROM Todo_Day WHERE title=:title AND day=:day")
    public int getDelete_title(String title,int day);

    @Query("SELECT * FROM Todo_Day WHERE title =:title")
    public List<Todo_Day> getDate(String title);
}
