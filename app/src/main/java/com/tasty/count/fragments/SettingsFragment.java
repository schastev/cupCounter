package com.tasty.count.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.tasty.count.R;
import com.tasty.count.utils.Validator;
import com.tasty.count.activity.MainActivity;


public class SettingsFragment extends PreferenceFragmentCompat {
    Resources res;
    Toast toast;
    String newAdminPassword;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setUpAdditionalResources();
        setPreferencesFromResource(R.xml.preferences, rootKey);
        returningCustomerSetting();
        freeCupsSetting();
        deleteCustomerSetting();
        adminPassword();

        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            newAdminPassword = bundle.getString(res.getString(R.string.placeholder_new_password_bundle));
            setNewPassword();
        });
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

    private void adminPassword() {
        Preference adminPassword = findPreference(res.getString(R.string.placeholder_setting_admin_password));
        assert adminPassword != null;
        adminPassword.setOnPreferenceClickListener(preference -> {
            SetPasswordFragment setPasswordFragment = new SetPasswordFragment();
            setPasswordFragment.show(getParentFragmentManager(), res.getString(R.string.placeholder_update_password_fragment_tag));
            return true;
        });
    }

    public void setNewPassword() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(res.getString(R.string.placeholder_setting_admin_password), newAdminPassword);
        editor.apply();
    }

    private void deleteCustomerSetting() {
        Preference deleteCustomer = findPreference(res.getString(R.string.placeholder_setting_delete));
        assert deleteCustomer != null;
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(res.getString(R.string.placeholder_extra_delete_customer), "true");
        deleteCustomer.setIntent(intent);
    }

    private boolean validateIntSetting(String newValue) {
        boolean valid = Validator.validateIntSetting(newValue);
        if (!valid) {
            toast.show();
        }
        return valid;
    }
}
