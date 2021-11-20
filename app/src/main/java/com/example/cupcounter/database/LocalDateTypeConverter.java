package com.example.cupcounter.database;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.LocalDate;

public class LocalDateTypeConverter {
    @TypeConverter
    public String fromLocalDate(LocalDate date) {
        return date.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public LocalDate toLocalDate(String date) {
        return LocalDate.parse(date);
    }
}
