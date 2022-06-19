package com.tasty.count;

import static com.tasty.count.utils.Validator.validateIntSetting;
import static com.tasty.count.utils.Validator.validateName;
import static com.tasty.count.utils.Validator.validatePhone;
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
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.qameta.allure.kotlin.junit4.AllureParametrizedRunner;

@RunWith(Enclosed.class)
public class ValidatorTest {

    @RunWith(AllureParametrizedRunner.class)
    public static class PhoneNumber {
        String input;
        boolean expectedResult;
        @Rule
        public final MockitoRule rule = MockitoJUnit.rule();
        private Resources res;
        private TextInputLayout inputField;
        private CustomerDAO customerDAO;

        public PhoneNumber(String input, boolean expectedResult) {
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
        public static List<Object> parameters() {
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

    @RunWith(AllureParametrizedRunner.class)
    public static class Name {
        String input;
        boolean expectedResult;
        @Rule
        public final MockitoRule rule = MockitoJUnit.rule();
        private Resources res;
        private TextInputLayout inputField;

        public Name(String input, boolean expectedResult) {
            this.input = input;
            this.expectedResult = expectedResult;
        }

        @Before
        public void setUpMock() {
            res = mock(Resources.class);
            when(res.getString(R.string.placeholder_pattern_en)).thenReturn("[a-zA-Z]+");
            when(res.getString(R.string.placeholder_pattern_ru)).thenReturn("[а-яА-Я]+");
            inputField = mock(TextInputLayout.class);
        }

        @Parameterized.Parameters
        public static List<Object> parameters() {
            return Arrays.asList(new Object[][]{
                    {"Jack", true},
                    {"Абс", true},
                    {" ", false},
                    {"?", false},
                    {"", false},
                    {"123", false}
            });
        }

        @Test
        @Parameterized.Parameters()
        public void nameValidation() {
            assertThat(validateName(input, res, inputField)).isEqualTo(expectedResult);
        }
    }

    @RunWith(AllureParametrizedRunner.class)
    public static class Settings {
        String input;
        boolean expectedResult;
        @Rule
        public final MockitoRule rule = MockitoJUnit.rule();

        public Settings(String input, boolean expectedResult) {
            this.input = input;
            this.expectedResult = expectedResult;
        }

        @Parameterized.Parameters
        public static List<Object> parameters() {
            return Arrays.asList(new Object[][]{
                    {"1", true},
                    {"10", true},
                    {"123", true},
                    {"0", false},
                    {"-1", false},
                    {"?", false},
                    {"", false},
                    {" ", false},
                    {new Customer().toString(), false}
            });
        }

        @Test
        @Parameterized.Parameters()
        public void integerValidation() {
            assertThat(validateIntSetting(input)).isEqualTo(expectedResult);
        }
    }
}
