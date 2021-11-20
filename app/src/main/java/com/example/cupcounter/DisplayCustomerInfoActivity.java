package com.example.cupcounter;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cupcounter.database.AppDatabase;
import com.example.cupcounter.database.Customer;
import com.example.cupcounter.database.CustomerDAO;
import com.example.cupcounter.database.DBClient;

public class DisplayCustomerInfoActivity extends AppCompatActivity {

    AppDatabase db;
    CustomerDAO customerDAO;
    int FREE_CUP_THRESHOLD = 5;
    Customer customer;
    int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_customer_info);
        db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();
        customerId = (int) this.getIntent().getExtras().get("CUSTOMER_ID");
        customer = customerDAO.getById(customerId);
        TextView phoneField = findViewById(R.id.CustomerPhoneNumber);
        phoneField.setText(customer.getPhoneNumber());
        TextView nameField = findViewById(R.id.CustomerName);
        nameField.setText(customer.getName());

        //todo change output date format
        TextView registrationField = findViewById(R.id.RegistrationDate);
        registrationField.setText(customer.getRegistrationDate().toString());
        TextView lastVisitField = findViewById(R.id.LastVisitDate);
        lastVisitField.setText(customer.getLastVisit().toString());
//        displayFreeCoffeeButton(customer.getCups());
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