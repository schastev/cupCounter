package com.tasty.count.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.tasty.count.R;
import com.tasty.count.activity.SettingsActivity;

import java.util.Objects;

public class PasswordPromptFragment extends androidx.fragment.app.DialogFragment {

    private Resources res;
    private SharedPreferences sharedPreferences;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setUpAdditionalResources();
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogPopUp = inflater.inflate(R.layout.dialog_phone_edit, null);
        showSoftKeyboard(dialogPopUp);
        EditText passwordField = dialogPopUp.findViewById(R.id.info_dialog_edit_phone_field);
        passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());

        builder.setTitle(res.getString(R.string.toolbar_dialog_title))
                .setView(dialogPopUp)
                .setPositiveButton(res.getText(R.string.toolbar_dialog_ok), (dialog, whichButton) -> checkPassword(passwordField))
                .setNegativeButton(R.string.info_dialog_button_cancel, null);
        return builder.create();
    }

    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void setUpAdditionalResources() {
        res = getResources();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getActivity()));
    }

    private void checkPassword(EditText passwordField) {
        String password = String.valueOf(passwordField.getText());
        if (!password.equals(sharedPreferences.getString(res.getString(R.string.settings_line_admin_password), "coffee"))) {
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                    res.getString(R.string.toolbar_toast_wrong_pw),
                    Toast.LENGTH_SHORT).show();
        } else {
            Activity activity = Objects.requireNonNull(getActivity());
            Intent intent = new Intent(activity, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
        }
    }
}
