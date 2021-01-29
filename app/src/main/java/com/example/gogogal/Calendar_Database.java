package com.example.gogogal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Todo_calendar.class} , version = 1)
public abstract class Calendar_Database  extends RoomDatabase {
    private  static Calendar_Database INSTANCE;
    public  abstract  Todo_calendarDao todo_calendarDao();

    public static synchronized  Calendar_Database getINSTANCE(Context context){
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,Calendar_Database.class,"todo_calendar_db").allowMainThreadQueries().build();

        }
        return INSTANCE;
    }

}
