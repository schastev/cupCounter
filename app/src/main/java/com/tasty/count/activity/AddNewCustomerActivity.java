package com.tasty.count.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.tasty.count.R;
import com.tasty.count.Validator;
import com.tasty.count.database.AppDatabase;
import com.tasty.count.database.Customer;
import com.tasty.count.database.CustomerDAO;
import com.tasty.count.database.DBClient;
import com.tasty.count.toolbar.ToolbarHelper;

import java.time.LocalDate;
import java.util.Objects;

public class AddNewCustomerActivity extends AppCompatActivity {

    private CustomerDAO customerDAO;
    private Toast toast;
    private TextInputLayout phoneField, nameField;
    private Resources res;
    private SwitchMaterial addCupSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        setUpDatabase();
        setUpAdditionalResources();
        initializeUiElements();
    }

    private void setUpDatabase() {
        AppDatabase db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();
    }

    private void initializeUiElements() {
        MaterialToolbar toolbar = ToolbarHelper.setUpToolbar(this, R.string.main_button_add_customer);
        toolbar.setOnMenuItemClickListener(menuItem -> ToolbarHelper.setListenerSettingsOnly(this, menuItem));
        phoneField = findViewById(R.id.new_input_phone);
        nameField = findViewById(R.id.new_input_name);
        addCupSwitch = findViewById(R.id.new_switch_add_cup);
        showSoftKeyboard(phoneField);
    }

    private void setUpAdditionalResources() {
        toast = new Toast(getApplicationContext());
        res = getResources();
    }

    public void addCustomer(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        String phoneNumber = Objects.requireNonNull(phoneField.getEditText()).getText().toString();
        String name = Objects.requireNonNull(nameField.getEditText()).getText().toString();
        String validName = null;
        String validNumber = null;
        if (Validator.validatePhone(phoneNumber, res, phoneField, customerDAO)) {
            validNumber = phoneNumber;
        }
        if (Validator.validateName(name, res, nameField)) {
            nameField.setError(null);
            validName = name;
        }
        if (validName != null && validNumber != null) {
            phoneField.setError(null);
            nameField.setError(null);
            int defaultCups = 0;
            if (addCupSwitch.isChecked()) {
                defaultCups = 1;
            }
            Customer newCustomer = new Customer(validName, validNumber, LocalDate.now(), LocalDate.now(), defaultCups);
            customerDAO.insert(newCustomer);
            toast = Toast.makeText(getApplicationContext(), res.getString(R.string.new_toast_added), Toast.LENGTH_SHORT);
            toast.show();
            startActivity(intent);
        }
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

    @Override
    public void onResume() {
        super.onResume();
        initializeUiElements();
    }
}