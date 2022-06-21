package com.tasty.count.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.tasty.count.utils.CustomerGenerator;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@TypeConverters({LocalDateTypeConverter.class})
public class Customer implements Serializable, Comparable<Customer> {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "name")
    private final String name;
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;
    @ColumnInfo(name = "registration_date")
    private final LocalDate registrationDate;
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

    @Ignore
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
    public int compareTo(Customer customer) {
        if (customer.getClass().equals(Customer.class)) {
            return this.name.compareTo(customer.getName());
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

    public Customer setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Customer setLastVisit(LocalDate lastVisit) {
        this.lastVisit = lastVisit;
        return this;
    }

    public Customer setCups(int cups) {
        this.cups = cups;
        return this;
    }

}

