package com.tasty.count.page;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.tasty.count.CustomMatchers.isEditTextInLayout;

import android.view.View;

import com.tasty.count.R;

import org.hamcrest.Matcher;

public class NewCustomerPage extends BasePage {
    public final Matcher<View> phoneInputContainer = withId(R.id.new_input_phone);
    public final Matcher<View> nameInputContainer = withId(R.id.new_input_name);
    public final Matcher<View> phoneInput = isEditTextInLayout(R.id.new_input_phone);
    public final Matcher<View> nameInput = isEditTextInLayout(R.id.new_input_name);
    public final Matcher<View> cupSwitch = withId(R.id.new_switch_add_cup);
    public final Matcher<View> addButton = withId(R.id.new_button_add);
}
