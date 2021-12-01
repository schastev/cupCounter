package com.example.cupcounter.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface CustomerDAO {
    @Query("SELECT * FROM customer")
    Single<List<Customer>> getAll();

    @Query("SELECT * FROM customer WHERE id = :id")
    Single<Customer> getById(long id);

    @Query("SELECT * FROM customer WHERE phone_number LIKE :number")
    Single<List<Customer>> findByShortNumber(String number);

    @Query("SELECT * FROM customer WHERE phone_number = :number")
    Single<List<Customer>> findByFullNumber(String number);

    @Insert
    long insert(Customer customer);

    @Delete
    void delete(Customer customer);

    @Update
    Single<Integer> update(Customer customer);

}
