package com.tasty.count.activity;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.tasty.count.CustomMatchers.atPosition;
import static org.hamcrest.CoreMatchers.instanceOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.material.textfield.TextInputEditText;
import com.tasty.count.R;
import com.tasty.count.database.Customer;
import com.tasty.count.test.BaseTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Collectors;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends BaseTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        for (int i = 0; i < 10; i++) {
            Customer customer = new Customer();
            int id = (int) customerDAO.insert(customer);
            customer.setId(id);
        }
    }

    @Test
    public void mainActivityLoads() {
        checkVisibility(withId(R.id.my_toolbar));
        checkVisibility(withId(R.id.main_field_search));
    }

    @Test
    public void searchNumber() {
        int random = (int) ((Math.random() * 9));
        String search = String.valueOf(random);
        onView(instanceOf(TextInputEditText.class)).perform(typeText(search)).perform(pressImeActionButton());
        List<Customer> actual = customerDAO.findByShortNumber("%" + search)
                .stream()
                .sorted()
                .collect(Collectors.toList());
        int resultSize = actual.size();
        if (resultSize == 0) {
            checkErrorMessage(withId(R.id.main_field_search), res.getString(R.string.main_no_result));
        } else if (resultSize == 1) {
            Customer customer = actual.get(0);
            onView(withId(R.id.info_field_container_phone)).check(matches(withText(customer.getPhoneNumber())));
        } else {
            for (int i = 0; i < resultSize; i++) {
                Customer customer = actual.get(i);
                onView(withId(R.id.main_list_results))
                        .check(matches(atPosition(i, hasDescendant(withText(customer.getName())))));
                onView(withId(R.id.main_list_results))
                        .check(matches(atPosition(i, hasDescendant(withText(customer.getPhoneNumber())))));
            }
        }
    }
}
