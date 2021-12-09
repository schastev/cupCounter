package com.example.cupcounter.database;

import android.content.Context;

import androidx.room.Room;

public class DBClient {
    private Context mCtx;
    private static DBClient mInstance;

    private AppDatabase appDatabase;

    private DBClient(Context mCtx) {
        this.mCtx = mCtx;
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "cup_counter.db")//todo remove the main thread setting
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
