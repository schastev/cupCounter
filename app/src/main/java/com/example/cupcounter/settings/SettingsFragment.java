package com.example.cupcounter.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.cupcounter.R;
import com.example.cupcounter.activity.MainActivity;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        EditTextPreference returningPreference = findPreference("setting_returning_customer");
        assert returningPreference != null;
        returningPreference.setSummaryProvider(EditTextPreference.SimpleSummaryProvider.getInstance());
            returningPreference.setOnBindEditTextListener(
                    editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));

        EditTextPreference freeCupPreference = findPreference("setting_free_cups");
        assert freeCupPreference != null;
        freeCupPreference.setSummaryProvider(EditTextPreference.SimpleSummaryProvider.getInstance());
        freeCupPreference.setOnBindEditTextListener(
                editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));

        Preference deleteCustomer = findPreference("settings_delete_customer");
        assert deleteCustomer != null;
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("DELETE_CUSTOMER", "true");
        deleteCustomer.setIntent(intent);
    }

}
