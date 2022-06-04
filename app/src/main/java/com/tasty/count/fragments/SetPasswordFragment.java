package com.tasty.count.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.android.material.textfield.TextInputLayout;
import com.tasty.count.R;

import java.util.Objects;

public class SetPasswordFragment extends androidx.fragment.app.DialogFragment {
    private String currentPassword;
    Resources res;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        res = getResources();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogPopUp = inflater.inflate(R.layout.settings_password, null);
        showSoftKeyboard(dialogPopUp);
        TextInputLayout currentPasswordField = dialogPopUp.findViewById(R.id.settings_password_current);
        TextInputLayout newPasswordField = dialogPopUp.findViewById(R.id.settings_password_new);
        TextInputLayout confirmationField = dialogPopUp.findViewById(R.id.settings_password_confirm);
        builder.setTitle(res.getString(R.string.settings_dialog_title_update_password))
                .setView(dialogPopUp)
                .setPositiveButton(R.string.info_dialog_button_save, (dialog, whichButton) -> {
                    String toastTest;
                    if (currentPasswordField.getError() == null
                            && newPasswordField.getError() == null
                            && confirmationField.getError() == null) {
                        String newPassword = Objects.requireNonNull(newPasswordField.getEditText()).getText().toString();
                        Bundle result = new Bundle();
                        result.putString(res.getString(R.string.placeholder_new_password_bundle), newPassword);
                        getParentFragmentManager().setFragmentResult("requestKey", result);
                        toastTest = res.getString(R.string.settings_toast_password_saved);
                    } else {
                        toastTest = res.getString(R.string.settings_toast_password_not_saved);
                    }
                    Toast.makeText(getContext(), toastTest, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.info_dialog_button_cancel, null);
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(currentPasswordField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                currentPassword = String.valueOf(Objects.requireNonNull(currentPasswordField.getEditText()).getText());
                if (!currentPassword.equals(sharedPreferences.getString(res.getString(R.string.placeholder_setting_admin_password), "coffee"))) {
                    currentPasswordField.setError(res.getString(R.string.settings_dialog_error_wrong_current_password));
                    newPasswordField.setEnabled(false);
                    confirmationField.setEnabled(false);
                } else {
                    currentPasswordField.setError(null);
                    newPasswordField.setEnabled(true);
                    confirmationField.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Objects.requireNonNull(newPasswordField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newPassword = Objects.requireNonNull(newPasswordField.getEditText()).getText().toString();
                validatePassword(newPassword, newPasswordField);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Objects.requireNonNull(confirmationField.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newPassword = Objects.requireNonNull(newPasswordField.getEditText()).getText().toString();
                String confirmation = Objects.requireNonNull(confirmationField.getEditText()).getText().toString();
                if (!newPassword.equals(confirmation)) {
                    newPasswordField.setError(res.getString(R.string.settings_dialog_error_mismatch));
                    confirmationField.setError(res.getString(R.string.settings_dialog_error_mismatch));
                } else {
                    newPasswordField.setError(null);
                    confirmationField.setError(null);
                    if (validatePassword(newPassword, newPasswordField)) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        dialog.setOnShowListener(dialog1 -> ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false));
        return dialog;
    }

    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private boolean validatePassword(String newValue, TextInputLayout field) {
        if (newValue.matches(res.getString(R.string.placeholder_pattern_spaces)) || newValue.equals("")) {
            field.setError(res.getString(R.string.settings_toast_empty_password));
            return false;
        }
        if (newValue.length() < 3) {
            field.setError(res.getString(R.string.settings_toast_short_password));
            return false;
        }
        field.setError(null);
        return true;
    }
}
