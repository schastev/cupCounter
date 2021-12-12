package com.example.cupcounter.toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cupcounter.R;

import java.util.Objects;

public class ToolbarHelper {
    public static void setUpToolbar(AppCompatActivity activity, int title) {
        Toolbar myToolbar = activity.findViewById(R.id.my_toolbar);
        activity.setSupportActionBar(myToolbar);
        Objects.requireNonNull(activity.getSupportActionBar()).setTitle(title);
    }
}
