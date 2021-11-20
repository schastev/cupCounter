package com.example.cupcounter.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Customer.class}, version = 3)

public abstract class AppDatabase extends RoomDatabase {
    public abstract CustomerDAO customerDao();
}

