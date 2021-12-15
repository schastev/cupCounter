package com.example.cupcounter.settings;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.cupcounter.R;
import com.example.cupcounter.activity.MainActivity;

public class SettingsFragment extends PreferenceFragmentCompat {
    Resources res;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        res = getResources();
        setPreferencesFromResource(R.xml.preferences, rootKey);
        returningCustomerSetting();
        freeCupsSetting();
        deleteCustomerSetting();
    }

    private void returningCustomerSetting() {
        EditTextPreference returningPreference = findPreference("setting_returning_customer");
        assert returningPreference != null;
        returningPreference.setSummaryProvider(EditTextPreference.SimpleSummaryProvider.getInstance());
        returningPreference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));
        returningPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            try {
                Integer.parseInt((String) newValue);
                return true;
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Введите целое число", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void freeCupsSetting() {
        EditTextPreference freeCupPreference = findPreference("setting_free_cups");
        assert freeCupPreference != null;
        freeCupPreference.setSummaryProvider(EditTextPreference.SimpleSummaryProvider.getInstance());
        freeCupPreference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));
        freeCupPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            try {
                Integer.parseInt((String) newValue);
                return true;
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Введите целое число", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void deleteCustomerSetting() {
        Preference deleteCustomer = findPreference("settings_delete_customer");
        assert deleteCustomer != null;
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(res.getString(R.string.placeholder_extra_delete_customer), "true");
        deleteCustomer.setIntent(intent);
    }

}
