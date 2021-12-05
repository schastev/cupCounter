package com.example.cupcounter.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.cupcounter.R;
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
    Customer customer;
    Resources res;
    SharedPreferences defaultSettings;
    int FREE_CUP;
    int RETURNING_CUSTOMER;
    SharedPreferences settings;

    TextView phoneField, nameField, cupNumberField, registrationField, lastVisitField, lostClientAlert, freeCupsAlert;
    Button claimCoffeeButton, revertPhoneButton, revertCupsButton;

    //fields to keep new values until they are recorded in the database
    int newCups;
    String newPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_customer_info);
        setUpDatabase();
        setUpAdditionalResources();
        setConstants();
        setUpCustomer();
        initializeUiElements();
        setFieldValues();

        newCups = customer.getCups();

        checkDateFieldsVisibility();
        checkClaimButtonVisibility();
        checkReturningClient();
        checkRevertPhoneButtonVisibility();
        checkRevertCupsButtonVisibility();
    }

    private void setUpDatabase() {
        db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();
    }

    private void initializeUiElements() {
        nameField = findViewById(R.id.info_field_name);
        phoneField = findViewById(R.id.info_field_phone);
        cupNumberField = findViewById(R.id.info_field_cups);
        registrationField = findViewById(R.id.info_field_registration);
        lastVisitField = findViewById(R.id.info_field_last_visit);
        lostClientAlert = findViewById(R.id.info_alert_lost);
        freeCupsAlert = findViewById(R.id.info_alert_free_cups);

        claimCoffeeButton = findViewById(R.id.info_button_claim);
        revertPhoneButton= findViewById(R.id.info_button_revert_phone);
        revertCupsButton= findViewById(R.id.info_button_revert_cups);
    }

    private void setFieldValues() {
        phoneField.setText(customer.getPhoneNumber());
        nameField.setText(customer.getName());
        cupNumberField.setText(String.valueOf(customer.getCups()));
        registrationField.setText(formatDate(customer.getRegistrationDate()));
        lastVisitField.setText(formatDate(customer.getLastVisit()));
    }

    private void setUpAdditionalResources() {
        res = getResources();
        settings = getSharedPreferences("Constants", 0);
        defaultSettings = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void setConstants() {
        FREE_CUP = settings.getInt("Free cup", 5);
        RETURNING_CUSTOMER = settings.getInt("Returning client", 30);
    }

    private void setUpCustomer() {
        int customerId = (int) this.getIntent().getExtras().get("CUSTOMER_ID");
        customer = customerDAO.getById(customerId);
    }

    private void checkDateFieldsVisibility() {
        if (defaultSettings.getBoolean("settings_show_dates_on_customer_screen", false)) {
            findViewById(R.id.info_hint_last_visit_date).setVisibility(View.VISIBLE);
            findViewById(R.id.info_hint_registration_date).setVisibility(View.VISIBLE);
            registrationField.setVisibility(View.VISIBLE);
            lastVisitField.setVisibility(View.VISIBLE);
        } else {
            registrationField.setVisibility(View.INVISIBLE);
            lastVisitField.setVisibility(View.INVISIBLE);
            findViewById(R.id.info_hint_last_visit_date).setVisibility(View.INVISIBLE);
            findViewById(R.id.info_hint_registration_date).setVisibility(View.INVISIBLE);
        }
    }

    private void checkReturningClient() {
        if (customer.getLastVisit().isBefore(LocalDate.now().minusDays(RETURNING_CUSTOMER))) {
            lostClientAlert.setVisibility(View.VISIBLE);
        } else {
            lostClientAlert.setVisibility(View.INVISIBLE);
        }
    }

    private void checkClaimButtonVisibility() {
        if (newCups / FREE_CUP >= 1) {
            claimCoffeeButton.setVisibility(View.VISIBLE);
            String cupsAlertTest = String.format(res.getString(R.string.info_alert_free_cups), newCups / FREE_CUP);
            freeCupsAlert.setText(cupsAlertTest);
            freeCupsAlert.setVisibility(View.VISIBLE);
        } else {
            claimCoffeeButton.setVisibility(View.INVISIBLE);
            freeCupsAlert.setVisibility(View.INVISIBLE);
        }
    }

    private void checkRevertPhoneButtonVisibility() { //fixme add a listener of some sort to look out for symbols being typed/erased in the field
        if(phoneField.getText().toString().equals(customer.getPhoneNumber())) {
            revertPhoneButton.setVisibility(View.INVISIBLE);
        } else revertPhoneButton.setVisibility(View.VISIBLE);
    }

    private void checkRevertCupsButtonVisibility() {
        if(cupNumberField.getText().equals(String.valueOf(customer.getCups()))) {
            revertCupsButton.setVisibility(View.INVISIBLE);
        } else revertCupsButton.setVisibility(View.VISIBLE);
    }

    private String formatDate(LocalDate date) {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                .withLocale(new Locale("ru"))
                .format(date);
    }

    public void claimCoffee(View view) {
        newCups = newCups - FREE_CUP;
        cupNumberField.setText(String.valueOf(newCups));
        checkClaimButtonVisibility();
        checkRevertCupsButtonVisibility();
    }

    public void addCoffee(View view) {
        newCups = newCups + 1;
        cupNumberField.setText(String.valueOf(newCups));
        checkClaimButtonVisibility();
        checkRevertCupsButtonVisibility();
    }

    public void revertPhoneChanges(View view) {
        phoneField.setText(customer.getPhoneNumber());
    }

    public void revertCupChanges(View view) {
        newCups = customer.getCups();
        cupNumberField.setText(String.valueOf(newCups));
        checkClaimButtonVisibility();
    }

    public void updateCustomer(View view) {
        int newCups = Integer.parseInt(String.valueOf(cupNumberField.getText()));
        newPhoneNumber = String.valueOf(phoneField.getText());
        Toast cupsUpdated = null;
        Toast phoneUpdated = null;
        if (newCups != customer.getCups()) {
            customer.setCups(newCups);
            cupsUpdated = Toast.makeText(getApplicationContext(), res.getString(R.string.info_toast_cups_updated), Toast.LENGTH_SHORT);
            customer.setLastVisit(LocalDate.now());
        }
        if (!newPhoneNumber.equals(customer.getPhoneNumber())) {
            customer.setPhoneNumber(newPhoneNumber);
            phoneUpdated = Toast.makeText(getApplicationContext(), res.getString(R.string.info_toast_phone_updated), Toast.LENGTH_SHORT);
            customer.setLastVisit(LocalDate.now());
        }
        customerDAO.update(customer);
        Intent intent = new Intent(this, MainActivity.class);
        if (phoneUpdated != null) phoneUpdated.show();
        if (cupsUpdated != null) cupsUpdated.show();
        startActivity(intent);
    }
}