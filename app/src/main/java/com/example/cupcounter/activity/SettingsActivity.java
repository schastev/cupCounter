package com.example.cupcounter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;

import com.example.cupcounter.R;

import com.example.cupcounter.settings.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container_options, new SettingsFragment())
                .commit();
        setContentView(R.layout.activity_settings);
        setUpAdditionalResources();
    }

    private void setUpAdditionalResources() {
        res = getResources();
    }


}