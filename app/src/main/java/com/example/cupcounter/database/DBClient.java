package com.example.cupcounter.database;

import android.content.Context;

import androidx.room.Room;

public class DBClient {
    private Context mCtx;
    private static DBClient mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private DBClient(Context mCtx) {
        this.mCtx = mCtx;
        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "cup_counter.db")//todo remove the following two bits before deployment
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public static synchronized DBClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DBClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
