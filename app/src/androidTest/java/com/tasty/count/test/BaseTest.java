package com.tasty.count.test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static com.tasty.count.CustomMatchers.hasTextInputLayoutErrorText;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.platform.app.InstrumentationRegistry;

import com.tasty.count.database.AppDatabase;
import com.tasty.count.database.CustomerDAO;
import com.tasty.count.database.DBClient;

import org.hamcrest.Matcher;
import org.junit.Before;

public class BaseTest {
    public Resources res;
    public CustomerDAO customerDAO;

    @Before
    public void setup() {
        res = InstrumentationRegistry.getInstrumentation().getTargetContext().getResources();
        Context context = ApplicationProvider.getApplicationContext();
        AppDatabase db = DBClient.getInstance(context).getAppDatabase();
        customerDAO = db.customerDao();
    }

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
