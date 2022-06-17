package com.tasty.count.activity;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.tasty.count.R;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddNewCustomerActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);
    private Matcher<View> phoneInput = withId(R.id.new_input_phone);
    private Matcher<View> nameInput = withId(R.id.new_input_name);
    private Matcher<View> cupSwitch = withId(R.id.new_switch_add_cup);
    private Matcher<View> addButton = withId(R.id.new_button_add);

    @Before
    public void openActivity() {
        onView(withId(R.id.toolbar_button_add)).perform(click());
    }

    @Test
    public void activityOpensViaToolbarButtonClick() {
        onView(phoneInput).check(matches(isDisplayed()));
        onView(nameInput).check(matches(isDisplayed()));
        onView(cupSwitch).check(matches(isDisplayed()));
    }

    @Test
    public void happyPath() {
        onView(nameInput).perform(typeText(RandomStringUtils.randomAlphabetic(5)));
        onView(phoneInput).perform(typeText(RandomStringUtils.randomNumeric(10)));
        onView(addButton).perform(click());
        onView(withId(R.id.main_field_search)).check(matches(isDisplayed()));
    }
}
