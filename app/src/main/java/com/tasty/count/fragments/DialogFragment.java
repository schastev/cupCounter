package com.tasty.count.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tasty.count.R;
import com.tasty.count.activity.DisplayCustomerInfoActivity;

import java.util.Objects;

public class DialogFragment extends androidx.fragment.app.DialogFragment {
    private String newPhoneNumber;
    private OnDataPass dataPasser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Resources res = getResources();
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogPopUp = inflater.inflate(R.layout.dialog_phone_edit, null);
        showSoftKeyboard(dialogPopUp);
        EditText newPhoneField = dialogPopUp.findViewById(R.id.info_dialog_edit_phone_field);
        newPhoneField.setHint(res.getString(R.string.info_hint_update_phone));
        newPhoneField.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setTitle(R.string.info_dialog_phone_title)
                .setView(dialogPopUp)
                .setPositiveButton(R.string.info_dialog_button_save, (dialog, whichButton) -> {
                    newPhoneNumber = String.valueOf(newPhoneField.getText());
                    if (!newPhoneNumber.matches(res.getString(R.string.placeholder_pattern_digits))) {
                        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                                res.getString(R.string.new_toast_phone_error),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        dataPasser.onDataPass(newPhoneNumber);
                        ((DisplayCustomerInfoActivity) Objects.requireNonNull(getActivity())).updatePhone();
                    }
                })
                .setNegativeButton(R.string.info_dialog_button_cancel, null);
        return builder.create();
    }

    public interface OnDataPass {
        void onDataPass(String data);
    }

    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

}