package com.tasty.count.test.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.tasty.count.CustomMatchers.atPosition;
import static com.tasty.count.CustomMatchers.hasTextInputLayoutErrorText;
import static org.hamcrest.CoreMatchers.not;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.platform.app.InstrumentationRegistry;

import com.tasty.count.database.AppDatabase;
import com.tasty.count.database.Customer;
import com.tasty.count.database.CustomerDAO;
import com.tasty.count.database.DBClient;

import org.hamcrest.Matcher;
import org.junit.Before;

import java.util.List;

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

    public void checkErrorMessage(Matcher<View> view, int error) {
        onView(view).check(matches(hasTextInputLayoutErrorText(res.getString(error))));
    }

    public static void hideKeyboard() {
        Espresso.closeSoftKeyboard();
    }

    public static void assertThatUiListIsEqualToDatabase(List<Customer> actual, Matcher<View> view) {
        int resultSize = actual.size();
        for (int i = 0; i < resultSize; i++) {
            Customer customer = actual.get(i);
            onView(view).check(matches(atPosition(i, hasDescendant(withText(customer.getName())))));
            onView(view).check(matches(atPosition(i, hasDescendant(withText(customer.getPhoneNumber())))));
        }
    }

    public static void assertNotDisplayed(Matcher<View> view) {
        onView(view).check(matches(not(isDisplayed())));
    }

    public void assertToolbarName(Matcher<View> view, int titleId) {
        onView(view).check(matches(withText(res.getString(titleId))));
    }
}
