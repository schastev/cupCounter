package com.example.cupcounter.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.cupcounter.R;
import com.example.cupcounter.database.AppDatabase;
import com.example.cupcounter.database.Customer;
import com.example.cupcounter.database.CustomerDAO;
import com.example.cupcounter.database.DBClient;
import com.example.cupcounter.toolbar.ToolbarHelper;

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
        ToolbarHelper.setUpToolbar(this, R.string.main_button_add_customer);
    }

    private void setUpDatabase() {
        db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();
    }

    private void initializeUiElements() {
        phoneField = findViewById(R.id.new_input_phone);
        nameField = findViewById(R.id.new_input_name);
        showSoftKeyboard(phoneField);
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
        if (phoneNumber.matches(res.getString(R.string.placeholder_pattern_digits))) {
            validNumber = phoneNumber;
        } else {
            toast = Toast.makeText(getApplicationContext(), res.getString(R.string.new_toast_phone_error), Toast.LENGTH_SHORT);
            toast.show();
        }
        if (name.matches(res.getString(R.string.placeholder_pattern_en))
                || name.matches(res.getString(R.string.placeholder_pattern_ru))) {
            validName = name;
        } else {
            toast = Toast.makeText(getApplicationContext(), res.getString(R.string.new_toast_name_error), Toast.LENGTH_LONG);
            toast.show();
        }
        if (validName != null && validNumber != null) {
            int defaultCups = 0;
            if (defaultSettings.getBoolean(res.getString(R.string.placeholder_setting_add_cup), false)) {
                defaultCups = 1;
            }
            Customer newCustomer = new Customer(validName, validNumber, LocalDate.now(), LocalDate.now(), defaultCups);
            customerDAO.insert(newCustomer);
            toast = Toast.makeText(getApplicationContext(), res.getString(R.string.new_toast_added), Toast.LENGTH_SHORT);
            toast.show();
            startActivity(intent);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.toolbar_button_settings) {
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings_only, menu);
        return true;
    }

    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}