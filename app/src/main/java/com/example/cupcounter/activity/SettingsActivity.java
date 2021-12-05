package com.example.cupcounter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cupcounter.R;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences settings;
    TextView returningCustomerInputField;
    TextView freeCupField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settings = getSharedPreferences("Constants", 0);
        freeCupField = findViewById(R.id.settings_input_free_cup);
        returningCustomerInputField = findViewById(R.id.settings_input_returning_client);
        int freeCup = settings.getInt("Free cup", 5);
        int returningCustomer= settings.getInt("Returning client", 30);
        freeCupField.setText(String.valueOf(freeCup));
        returningCustomerInputField.setText(String.valueOf(returningCustomer));
    }

    public void saveSettings(View view) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("Returning client", Integer.parseInt(returningCustomerInputField.getText().toString()));
        editor.putInt("Free cup", Integer.parseInt(freeCupField.getText().toString()));
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}