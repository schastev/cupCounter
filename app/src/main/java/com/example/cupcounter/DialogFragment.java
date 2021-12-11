package com.example.cupcounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.cupcounter.activity.DisplayCustomerInfoActivity;

public class DialogFragment extends androidx.fragment.app.DialogFragment {
    String newPhoneNumber;

    OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogPopUp = inflater.inflate(R.layout.dialog_phone_edit, null);
        builder.setTitle(R.string.info_dialog_phone_title)
                .setView(dialogPopUp)
                .setPositiveButton(R.string.info_dialog_button_save, (dialog, whichButton) -> {
                    EditText newPhoneField = dialogPopUp.findViewById(R.id.info_dialog_edit_phone_field);
                    newPhoneNumber = String.valueOf(newPhoneField.getText());
                    dataPasser.onDataPass(newPhoneNumber);
                    ((DisplayCustomerInfoActivity) getActivity()).updatePhone();
                })
                .setNegativeButton(R.string.info_dialog_button_cancel, null);
        return builder.create();
    }

    public interface OnDataPass {
        void onDataPass(String data);
    }

}