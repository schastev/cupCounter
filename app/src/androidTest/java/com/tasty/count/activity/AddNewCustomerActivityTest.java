package com.tasty.count.activity;


import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.tasty.count.CustomMatchers.isEditTextInLayout;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.tasty.count.R;
import com.tasty.count.test.BaseTest;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddNewCustomerActivityTest extends BaseTest {

    @Rule
    public ActivityScenarioRule<AddNewCustomerActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AddNewCustomerActivity.class);
    private Matcher<View> phoneInputContainer = withId(R.id.new_input_phone);
    private Matcher<View> nameInputContainer = withId(R.id.new_input_name);
    private Matcher<View> phoneInput = isEditTextInLayout(R.id.new_input_phone);
    private Matcher<View> nameInput = isEditTextInLayout(R.id.new_input_name);
    private Matcher<View> cupSwitch = withId(R.id.new_switch_add_cup);
    private Matcher<View> addButton = withId(R.id.new_button_add);

    @Test
    public void activityLoads() {
        checkVisibility(phoneInputContainer);
        checkVisibility(nameInputContainer);
        checkVisibility(addButton);
        checkVisibility(cupSwitch);
    }

    @Test
    public void happyPath() {
        inputText(phoneInput, RandomStringUtils.randomNumeric(10));
        inputText(nameInput, RandomStringUtils.randomAlphabetic(5));
        clickButton(addButton);
        checkVisibility(withId(R.id.main_field_search));
    }

    @Test
    public void emptyFields() {
        hideKeyboard();
        clickButton(addButton);
        String errorMessage = res.getString(R.string.new_toast_empty_field);
        checkErrorMessage(phoneInputContainer, errorMessage);
        checkErrorMessage(nameInputContainer, errorMessage);
    }
}
