package com.tasty.count.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.cupcounter.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class InfoDisplayFragment extends Fragment {

    private static final String FIELD_NAME = "FIELD_NAME";
    private static final String FIELD_VALUE = "FIELD_VALUE";
    private String fieldName;
    private String fieldValue;

    public InfoDisplayFragment() {

    }

    public static InfoDisplayFragment newInstance(String fieldName, String fieldValue) {
        InfoDisplayFragment fragment = new InfoDisplayFragment();
        Bundle args = new Bundle();
        args.putString(FIELD_NAME, fieldName);
        args.putString(FIELD_VALUE, fieldValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            fieldName = args.getString(FIELD_NAME);
            fieldValue = args.getString(FIELD_VALUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.info_pair, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        TextInputLayout layout = view.findViewById(R.id.info_pair);
        layout.setHint(fieldName);
        Objects.requireNonNull(layout.getEditText()).setText(fieldValue);
    }
}