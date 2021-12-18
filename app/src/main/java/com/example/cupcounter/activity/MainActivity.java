package com.example.cupcounter.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cupcounter.R;
import com.example.cupcounter.customerlist.CustomerAdapter;
import com.example.cupcounter.customerlist.OnItemClickListener;
import com.example.cupcounter.database.AppDatabase;
import com.example.cupcounter.database.Customer;
import com.example.cupcounter.database.CustomerDAO;
import com.example.cupcounter.database.DBClient;
import com.example.cupcounter.toolbar.ToolbarHelper;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private EditText phoneSearchField;
    private RecyclerView customerNameList;
    private AppDatabase db;
    private CustomerDAO customerDAO;
    private List<Customer> foundCustomers;
    private boolean deleteCustomer;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpAdditionalResources();
        setContentView(R.layout.activity_main);
        setUpDatabase();
        initializeUiElements();
        deleteCustomer = Boolean.parseBoolean(getIntent().getStringExtra(res.getString(R.string.placeholder_extra_delete_customer)));
        ToolbarHelper.setUpToolbar(this, R.string.main_title);
    }

    private void setUpDatabase() {
        db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();
    }

    private void initializeUiElements() {
        phoneSearchField = findViewById(R.id.main_field_search);
        customerNameList = findViewById(R.id.main_list_results);
        customerNameList.setVisibility(View.INVISIBLE);
        showSoftKeyboard(phoneSearchField);
        phoneSearchField.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                customerLookUp();
                handled = true;
            }
            return handled;
        });
    }

    private void setUpAdditionalResources() {
        res = getResources();
    }

    private void customerLookUp() {
        String numberEnding = phoneSearchField.getText().toString();
        foundCustomers = customerDAO.findByShortNumber("%" + numberEnding)
                .stream()
                .sorted()
                .collect(Collectors.toList());
        if (foundCustomers.size() == 1) {
            goToCustomerCard(0);
        } else {
            CustomerAdapter adapter = new CustomerAdapter(this, foundCustomers);
            customerNameList.setAdapter(adapter);
            customerNameList.setVisibility(View.VISIBLE);
            adapter.setClickListener(this);
            hideSoftKeyboard(phoneSearchField);
        }
    }

    private void goToCustomerCard(int position) {
        final Customer customer = foundCustomers.get(position);
        Intent intent = new Intent(this, DisplayCustomerInfoActivity.class);
        intent.putExtra(res.getString(R.string.placeholder_extra_customer_id), customer.getId());
        intent.putExtra(res.getString(R.string.placeholder_extra_delete_customer), deleteCustomer);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_both, menu);
        return true;
    }

    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void addCustomer(View view) {
        Intent intent = new Intent(this, AddNewCustomerActivity.class);
        startActivity(intent);
    }

    public void findCustomer(View view) {
        customerLookUp();
    }

    public void onClick(View view, int position) {
        goToCustomerCard(position);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.toolbar_button_add) {
            intent = new Intent(this, AddNewCustomerActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.toolbar_button_settings) {
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else {
            return false;
        }
    }
}