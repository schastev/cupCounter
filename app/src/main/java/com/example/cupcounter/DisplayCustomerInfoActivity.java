package com.example.cupcounter;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cupcounter.database.AppDatabase;
import com.example.cupcounter.database.Customer;
import com.example.cupcounter.database.CustomerDAO;
import com.example.cupcounter.database.DBClient;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DisplayCustomerInfoActivity extends AppCompatActivity {

    AppDatabase db;
    CustomerDAO customerDAO;
    int FREE_CUP_THRESHOLD = 5;
    Customer customer;
    int customerId;

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        TextView registrationField = findViewById(R.id.RegistrationDate);
        registrationField.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                .withLocale(new Locale("ru"))
                .format(customer.getRegistrationDate()));
        TextView lastVisitField = findViewById(R.id.LastVisitDate);
        lastVisitField.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                .withLocale(new Locale("ru"))
                .format(customer.getLastVisit()));
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