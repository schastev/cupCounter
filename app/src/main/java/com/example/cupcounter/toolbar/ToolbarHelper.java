package com.example.cupcounter.toolbar;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cupcounter.R;
import com.example.cupcounter.activity.AddNewCustomerActivity;
import com.example.cupcounter.activity.SettingsActivity;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Objects;

public class ToolbarHelper {
    public static MaterialToolbar setUpToolbar(AppCompatActivity activity, int title) {
        MaterialToolbar myToolbar = activity.findViewById(R.id.my_toolbar);
        activity.setSupportActionBar(myToolbar);
        Objects.requireNonNull(activity.getSupportActionBar()).setTitle(title);
        return myToolbar;
    }

    public static boolean setListenerBoth(Activity activity, MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.toolbar_button_add) {
            intent = new Intent(activity, AddNewCustomerActivity.class);
            activity.startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.toolbar_button_settings) {
            intent = new Intent(activity, SettingsActivity.class);
            activity.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    public static boolean setListenerAddOnly(Activity activity, MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.toolbar_button_add) {
            intent = new Intent(activity, AddNewCustomerActivity.class);
            activity.startActivity(intent);
            return true;
        }
        return false;
    }

    public static boolean setListenerSettingsOnly(Activity activity, MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.toolbar_button_settings) {
            intent = new Intent(activity, SettingsActivity.class);
            activity.startActivity(intent);
            return true;
        }
        return false;
    }
}
