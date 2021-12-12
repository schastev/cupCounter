package com.example.cupcounter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cupcounter.R;

import com.example.cupcounter.settings.SettingsFragment;
import com.example.cupcounter.toolbar.ToolbarHelper;


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
        ToolbarHelper.setUpToolbar(this, R.string.main_button_settings);
    }

    private void setUpAdditionalResources() {
        res = getResources();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.toolbar_button_add) {
            intent = new Intent(this, AddNewCustomerActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_only, menu);
        return true;
    }

}