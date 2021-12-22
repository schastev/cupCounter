package com.tasty.count.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cupcounter.R;
import com.tasty.count.fragments.SettingsFragment;
import com.tasty.count.toolbar.ToolbarHelper;
import com.google.android.material.appbar.MaterialToolbar;


public class SettingsActivity extends AppCompatActivity {
    Resources res;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.settings_container_options, new SettingsFragment())
                .commit();
        setContentView(R.layout.activity_settings);
        setUpAdditionalResources();
        toolbar = ToolbarHelper.setUpToolbar(this, R.string.main_button_settings);
        toolbar.setOnMenuItemClickListener(menuItem -> ToolbarHelper.setListenerAddOnly(this, menuItem));
    }

    private void setUpAdditionalResources() {
        res = getResources();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_only, menu);
        return true;
    }

}