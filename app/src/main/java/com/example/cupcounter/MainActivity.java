package com.example.cupcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.cupcounter.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void findCustomer(View view) {
        Intent intent = new Intent(this, DisplaySearchResultsActivity.class);
        EditText editText = findViewById(R.id.enterPhoneNumberToSearch);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void addCustomer(View view) {
        Intent intent = new Intent(this, AddNewCustomerActivity.class);
        startActivity(intent);
    }
}