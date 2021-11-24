package com.example.cupcounter;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cupcounter.database.AppDatabase;
import com.example.cupcounter.database.Customer;
import com.example.cupcounter.database.CustomerDAO;
import com.example.cupcounter.database.DBClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DisplayCustomerInfoActivity extends AppCompatActivity {

    AppDatabase db;
    CustomerDAO customerDAO;
    int FREE_CUP_THRESHOLD = 5; //cups customers need to buy before they can claim one free cup. Will be defined in the app's settings
    Customer customer;
    int customerId;
    TextView phoneField, nameField, cupNumberField, registrationField, lastVisitField;
    //fields to keep new values until they are recorded in the database
    int newCups;
    String newPhoneNumber;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_customer_info);
        db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();
        customerId = (int) this.getIntent().getExtras().get("CUSTOMER_ID");
        customer = customerDAO.getById(customerId);
        //find text fields
        nameField = findViewById(R.id.info_field_name);
        phoneField = findViewById(R.id.info_field_phone);
        cupNumberField = findViewById(R.id.info_field_cups);
        registrationField = findViewById(R.id.info_field_registration);
        lastVisitField = findViewById(R.id.info_field_last_visit);

        //set field values using data from selected customer
        phoneField.setText(customer.getPhoneNumber());
        nameField.setText(customer.getName());
        cupNumberField.setText(String.valueOf(customer.getCups()));
        registrationField.setText(formatDate(customer.getRegistrationDate()));
        lastVisitField.setText(formatDate(customer.getLastVisit()));

        newCups = customer.getCups();

//        displayFreeCoffeeButton(newCups);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String formatDate(LocalDate date) {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                .withLocale(new Locale("ru"))
                .format(date);
    }

    public void useFreeCoffee(View view) {
        newCups = newCups - FREE_CUP_THRESHOLD;
        cupNumberField.setText(String.valueOf(newCups));
    }

    public void addCoffee(View view) {
        newCups = newCups + 1;
        cupNumberField.setText(String.valueOf(newCups));
    }

    public int displayFreeCoffeeButton(int numberOfCups) {
        return numberOfCups / FREE_CUP_THRESHOLD;
    }

    public void revertPhoneNumber() {
        phoneField.setText(customer.getPhoneNumber());
    }

    public void updateCustomer(View view) {
        int newCups = Integer.parseInt(String.valueOf(cupNumberField.getText()));
        newPhoneNumber = String.valueOf(phoneField.getText());
        customer.setCups(newCups);
        customer.setPhoneNumber(newPhoneNumber);
        customerDAO.update(customer);
        Intent intent = new Intent(this, MainActivity.class);
        Toast toast = Toast.makeText(getApplicationContext(), "Данные клиента обновлены успешно", Toast.LENGTH_SHORT);
        toast.show();
        startActivity(intent);

    }
}