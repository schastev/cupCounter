package com.example.cupcounter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    boolean deleteCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpDatabase();
        initializeUiElements();
        deleteCustomer = Boolean.parseBoolean(getIntent().getStringExtra(String.valueOf(R.string.placeholder_extra_delete_customer)));
        ToolbarHelper.setUpToolbar(this, R.string.main_title);
    }

    private void setUpDatabase() {
        db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();
    }

    private void initializeUiElements() {
        phoneSearchField = findViewById(R.id.enterPhoneNumberToSearch);
        customerNameList = findViewById(R.id.result_list_names);
        customerNameList.setVisibility(View.INVISIBLE);
    }

    public void addCustomer(View view) {
        Intent intent = new Intent(this, AddNewCustomerActivity.class);
        startActivity(intent);
    }

    public void findCustomer(View view) {
        String numberEnding = phoneSearchField.getText().toString();
        foundCustomers = customerDAO.findByShortNumber("%" + numberEnding)
                .stream()
                .sorted()
                .collect(Collectors.toList());
        if (foundCustomers.size() == 1) {
            goToCustomerCard(0);
        } else {
            CustomerAdapter adapter = new CustomerAdapter(this, foundCustomers);
            // Привяжем массив через адаптер к ListView
            customerNameList.setAdapter(adapter);
            customerNameList.setVisibility(View.VISIBLE);
            adapter.setClickListener(this);
        }
    }

    public void onClick(View view, int position) {
        goToCustomerCard(position);
    }

    private void goToCustomerCard(int position) {
        final Customer customer = foundCustomers.get(position);
        Intent intent = new Intent(this, DisplayCustomerInfoActivity.class);
        intent.putExtra(String.valueOf(R.string.placeholder_extra_customer_id), customer.getId());
        intent.putExtra(String.valueOf(R.string.placeholder_extra_delete_customer), deleteCustomer);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.toolbar_button_add:
                intent = new Intent(this, AddNewCustomerActivity.class);
                startActivity(intent);
                return true;

            case R.id.toolbar_button_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_both, menu);
        return true;
    }
}