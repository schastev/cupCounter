package com.tasty.count;


import static com.tasty.count.Validator.validatePhone;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.res.Resources;

import com.google.android.material.textfield.TextInputLayout;
import com.tasty.count.database.Customer;
import com.tasty.count.database.CustomerDAO;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class PhoneValidatorTest {
    String input;
    boolean expectedResult;
    @Rule
    public final MockitoRule rule = MockitoJUnit.rule();
    private Resources res;
    private TextInputLayout inputField;
    private CustomerDAO customerDAO;

    public PhoneValidatorTest(String input, boolean expectedResult) {
        this.input = input;
        this.expectedResult = expectedResult;
    }

    @Before
    public void setUpMock() {
        res = mock(Resources.class);
        when(res.getString(R.string.placeholder_pattern_digits)).thenReturn("[0-9]+");
        inputField = mock(TextInputLayout.class);
        customerDAO = mock(CustomerDAO.class);
        when(customerDAO.findByFullNumber(anyString())).thenReturn(new ArrayList<>());
    }

    @Parameterized.Parameters
    public static List<Object> happyPhoneParameters() {
        return Arrays.asList(new Object[][]{
                {"1234", true},
                {"1111111111111111111", true},
                {" ", false},
                {"?", false},
                {"", false},
                {"asa", false}
        });
    }

    @Test
    @Parameterized.Parameters()
    public void phoneValidation() {
        assertThat(validatePhone(input, res, inputField, customerDAO)).isEqualTo(expectedResult);
    }
}
