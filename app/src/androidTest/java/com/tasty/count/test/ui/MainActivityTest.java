package com.tasty.count.test.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.material.textfield.TextInputEditText;
import com.tasty.count.R;
import com.tasty.count.database.Customer;
import com.tasty.count.page.MainPage;
import com.tasty.count.test.BaseTest;
import com.tasty.count.utils.CustomerGenerator;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends BaseTest {
    MainPage mainPage = new MainPage();

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void mainActivityLoads() {
        checkVisibility(mainPage.toolbar);
        checkVisibility(mainPage.searchBar);
        assertToolbarName(mainPage.toolbarText, R.string.main_title);
    }

    /**
     * Sets up a specified number of customers, whose numbers end with the same digits
     *
     * @param search the digits at the end of the phone numbers that must be the same
     * @param number the number of customers to set up
     * @return list of set-up customers
     */
    private List<Customer> setupCustomersWithSimilarNumbers(String search, int number) {
        List<Customer> customers = new ArrayList<>();
        if (number == 0) {
            List<Customer> presentCustomers = customerDAO.findByShortNumber("%" + search);
            for (Customer c : presentCustomers) {
                customerDAO.delete(c);
            }
        } else {
            for (int i = 0; i < number; i++) {
                Customer c = new Customer().setPhoneNumber(RandomStringUtils.randomNumeric(5) + search);
                customers.add(c);
                customerDAO.insert(c);
            }
        }
        return customers.stream().sorted().collect(Collectors.toList());
    }

    /**
     * A convenience method for getting sorted search results from database
     */
    private List<Customer> findSortedCustomers(String search) {
        return customerDAO.findByShortNumber("%" + search)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Test
    public void searchNumberNoResults() {
        int random = (int) ((Math.random() * 9));
        setupCustomersWithSimilarNumbers(String.valueOf(random), 0);
        String search = String.valueOf(random);
        onView(instanceOf(TextInputEditText.class)).perform(typeText(search)).perform(pressImeActionButton());
        List<Customer> actual = findSortedCustomers(search);
        assertThat(actual.size(), equalTo((0)));
        assertNotDisplayed(mainPage.results);
        checkErrorMessage(mainPage.searchBar, R.string.main_no_result);
    }

    @Test
    @Ignore("figure out how to match fragments and their contents to make this one pass")
    public void searchNumberOneResult() {
        String number = CustomerGenerator.randomNumber();
        String search = number.substring(number.length() - 5);
        List<Customer> expected = setupCustomersWithSimilarNumbers(search, 1);
        onView(instanceOf(TextInputEditText.class)).perform(typeText(search)).perform(pressImeActionButton());
        List<Customer> actual = findSortedCustomers(search);
        assertThat(actual, equalTo(expected));
        assertThat(actual.size(), equalTo(1));
        Customer customer = actual.get(0);
        onView(withId(R.id.info_field_container_phone)).check(matches(withText(customer.getPhoneNumber())));
    }

    @Test
    public void searchNumberMultipleResults() {
        String number = CustomerGenerator.randomNumber();
        String search = number.substring(number.length() - 5);
        List<Customer> expected = setupCustomersWithSimilarNumbers(search, 3);
        onView(instanceOf(TextInputEditText.class)).perform(typeText(search)).perform(pressImeActionButton());
        List<Customer> actual = findSortedCustomers(search);
        assertThat(actual, equalTo(expected));
        assertThatUiListIsEqualToDatabase(actual, mainPage.results);
    }
}
