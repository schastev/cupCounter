package com.example.cupcounter.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CustomerDAO {
    @Query("SELECT * FROM customer")
    List<Customer> getAll();

    @Query("SELECT * FROM customer WHERE id = :id")
    Customer getById(int id);

    @Query("SELECT * FROM customer WHERE phone_number LIKE :number")
    List<Customer> findByShortNumber(String number);

    @Query("SELECT * FROM customer WHERE phone_number = :number")
    List<Customer> findByFullNumber(String number);

    @Insert
    long insert(Customer customer);

    @Delete
    void delete(Customer customer);

    @Update
    void update(Customer customer);

}
