package com.tasty.count.page;

import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.endsWith;

import android.view.View;

import com.tasty.count.R;

import org.hamcrest.Matcher;

public class BasePage {
    public final Matcher<View> toolbar = withId(R.id.my_toolbar);
    public final Matcher<View> toolbarText = allOf(isDescendantOfA(toolbar),
            withClassName(endsWith("TextView")));
}
