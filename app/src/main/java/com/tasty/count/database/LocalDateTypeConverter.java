package com.tasty.count.database;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class LocalDateTypeConverter {
    @TypeConverter
    public String fromLocalDate(LocalDate date) {
        return date.toString();
    }

    @TypeConverter
    public LocalDate toLocalDate(String date) {
        return LocalDate.parse(date);
    }
}
