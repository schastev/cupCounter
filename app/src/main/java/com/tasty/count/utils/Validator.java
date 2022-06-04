package com.tasty.count.utils;

import android.content.res.Resources;

import com.google.android.material.textfield.TextInputLayout;
import com.tasty.count.R;
import com.tasty.count.database.Customer;
import com.tasty.count.database.CustomerDAO;

import java.util.List;

public class Validator {

    public static boolean validatePhone(String phoneNumber, Resources res, TextInputLayout phoneField, CustomerDAO customerDAO) {
        if (checkForRepeatedNumbers(phoneNumber, customerDAO)) {
            phoneField.setError(res.getString(R.string.new_toast_already_registered));
            return false;
        } else if (phoneNumber.equals("")) {
            phoneField.setError(res.getText(R.string.new_toast_empty_field));
            return false;
        } else if (!phoneNumber.matches(res.getString(R.string.placeholder_pattern_digits))) {
            phoneField.setError(res.getString(R.string.new_toast_phone_error));
            return false;
        }
        return true;
    }

    public static boolean validateName(String name, Resources res, TextInputLayout nameField) {
        if (name.equals("")) {
            nameField.setError(res.getText(R.string.new_toast_empty_field));
            return false;
        }
        if (!(name.matches(res.getString(R.string.placeholder_pattern_en))
                || name.matches(res.getString(R.string.placeholder_pattern_ru)))) {
            nameField.setError(res.getString(R.string.new_toast_name_error));
            return false;
        }
        return true;
    }


    /**
     * Checks if the number is already registered in the database
     *
     * @param phone number we need to check
     * @return true if already registered, false otherwise
     */
    public static boolean checkForRepeatedNumbers(String phone, CustomerDAO customerDAO) {
        List<Customer> customers = customerDAO.findByFullNumber(phone);
        return !customers.isEmpty();
    }

    public static boolean validateIntSetting(String newValue) {
        try {
            int number = Integer.parseInt(newValue);
            return number >= 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
