package com.tasty.count.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.cupcounter.R;
import com.tasty.count.activity.MainActivity;

public class SettingsFragment extends PreferenceFragmentCompat {
    Resources res;
    Toast toast;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setUpAdditionalResources();
        setPreferencesFromResource(R.xml.preferences, rootKey);
        returningCustomerSetting();
        freeCupsSetting();
        deleteCustomerSetting();
    }

    private void setUpAdditionalResources() {
        res = getResources();
        toast = Toast.makeText(getContext(), res.getString(R.string.settings_toast_invalid_number), Toast.LENGTH_SHORT);
    }

    private void returningCustomerSetting() {
        EditTextPreference returningPreference = findPreference(res.getString(R.string.placeholder_setting_returning_customer));
        assert returningPreference != null;
        returningPreference.setSummaryProvider(EditTextPreference.SimpleSummaryProvider.getInstance());
        returningPreference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));
        returningPreference.setOnPreferenceChangeListener((preference, newValue) -> validateIntSetting(String.valueOf(newValue)));
    }

    private void freeCupsSetting() {
        EditTextPreference freeCupPreference = findPreference(res.getString(R.string.placeholder_setting_free_cup));
        assert freeCupPreference != null;
        freeCupPreference.setSummaryProvider(EditTextPreference.SimpleSummaryProvider.getInstance());
        freeCupPreference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));
        freeCupPreference.setOnPreferenceChangeListener((preference, newValue) -> validateIntSetting(String.valueOf(newValue)));
    }

    private void deleteCustomerSetting() {
        Preference deleteCustomer = findPreference(res.getString(R.string.placeholder_setting_delete));
        assert deleteCustomer != null;
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(res.getString(R.string.placeholder_extra_delete_customer), "true");
        deleteCustomer.setIntent(intent);
    }

    private boolean validateIntSetting(String newValue) {
        try {
            int number = Integer.parseInt(newValue);
            if (number < 1) {
                toast.show();
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            toast.show();
            return false;
        }
    }

}
