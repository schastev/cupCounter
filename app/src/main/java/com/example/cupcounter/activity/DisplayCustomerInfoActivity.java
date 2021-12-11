package com.example.cupcounter.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.cupcounter.DialogFragment;
import com.example.cupcounter.R;
import com.example.cupcounter.database.AppDatabase;
import com.example.cupcounter.database.Customer;
import com.example.cupcounter.database.CustomerDAO;
import com.example.cupcounter.database.DBClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DisplayCustomerInfoActivity extends AppCompatActivity implements DialogFragment.OnDataPass {

    AppDatabase db;
    CustomerDAO customerDAO;
    Customer customer;
    Resources res;
    SharedPreferences sharedPreferences;
    int FREE_CUP;
    int RETURNING_CUSTOMER;

    TextView phoneField, nameField, cupNumberField, registrationField, lastVisitField, lostClientAlert, freeCupsAlert;
    Button claimCoffeeButton, revertPhoneButton, revertCupsButton, deleteCustomerButton;

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
        checkRevertCupsButtonVisibility();
        checkDeleteButtonVisibility();
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
        revertPhoneButton = findViewById(R.id.info_button_edit_phone);
        revertCupsButton = findViewById(R.id.info_button_revert_cups);
        deleteCustomerButton = findViewById(R.id.info_button_delete_customer);
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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void setConstants() {
        FREE_CUP = Integer.parseInt(sharedPreferences.getString("setting_free_cups", "5"));
        RETURNING_CUSTOMER = Integer.parseInt(sharedPreferences.getString("setting_returning_customer", "30"));
    }

    private void setUpCustomer() {
        int customerId = (int) this.getIntent().getExtras().get("CUSTOMER_ID");
        customer = customerDAO.getById(customerId);
    }

    private void checkDateFieldsVisibility() {
        if (sharedPreferences.getBoolean("settings_show_dates_on_customer_screen", false)) {
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

    private void checkRevertCupsButtonVisibility() {
        if (cupNumberField.getText().equals(String.valueOf(customer.getCups()))) {
            revertCupsButton.setVisibility(View.INVISIBLE);
        } else revertCupsButton.setVisibility(View.VISIBLE);
    }

    private void checkDeleteButtonVisibility() {
        if ((boolean) this.getIntent().getExtras().get("DELETE_CUSTOMER")) {
            deleteCustomerButton.setVisibility(View.VISIBLE);
        } else {
            deleteCustomerButton.setVisibility(View.INVISIBLE);
        }
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

    public void revertCupChanges(View view) {
        newCups = customer.getCups();
        cupNumberField.setText(String.valueOf(newCups));
        checkClaimButtonVisibility();
    }

    public void callNumberEditDialog(View view) {
        DialogFragment newFragment = new DialogFragment();
        newFragment.show(getSupportFragmentManager(), "edit_phone");
    }

    public void updatePhone() {
        if (!newPhoneNumber.equals(customer.getPhoneNumber())) {
            if (!newPhoneNumber.matches("[0-9]+")) {
                Toast.makeText(getApplicationContext(), res.getString(R.string.new_toast_phone_error), Toast.LENGTH_SHORT).show();
            } else {
                customer.setPhoneNumber(newPhoneNumber);
                Toast phoneUpdated = Toast.makeText(getApplicationContext(), res.getString(R.string.info_toast_phone_updated), Toast.LENGTH_SHORT);
                customer.setLastVisit(LocalDate.now());
                customerDAO.update(customer);
                phoneUpdated.show();
                phoneField.setText(customer.getPhoneNumber());
            }
        }
    }

    public void updateCustomer(View view) {
        Toast cupsUpdated = null;
        if (newCups != customer.getCups()) {
            customer.setCups(newCups);
            cupsUpdated = Toast.makeText(getApplicationContext(), res.getString(R.string.info_toast_cups_updated), Toast.LENGTH_SHORT);
            customer.setLastVisit(LocalDate.now());
        }
        customerDAO.update(customer);
        Intent intent = new Intent(this, MainActivity.class);
        if (cupsUpdated != null) cupsUpdated.show();
        startActivity(intent);
    }

    public void deleteCustomer(View view) {
        new AlertDialog.Builder(this)
                .setTitle(res.getString(R.string.info_dialog_delete_title))
                .setMessage(res.getString(R.string.info_dialog_delete_main))
                .setPositiveButton(R.string.info_dialog_button_delete, (dialog, whichButton) -> {
                    customerDAO.delete(customer);
                    Intent intent = new Intent(this, MainActivity.class);
                    Toast.makeText(getApplicationContext(), res.getString(R.string.info_toast_customer_deleted), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                })
                .setNegativeButton(R.string.info_dialog_button_cancel, null).show();
    }

    @Override
    public void onDataPass(String data) {
        newPhoneNumber = data;
    }
}