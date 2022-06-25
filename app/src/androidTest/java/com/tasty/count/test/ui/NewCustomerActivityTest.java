package com.tasty.count.test.ui;


import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.tasty.count.R;
import com.tasty.count.activity.AddNewCustomerActivity;
import com.tasty.count.page.NewCustomerPage;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NewCustomerActivityTest extends BaseTest {
    NewCustomerPage newCustomerPage = new NewCustomerPage();

    @Rule
    public ActivityScenarioRule<AddNewCustomerActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AddNewCustomerActivity.class);

    @Test
    public void activityLoads() {
        checkVisibility(newCustomerPage.phoneInputContainer);
        checkVisibility(newCustomerPage.nameInputContainer);
        checkVisibility(newCustomerPage.addButton);
        checkVisibility(newCustomerPage.cupSwitch);
        assertToolbarName(newCustomerPage.toolbarText, R.string.main_button_add_customer);
    }

    @Test
    public void happyPath() {
        inputText(newCustomerPage.phoneInput, RandomStringUtils.randomNumeric(10));
        inputText(newCustomerPage.nameInput, RandomStringUtils.randomAlphabetic(5));
        clickButton(newCustomerPage.addButton);
        checkVisibility(withId(R.id.main_field_search));
    }

    @Test
    public void emptyFields() {
        hideKeyboard();
        clickButton(newCustomerPage.addButton);
        int errorMessage = R.string.new_toast_empty_field;
        checkErrorMessage(newCustomerPage.phoneInputContainer, errorMessage);
        checkErrorMessage(newCustomerPage.nameInputContainer, errorMessage);
    }
}
