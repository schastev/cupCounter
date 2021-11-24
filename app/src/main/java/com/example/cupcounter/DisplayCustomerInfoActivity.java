package com.example.cupcounter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    int RETURNING_CLIENT_THRESHHOLD = 30; //the number of days that have to pass since the last visit for the client to be considered lost. If they return afterwards, display a notification for the barista. This should also be defined in the settings.
    Customer customer;
    TextView phoneField, nameField, cupNumberField, registrationField, lastVisitField, lostClientAlert;
    Button claimCoffeeButton;
    //fields to keep new values until they are recorded in the database
    int newCups;
    String newPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_customer_info);
        db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();
        int customerId = (int) this.getIntent().getExtras().get("CUSTOMER_ID");
        customer = customerDAO.getById(customerId);
        //find text fields
        nameField = findViewById(R.id.info_field_name);
        phoneField = findViewById(R.id.info_field_phone);
        cupNumberField = findViewById(R.id.info_field_cups);
        registrationField = findViewById(R.id.info_field_registration);
        lastVisitField = findViewById(R.id.info_field_last_visit);
        claimCoffeeButton = findViewById(R.id.info_button_claim);
        lostClientAlert = findViewById(R.id.info_alert_lost);

        //set field values using data from selected customer
        phoneField.setText(customer.getPhoneNumber());
        nameField.setText(customer.getName());
        cupNumberField.setText(String.valueOf(customer.getCups()));
        registrationField.setText(formatDate(customer.getRegistrationDate()));
        lastVisitField.setText(formatDate(customer.getLastVisit()));

        newCups = customer.getCups();
        checkClaimButtonVisibility();
        checkReturningClient();
    }

    private String formatDate(LocalDate date) {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                .withLocale(new Locale("ru"))
                .format(date);
    }

    public void claimCoffee(View view) {
        newCups = newCups - FREE_CUP_THRESHOLD;
        cupNumberField.setText(String.valueOf(newCups));
        checkClaimButtonVisibility();
    }

    public void addCoffee(View view) {
        newCups = newCups + 1;
        cupNumberField.setText(String.valueOf(newCups));
        checkClaimButtonVisibility();
    }

    private void checkClaimButtonVisibility() {
        if (newCups / FREE_CUP_THRESHOLD >= 1) {
            claimCoffeeButton.setVisibility(View.VISIBLE);
        } else {
            claimCoffeeButton.setVisibility(View.INVISIBLE);
        }
    }

    private void checkReturningClient() {
        if (customer.getLastVisit().isBefore(LocalDate.now().minusDays(RETURNING_CLIENT_THRESHHOLD))) {
            lostClientAlert.setVisibility(View.VISIBLE);
        } else {
            lostClientAlert.setVisibility(View.INVISIBLE);
        }
    }

    public void revertPhoneNumber() {
        phoneField.setText(customer.getPhoneNumber());
    }

    public void updateCustomer(View view) {
        int newCups = Integer.parseInt(String.valueOf(cupNumberField.getText()));
        newPhoneNumber = String.valueOf(phoneField.getText());
        Toast cupsUpdated = null;
        Toast phoneUpdated = null;
        if (newCups != customer.getCups()) {
            customer.setCups(newCups);
            cupsUpdated = Toast.makeText(getApplicationContext(), "Данные о кружках обновлены успешно", Toast.LENGTH_SHORT);
        }
        if (!newPhoneNumber.equals(customer.getPhoneNumber())) {
            customer.setPhoneNumber(newPhoneNumber);
            phoneUpdated = Toast.makeText(getApplicationContext(), "Номер телефона обновлён успешно", Toast.LENGTH_SHORT);
        }
        customerDAO.update(customer);
        Intent intent = new Intent(this, MainActivity.class);
        if (phoneUpdated != null) phoneUpdated.show();
        if (cupsUpdated != null) cupsUpdated.show();
        startActivity(intent);
    }
}