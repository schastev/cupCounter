package com.example.cupcounter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = DBClient.getInstance(getApplicationContext()).getAppDatabase();
        customerDAO = db.customerDao();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        toast = new Toast(getApplicationContext());
        phoneField = findViewById(R.id.new_input_phone);
        nameField = findViewById(R.id.new_input_name);
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
            nameField.setText("");
            toast = Toast.makeText(getApplicationContext(), "Введите имя, состоящее только из символов русского или английского алфавита и пробелов", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (phoneNumber.matches("[0-9]+")) {
            validNumber = phoneNumber;
        } else {
            phoneField.setText("");
            toast = Toast.makeText(getApplicationContext(), "Введите телефон, состоящий только из цифр", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (validName != null && validNumber != null) {
            Customer newCustomer = new Customer(validName, validNumber, LocalDate.now(), LocalDate.now(), 1);
            customerDAO.insert(newCustomer);
            toast = Toast.makeText(getApplicationContext(), "Клиент добавлен успешно", Toast.LENGTH_SHORT);
            toast.show();
            startActivity(intent);
        }
    }
}