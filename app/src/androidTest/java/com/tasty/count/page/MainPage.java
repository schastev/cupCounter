package com.tasty.count.page;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;

import com.tasty.count.R;

import org.hamcrest.Matcher;

public class MainPage extends BasePage{
    public final Matcher<View> searchBar = withId(R.id.main_field_search);
    public final Matcher<View> results = withId(R.id.main_list_results);
}
