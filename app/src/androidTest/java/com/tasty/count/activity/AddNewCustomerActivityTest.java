package com.tasty.count.activity;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static com.tasty.count.CustomMatchers.hasTextInputLayoutErrorText;
import static com.tasty.count.CustomMatchers.isEditTextInLayout;

import android.content.res.Resources;
import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

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
    public ActivityScenarioRule<AddNewCustomerActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AddNewCustomerActivity.class);
    private Matcher<View> phoneInputContainer = withId(R.id.new_input_phone);
    private Matcher<View> nameInputContainer = withId(R.id.new_input_name);
    private Matcher<View> phoneInput = isEditTextInLayout(R.id.new_input_phone);
    private Matcher<View> nameInput = isEditTextInLayout(R.id.new_input_name);
    private Matcher<View> cupSwitch = withId(R.id.new_switch_add_cup);
    private Matcher<View> addButton = withId(R.id.new_button_add);
    private Resources res;

    @Before
    public void setup() {
        res = InstrumentationRegistry.getInstrumentation().getTargetContext().getResources();
    }

    @Test
    public void activityLoads() {
        onView(phoneInputContainer).check(matches(isDisplayed()));
        onView(nameInputContainer).check(matches(isDisplayed()));
        onView(addButton).check(matches(isDisplayed()));
        onView(cupSwitch).check(matches(isDisplayed()));
    }

    @Test
    public void happyPath() {
        onView(phoneInput).perform(typeText(RandomStringUtils.randomNumeric(10)), closeSoftKeyboard());
        onView(nameInput).perform(typeText(RandomStringUtils.randomAlphabetic(5)), closeSoftKeyboard());
        onView(addButton).perform(click());
        onView(withId(R.id.main_field_search)).check(matches(isDisplayed()));
    }

    @Test
    public void emptyFields() {
        Espresso.closeSoftKeyboard();
        onView(cupSwitch).perform(click());
        onView(addButton).perform(click());
        onView(phoneInputContainer).check(matches(hasTextInputLayoutErrorText(res.getString(R.string.new_toast_empty_field))));
        onView(nameInputContainer).check(matches(hasTextInputLayoutErrorText(res.getString(R.string.new_toast_empty_field))));
    }
}
