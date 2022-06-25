package com.tasty.count.test.db;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.tasty.count.database.AppDatabase;
import com.tasty.count.database.Customer;
import com.tasty.count.database.CustomerDAO;

import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;


public class BaseTest {
    public CustomerDAO customerDAO;
    private AppDatabase db;
    final List<Customer> allCustomers = new ArrayList<>();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .build();
        customerDAO = db.customerDao();
        for (int i = 0; i < 10; i++) {
            Customer customer = new Customer();
            int id = (int) customerDAO.insert(customer);
            customer.setId(id);
            allCustomers.add(customer);
        }
    }

    @After
    public void closeDb() {
        db.close();
    }


}

