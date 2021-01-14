package com.example.gogogal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Collection;
import java.util.List;

@Dao
public interface TodoDao {

    @Query("SELECT * FROM Todo")
    List<Todo> getAll();

    @Query("SELECT * FROM Todo WHERE id =:id_value")
    List<Todo> select(int id_value);

    @Query("SELECT title FROM Todo WHERE id=:id_value")
    public String getTitle(int id_value);

    //받은 제목으로 검색해서 데이터 가져오기
    @Query("SELECT * FROM Todo WHERE title =:title")
    public List<Todo> getDate(String title);

    @Query("DELETE FROM Todo WHERE title=:title")
    public int getDelete_title(String title);
    @Insert
    void insert(Todo todo);

    @Query("UPDATE Todo SET title=:title, count_check=:count_check, ex_all_count=:ex_all_count, ex_set=:ex_set, progr=:progr, day=:day  WHERE id=:id_value")
    public void upDate(String title,int ex_all_count,int ex_set,int count_check,int progr ,int id_value, int day);

    @Delete
    void delete(Todo todo);


}
