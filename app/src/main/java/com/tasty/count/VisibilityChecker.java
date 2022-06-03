package com.tasty.count;

import android.view.View;

import java.time.LocalDate;

public class VisibilityChecker {
    public static int isReturningClient(LocalDate lastVisit, int returningCustomerSetting) {
        int visibility;
        if (lastVisit.isBefore(LocalDate.now().minusDays(returningCustomerSetting))) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.INVISIBLE;
        }
        return visibility;
    }

    public static int canClaimCup(int newCups, int freeCupSetting) {
        int visibility;
        if (newCups / freeCupSetting >= 1) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.INVISIBLE;
        }
        return visibility;
    }

    public static int canRevertChanges(int cups, CharSequence currentValue) {
        int visibility;
        if (currentValue.equals(String.valueOf(cups))) {
            visibility = View.INVISIBLE;
        } else {
            visibility = View.VISIBLE;
        }
        return visibility;
    }

    public static int canDelete(boolean adminPermissions) {
        int visibility;
        if (adminPermissions) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.INVISIBLE;
        }
        return visibility;
    }
}
