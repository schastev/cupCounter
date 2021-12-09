package com.example.cupcounter.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.cupcounter.R;
import com.example.cupcounter.database.AppDatabase;
import com.example.cupcounter.database.Customer;
import com.example.cupcounter.database.CustomerDAO;
import com.example.cupcounter.database.DBClient;

import java.time.LocalDate;

public class AddNewCustomerActivity extends AppCompatActivity {

    AppDatabase db;
    CustomerDAO customerDAO;
    Toast toast;
    EditText phoneField, nameField;
    Resources res;
    SharedPreferences defaultSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        setUpDatabase();
        initializeUiElements();
        setUpAdditionalResources();
    }

    private void setUpDatabase() {
        db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();
    }

    private void initializeUiElements() {
        phoneField = findViewById(R.id.new_input_phone);
        nameField = findViewById(R.id.new_input_name);
    }

    private void setUpAdditionalResources() {
        toast = new Toast(getApplicationContext());
        res = getResources();
        defaultSettings = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void addCustomer(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        String phoneNumber = phoneField.getText().toString();
        String name = nameField.getText().toString();
        String validName = null;
        String validNumber = null;
        if (name.matches("[a-zA-Z ]+") || name.matches("[а-яА-Я ]+")) {
            validName = name;
        } else {
            toast = Toast.makeText(getApplicationContext(), res.getString(R.string.new_toast_name_error), Toast.LENGTH_LONG);
            toast.show();
        }
        if (phoneNumber.matches("[0-9]+")) {
            validNumber = phoneNumber;
        } else {
            toast = Toast.makeText(getApplicationContext(), res.getString(R.string.new_toast_phone_error), Toast.LENGTH_SHORT);
            toast.show();
        }
        if (validName != null && validNumber != null) {
            int defaultCups = 0;
            if (defaultSettings.getBoolean("settings_add_cup_to_new_customer", false)) {
                defaultCups = 1;
            }
            Customer newCustomer = new Customer(validName, validNumber, LocalDate.now(), LocalDate.now(), defaultCups);
            customerDAO.insert(newCustomer);
            toast = Toast.makeText(getApplicationContext(), res.getString(R.string.new_toast_added), Toast.LENGTH_SHORT);
            toast.show();
            startActivity(intent);
        }
    }
}