package com.example.cupcounter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cupcounter.customerlist.CustomerAdapter;
import com.example.cupcounter.customerlist.OnItemClickListener;
import com.example.cupcounter.database.AppDatabase;
import com.example.cupcounter.database.Customer;
import com.example.cupcounter.database.CustomerDAO;
import com.example.cupcounter.database.DBClient;

import java.util.List;

public class DisplaySearchResultsActivity extends AppCompatActivity implements OnItemClickListener {

    AppDatabase db;
    CustomerDAO customerDAO;
    List<Customer> foundCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search_results);
        db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String phoneNumberEnding = intent.getStringExtra(com.example.cupcounter.MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(phoneNumberEnding);

        RecyclerView listView = findViewById(R.id.client_names);
        // находим клиентов по телефону
        foundCustomers = customerDAO.findByShortNumber("%" + phoneNumberEnding);
        // используем адаптер данных
        CustomerAdapter adapter = new CustomerAdapter(this, foundCustomers);
        // Привяжем массив через адаптер к ListView
        listView.setAdapter(adapter);
        adapter.setClickListener(this);
    }
    public void onClick(View view, int position) {
        final Customer customer = foundCustomers.get(position);
        Intent intent = new Intent(this, DisplayCustomerInfoActivity.class);
        intent.putExtra("CUSTOMER_ID", customer.getId());
        startActivity(intent);
    }
}