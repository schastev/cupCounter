package com.example.cupcounter.database;


import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import android.os.Build;

import com.example.cupcounter.CustomerGenerator;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@TypeConverters({LocalDateTypeConverter.class})
public class Customer implements Serializable, Comparable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;
    @ColumnInfo(name = "registration_date")
    private LocalDate registrationDate;
    @ColumnInfo(name = "last_visit")
    private LocalDate lastVisit;
    @ColumnInfo(name = "cups")
    private int cups;


    public Customer(String name, String phoneNumber, LocalDate registrationDate, LocalDate lastVisit, int cups) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
        this.lastVisit = lastVisit;
        this.cups = cups;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Customer() {
        this.name = CustomerGenerator.randomName();
        this.phoneNumber = CustomerGenerator.randomNumber();
        this.registrationDate = CustomerGenerator.randomRegistrationDate();
        this.lastVisit = CustomerGenerator.randomLastVisitDate(registrationDate);
        this.cups = CustomerGenerator.randomCups();
    }

    public boolean equals(Object o) {
        if (!o.getClass().equals(Customer.class)) {
            return false;
        }
        return this.id == ((Customer) o).getId() ||
                this.name.equals(((Customer) o).getName()) ||
                this.phoneNumber.equals(((Customer) o).getPhoneNumber()) ||
                this.registrationDate.equals(((Customer) o).getRegistrationDate()) ||
                this.lastVisit.equals(((Customer) o).getLastVisit()) ||
                this.cups == ((Customer) o).getCups();
    }

    @Override
    public int compareTo(Object o) {
        if (o.getClass().equals(Customer.class)) {
            return this.name.compareTo(((Customer) o).getName());
        }
        return 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public LocalDate getLastVisit() {
        return lastVisit;
    }

    public int getCups() {
        return cups;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName (String name) {this.name = name;}

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setLastVisit(LocalDate lastVisit) {
        this.lastVisit = lastVisit;
    }

    public void setCups(int cups) {
        this.cups = cups;
    }

}

