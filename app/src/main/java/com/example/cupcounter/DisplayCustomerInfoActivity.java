package com.example.cupcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.cupcounter.customerlist.OnItemClickListener;
import com.example.cupcounter.database.AppDatabase;
import com.example.cupcounter.database.Customer;
import com.example.cupcounter.database.CustomerDAO;
import com.example.cupcounter.database.DBClient;

public class DisplayCustomerInfoActivity extends AppCompatActivity {

    AppDatabase db;
    CustomerDAO customerDAO;
    int FREE_CUP_THRESHOLD = 5;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_customer_info);
        db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();
//        displayFreeCoffeeButton(customer.getCups());
        Object customerId = savedInstanceState.get("CUSTOMER_ID");
    }

    public void updatePhoneNumber(String newNumber) {
        customer.setPhoneNumber(newNumber);
        customerDAO.insert(customer);
    }

    public void updateCups(int newNumber) {
        if (newNumber < customer.getCups()) {
            return;
        }
        customer.setCups(newNumber);
    }

    public void useFreeCoffee() {
        int newNumber = customer.getCups() - FREE_CUP_THRESHOLD;
        customer.setCups(newNumber);
        customerDAO.update(customer);
    }

    public int displayFreeCoffeeButton(int numberOfCups) {
        return numberOfCups / FREE_CUP_THRESHOLD;
    }
}