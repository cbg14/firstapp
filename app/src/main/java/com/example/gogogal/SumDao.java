package com.example.gogogal;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SumDao {
    @Query("SELECT * FROM Sum")
    List<Sum> getAll();

    @Insert
    void insert(Sum sum);
    //받은 제목 대비해서 삭제
    @Query("DELETE FROM Sum WHERE title=:title AND sum=:sum ")
    public int getDelete_title(String title, int sum);

    @Query("SELECT title FROM Sum WHERE id=:id_value")
    public String getTitle(int id_value);

    //받은 제목으로 검색해서 데이터 가져오기
    @Query("SELECT * FROM Sum WHERE title =:title")
    public List<Sum> getDate(String title);


    //이름만 업데이트
    @Query("UPDATE Sum SET title =:title WHERE title=:befor_title")
    public void upDate_name(String title,String befor_title);


    @Query("UPDATE Sum SET title=:title, sum=:count_check WHERE id=:id_value")
    public void upDate(String title,int count_check , int id_value);

}
