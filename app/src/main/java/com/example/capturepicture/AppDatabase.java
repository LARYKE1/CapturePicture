package com.example.capturepicture;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ImageModel.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME="final.db";
    private static AppDatabase appDatabase;

    public static synchronized AppDatabase getInstance(Context context){
        if(appDatabase==null){
            appDatabase= Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
        }

        return appDatabase;
    }

    public abstract ModelDao modelDao();

}
