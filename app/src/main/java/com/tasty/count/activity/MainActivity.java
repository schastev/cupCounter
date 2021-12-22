package com.tasty.count.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cupcounter.R;
import com.tasty.count.customerlist.CustomerAdapter;
import com.tasty.count.customerlist.OnItemClickListener;
import com.tasty.count.database.AppDatabase;
import com.tasty.count.database.Customer;
import com.tasty.count.database.CustomerDAO;
import com.tasty.count.database.DBClient;
import com.tasty.count.toolbar.ToolbarHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private TextInputLayout phoneSearchField;
    private RecyclerView customerNameList;
    private AppDatabase db;
    private CustomerDAO customerDAO;
    private List<Customer> foundCustomers;
    private boolean deleteCustomer;
    private Resources res;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpAdditionalResources();
        setContentView(R.layout.activity_main);
        setUpDatabase();
        initializeUiElements();
        deleteCustomer = Boolean.parseBoolean(getIntent().getStringExtra(res.getString(R.string.placeholder_extra_delete_customer)));
    }

    private void setUpDatabase() {
        db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();
    }

    private void initializeUiElements() {
        toolbar = ToolbarHelper.setUpToolbar(this, R.string.main_title);
        toolbar.setOnMenuItemClickListener(menuItem -> ToolbarHelper.setListenerBoth(this, menuItem));
        phoneSearchField = findViewById(R.id.main_field_search);
        customerNameList = findViewById(R.id.main_list_results);
        customerNameList.setVisibility(View.INVISIBLE);
        showSoftKeyboard(phoneSearchField);
        Objects.requireNonNull(phoneSearchField.getEditText()).setOnEditorActionListener((v, actionId, event) -> {
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
        String numberEnding = Objects.requireNonNull(phoneSearchField.getEditText()).getText().toString();
        foundCustomers = customerDAO.findByShortNumber("%" + numberEnding)
                .stream()
                .sorted()
                .collect(Collectors.toList());
        if (foundCustomers.size() == 1) {
            phoneSearchField.setError(null);
            goToCustomerCard(0);
        } else if (foundCustomers.size() == 0) {
            phoneSearchField.setError(res.getString(R.string.main_no_result));
            customerNameList.setVisibility(View.INVISIBLE);
        } else {
            phoneSearchField.setError(null);
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

    public void onClick(View view, int position) {
        goToCustomerCard(position);
    }

    @Override
    public void onResume() {
        super.onResume();
        customerLookUp();
    }

}