package com.tasty.count.activity;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.material.textfield.TextInputEditText;
import com.tasty.count.R;
import com.tasty.count.database.AppDatabase;
import com.tasty.count.database.Customer;
import com.tasty.count.database.CustomerDAO;
import com.tasty.count.database.DBClient;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private CustomerDAO customerDAO;

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void databaseSetup() {
        Context context = ApplicationProvider.getApplicationContext();
        AppDatabase db = DBClient.getInstance(context).getAppDatabase();
        customerDAO = db.customerDao();
    }

    @Test
    public void mainActivityLoads() {
        onView(withId(R.id.my_toolbar))
                .check(matches(isDisplayed()));
    }

    @Test
    public void searchNumber() {
        int random = (int) ((Math.random() * 9));
        String search = String.valueOf(random);
        onView(instanceOf(TextInputEditText.class)).perform(typeText(search)).perform(pressImeActionButton());
        List<Customer> actual = customerDAO.findByShortNumber("%" + search);
        int resultSize = actual.size();
        for (int i = 0; i < resultSize; i++) {
            onData(withId(R.id.main_list_results))
                    .atPosition(i)
                    .check(matches(hasDescendant(withText(actual.get(i).getPhoneNumber()))));

            onView(withId(R.id.main_list_results))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        }
    }
}
