package com.example.gogogal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Todo_Day.class},version = 1)
public abstract class DayDatabase extends RoomDatabase {
    private static DayDatabase INSTANCE;
    public abstract Todo_dayDao todo_dayDao();

    public static synchronized  DayDatabase getInstance(Context context){
        if(INSTANCE ==null) {
            INSTANCE = Room.databaseBuilder(context,DayDatabase.class,"todo_day_db").allowMainThreadQueries().build();

        }
        return INSTANCE;
    }
    public static void destroyInstance() {INSTANCE = null;}
}
