package com.example.gogogal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Sum.class}, version = 1)
public abstract class SumDatabase  extends RoomDatabase {
    private static SumDatabase INSTANCE;
    public abstract SumDao sumDao();

    public static synchronized SumDatabase getInstance(Context context){
        if(INSTANCE ==null) {
            INSTANCE = Room.databaseBuilder(context,SumDatabase.class,"Sum-db").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
