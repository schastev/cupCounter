package com.example.cupcounter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cupcounter.R;

import com.example.cupcounter.settings.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences settings;
    TextView returningCustomerInputField;
    TextView freeCupField;
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container_options, new SettingsFragment())
                .commit();
        setContentView(R.layout.activity_settings);
        initializeUiElements();
        setUpAdditionalResources();
        setFieldValues();
    }

    private void initializeUiElements() {
        settings = getSharedPreferences("Constants", 0);
        freeCupField = findViewById(R.id.settings_input_free_cup);
        returningCustomerInputField = findViewById(R.id.settings_input_returning_client);
    }

    private void setFieldValues() {
        int freeCup = settings.getInt(res.getString(R.string.placeholder_constant_free_cup), 5);
        int returningCustomer= settings.getInt(res.getString(R.string.placeholder_constant_returning_client), 30);
        freeCupField.setText(String.valueOf(freeCup));
        returningCustomerInputField.setText(String.valueOf(returningCustomer));
    }

    private void setUpAdditionalResources() {
        res = getResources();
    }

    public void saveSettings(View view) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(res.getString(R.string.placeholder_constant_returning_client), Integer.parseInt(returningCustomerInputField.getText().toString()));
        editor.putInt(res.getString(R.string.placeholder_constant_free_cup), Integer.parseInt(freeCupField.getText().toString()));
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}