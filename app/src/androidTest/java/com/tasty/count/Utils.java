package com.tasty.count;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import static com.tasty.count.CustomMatchers.hasTextInputLayoutErrorText;

import android.view.View;

import androidx.test.espresso.Espresso;

import org.hamcrest.Matcher;

public class Utils {
    public static void inputText(Matcher<View> view, String text) {
        onView(view).perform(typeText(text), closeSoftKeyboard());
    }

    public static void clickButton(Matcher<View> view) {
        onView(view).perform(click());
    }

    public static void checkVisibility(Matcher<View> view) {
        onView(view).check(matches(isDisplayed()));
    }

    public static void checkErrorMessage(Matcher<View> view, String error) {
        onView(view).check(matches(hasTextInputLayoutErrorText(error)));
    }
    public static void hideKeyboard() {
        Espresso.closeSoftKeyboard();
    }
}
