package com.tasty.count;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.android.material.textfield.TextInputLayout;

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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogPopUp = inflater.inflate(R.layout.settings_password, null);
        showSoftKeyboard(dialogPopUp);
        TextInputLayout currentPasswordField = dialogPopUp.findViewById(R.id.settings_password_current);
        TextInputLayout newPasswordField = dialogPopUp.findViewById(R.id.settings_password_new);
        TextInputLayout confirmationField = dialogPopUp.findViewById(R.id.settings_password_confirm);
        builder.setTitle("Обновить пароль")
                .setView(dialogPopUp)
                .setPositiveButton(R.string.info_dialog_button_save, (dialog, whichButton) -> {
                    currentPassword = String.valueOf(Objects.requireNonNull(currentPasswordField.getEditText()).getText());
                    if (!currentPassword.equals(sharedPreferences.getString(res.getString(R.string.placeholder_setting_admin_password), "coffee"))) {
                        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                                "Неверный текущий пароль",
                                Toast.LENGTH_SHORT).show();
                    }
                    String newPassword = Objects.requireNonNull(newPasswordField.getEditText()).getText().toString();
                    String confirmation = Objects.requireNonNull(confirmationField.getEditText()).getText().toString();
                    if (!newPassword.equals(confirmation)) {
                        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                                "Новый пароль и подтверждение не совпадают",
                                Toast.LENGTH_SHORT).show();
                    }
                    if (validatePassword(newPassword)) {
                        Bundle result = new Bundle();
                        result.putString("newPassword", newPassword);
                        getParentFragmentManager().setFragmentResult("requestKey", result);
                    }
                })
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

    private boolean validatePassword(String newValue) {
        if (newValue.matches(res.getString(R.string.placeholder_pattern_spaces)) || newValue.equals("")) {
            Toast.makeText(getContext(), res.getString(R.string.settings_toast_empty_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (newValue.length() < 3) {
            Toast.makeText(getContext(), res.getString(R.string.settings_toast_short_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
