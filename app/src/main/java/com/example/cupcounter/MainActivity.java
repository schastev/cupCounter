package com.example.cupcounter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cupcounter.customerlist.CustomerAdapter;
import com.example.cupcounter.customerlist.OnItemClickListener;
import com.example.cupcounter.database.AppDatabase;
import com.example.cupcounter.database.Customer;
import com.example.cupcounter.database.CustomerDAO;
import com.example.cupcounter.database.DBClient;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    public static final String EXTRA_MESSAGE = "com.example.cupcounter.MESSAGE";

    private EditText phoneSearchField;
    private RecyclerView customerNameList;
    private AppDatabase db;
    private CustomerDAO customerDAO;
    private List<Customer> foundCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();
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
        // находим клиентов по телефону
        foundCustomers = customerDAO.findByShortNumber("%" + numberEnding)
                .stream()
                .sorted()
                .collect(Collectors.toList());
        // используем адаптер данных
        CustomerAdapter adapter = new CustomerAdapter(this, foundCustomers);
        // Привяжем массив через адаптер к ListView
        customerNameList.setAdapter(adapter);
        customerNameList.setVisibility(View.VISIBLE);
        adapter.setClickListener(this);
    }

    public void onClick(View view, int position) {
        final Customer customer = foundCustomers.get(position);
        Intent intent = new Intent(this, DisplayCustomerInfoActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getId());
        startActivity(intent);
    }
}