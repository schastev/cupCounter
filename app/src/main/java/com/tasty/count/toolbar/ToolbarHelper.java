package com.tasty.count.toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.tasty.count.R;
import com.tasty.count.activity.AddNewCustomerActivity;
import com.tasty.count.fragments.PasswordPromptFragment;

import java.util.Objects;

public class ToolbarHelper {
    private static Resources res;

    public static MaterialToolbar setUpToolbar(AppCompatActivity activity, int title) {
        res = activity.getResources();
        MaterialToolbar myToolbar = activity.findViewById(R.id.my_toolbar);
        activity.setSupportActionBar(myToolbar);
        Objects.requireNonNull(activity.getSupportActionBar()).setTitle(title);
        return myToolbar;
    }

    public static boolean setListenerBoth(Activity activity, MenuItem item) {
        if (item.getItemId() == R.id.toolbar_button_add) {
            return moveToAddCustomer(activity);
        } else if (item.getItemId() == R.id.toolbar_button_settings) {
            return moveToSettings(activity);
        } else {
            return false;
        }
    }

    public static boolean setListenerAddOnly(Activity activity, MenuItem item) {
        if (item.getItemId() == R.id.toolbar_button_add) {
            return moveToAddCustomer(activity);
        }
        return false;
    }

    public static boolean setListenerSettingsOnly(Activity activity, MenuItem item) {
        if (item.getItemId() == R.id.toolbar_button_settings) {
            return moveToSettings(activity);
        }
        return false;
    }

    private static <T> boolean moveToSettings(T activity) {
        PasswordPromptFragment passwordPromptFragment = new PasswordPromptFragment();
        passwordPromptFragment.show(((AppCompatActivity) activity).getSupportFragmentManager(), res.getString(R.string.toolbar_fragment_tag));
        return true;
    }

    private static boolean moveToAddCustomer(Activity activity) {
        Intent intent = new Intent(activity, AddNewCustomerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        return true;
    }
}
