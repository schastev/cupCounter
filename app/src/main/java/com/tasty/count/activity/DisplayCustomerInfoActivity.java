package com.tasty.count.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.tasty.count.R;
import com.tasty.count.VisibilityChecker;
import com.tasty.count.database.AppDatabase;
import com.tasty.count.database.Customer;
import com.tasty.count.database.CustomerDAO;
import com.tasty.count.database.DBClient;
import com.tasty.count.fragments.DialogFragment;
import com.tasty.count.fragments.InfoDisplayFragment;
import com.tasty.count.toolbar.ToolbarHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DisplayCustomerInfoActivity extends AppCompatActivity implements DialogFragment.OnDataPass {

    private CustomerDAO customerDAO;
    private Customer customer;
    private Resources res;
    private SharedPreferences sharedPreferences;
    private int FREE_CUP;
    private int RETURNING_CUSTOMER;

    private TextView cupNumberField, lostClientAlert, freeCupsAlert;
    private MaterialButton claimCoffeeButton, revertCupsButton, deleteCustomerButton, saveChangesButton;
    private InfoDisplayFragment phoneFragment;
    private SwitchMaterial datesSwitch;

    //fields to keep new values until they are recorded in the database
    private int newCups;
    private String newPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpAdditionalResources();
        setUpDatabase();
        setUpCustomer();
        setContentView(R.layout.activity_display_customer_info);
        setConstants();
        initializeUiElements();
        setFieldValues();
        checkClaimButtonVisibility();
        checkReturningClientAlertVisibility();
        checkRevertCupsButtonVisibility();
        checkDeleteButtonVisibility();
    }

    private void setUpDatabase() {
        AppDatabase db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();
    }

    private void initializeUiElements() {
        MaterialToolbar toolbar = ToolbarHelper.setUpToolbar(this, R.string.info_title);
        toolbar.setOnMenuItemClickListener(menuItem -> ToolbarHelper.setListenerBoth(this, menuItem));
        cupNumberField = findViewById(R.id.info_field_cups);
        lostClientAlert = findViewById(R.id.info_alert_lost);
        freeCupsAlert = findViewById(R.id.info_alert_free_cups);

        claimCoffeeButton = findViewById(R.id.info_button_claim);
        revertCupsButton = findViewById(R.id.info_button_revert_cups);
        saveChangesButton = findViewById(R.id.info_button_update);
        deleteCustomerButton = findViewById(R.id.info_button_delete_customer);
        datesSwitch = findViewById(R.id.info_switch_dates);
        datesSwitch.setChecked(false);
        datesSwitch.setText(res.getString(R.string.info_hint_show_dates_on_customer_screen));

        InfoDisplayFragment registrationFragment = InfoDisplayFragment.newInstance(res.getString(R.string.info_hint_registration_date), formatDate(customer.getRegistrationDate()));
        InfoDisplayFragment lastVisitFragment = InfoDisplayFragment.newInstance(res.getString(R.string.info_hint_last_visit_date), formatDate(customer.getLastVisit()));
        InfoDisplayFragment nameFragment = InfoDisplayFragment.newInstance(res.getString(R.string.info_hint_name), customer.getName());
        phoneFragment = InfoDisplayFragment.newInstance(res.getString(R.string.info_hint_phone), customer.getPhoneNumber());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.info_field_container_name, nameFragment)
                .replace(R.id.info_field_container_phone, phoneFragment)
                .replace(R.id.info_field_container_registration, registrationFragment)
                .replace(R.id.info_field_container_last_visit, lastVisitFragment)
                .hide(registrationFragment)
                .hide(lastVisitFragment)
                .setReorderingAllowed(true)
                .commit();
        datesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FragmentTransaction ftDates = getSupportFragmentManager().beginTransaction();
            if (datesSwitch.isChecked()) {
                datesSwitch.setText(res.getString(R.string.info_hint_hide_dates_on_customer_screen));
                ftDates.show(registrationFragment)
                        .show(lastVisitFragment)
                        .setReorderingAllowed(true)
                        .commit();
            } else {
                datesSwitch.setText(res.getString(R.string.info_hint_show_dates_on_customer_screen));
                ftDates.hide(registrationFragment)
                        .hide(lastVisitFragment)
                        .setReorderingAllowed(true)
                        .commit();
            }
        });
    }

    private void setFieldValues() {
        newCups = customer.getCups();
        cupNumberField.setText(String.valueOf(customer.getCups()));
    }

    private void setUpAdditionalResources() {
        res = getResources();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void setConstants() {
        FREE_CUP = Integer.parseInt(sharedPreferences.getString(res.getString(R.string.placeholder_setting_free_cup), "5"));
        RETURNING_CUSTOMER = Integer.parseInt(sharedPreferences.getString(res.getString(R.string.placeholder_setting_returning_customer), "30"));
    }

    private void setUpCustomer() {
        int customerId = (int) this.getIntent().getExtras().get(res.getString(R.string.placeholder_extra_customer_id));
        customer = customerDAO.getById(customerId);
    }

    private void checkReturningClientAlertVisibility() {
        int visibility = VisibilityChecker.isReturningClient(customer.getLastVisit(), RETURNING_CUSTOMER);
        lostClientAlert.setVisibility(visibility);
    }

    private void checkClaimButtonVisibility() {
        int visibility = VisibilityChecker.canClaimCup(newCups, FREE_CUP);
        if (visibility == View.VISIBLE) {
            String cupsAlertTest = String.format(res.getString(R.string.info_alert_free_cups),
                    newCups / FREE_CUP);
            freeCupsAlert.setText(cupsAlertTest);
        }
        freeCupsAlert.setVisibility(visibility);
        claimCoffeeButton.setVisibility(visibility);
    }

    private void checkRevertCupsButtonVisibility() {
        int visibility = VisibilityChecker.canRevertChanges(customer.getCups(), cupNumberField.getText());
        revertCupsButton.setVisibility(visibility);
        saveChangesButton.setVisibility(visibility);
    }

    private void checkDeleteButtonVisibility() {
        int visibility = VisibilityChecker.canDelete((boolean) this.getIntent().getExtras()
                .get(res.getString(R.string.placeholder_extra_delete_customer)));
        deleteCustomerButton.setVisibility(visibility);
    }

    private String formatDate(LocalDate date) {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                .withLocale(new Locale(res.getString(R.string.placeholder_locale)))
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
        checkRevertCupsButtonVisibility();
    }

    public void callNumberEditDialog(View view) {
        DialogFragment phoneInputDialogFragment = new DialogFragment();
        phoneInputDialogFragment.show(getSupportFragmentManager(), res.getString(R.string.placeholder_phone_fragment_tag));
    }

    public void updatePhone() {
        if (!newPhoneNumber.equals(customer.getPhoneNumber())) {
            customer.setPhoneNumber(newPhoneNumber);
            Toast phoneUpdated = Toast.makeText(getApplicationContext(), res.getString(R.string.info_toast_phone_updated), Toast.LENGTH_SHORT);
            customer.setLastVisit(LocalDate.now());
            customerDAO.update(customer);
            phoneUpdated.show();
            InfoDisplayFragment fr = InfoDisplayFragment.newInstance(res.getString(R.string.info_hint_phone), customer.getPhoneNumber());
            getSupportFragmentManager().beginTransaction()
                    .detach(phoneFragment)
                    .replace(R.id.info_field_container_phone, fr)
                    .commit();
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
                .setTitle(res.getString(R.string.info_button_delete_customer))
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_both, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        setConstants();
        initializeUiElements();
    }
}