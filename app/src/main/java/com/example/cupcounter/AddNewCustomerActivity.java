package com.example.cupcounter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cupcounter.database.AppDatabase;
import com.example.cupcounter.database.Customer;
import com.example.cupcounter.database.CustomerDAO;
import com.example.cupcounter.database.DBClient;

import java.time.LocalDate;
import java.util.List;

public class AddNewCustomerActivity extends AppCompatActivity {

    AppDatabase db;
    CustomerDAO customerDAO;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        toast = new Toast(getApplicationContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addCustomer(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        EditText form = findViewById(R.id.enterPhoneNumber);
        String phoneNumber = form.getText().toString();
        form = findViewById(R.id.enterCustomerName);
        String name = form.getText().toString();
        Customer newCustomer = new Customer(name, phoneNumber, LocalDate.now(), LocalDate.now(), 1);
        customerDAO.insert(newCustomer);
        toast = Toast.makeText(getApplicationContext(), "Клиент добавлен успешно", Toast.LENGTH_SHORT);
        toast.show();
        startActivity(intent);
    }
}