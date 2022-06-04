package com.tasty.count;


import static com.tasty.count.utils.VisibilityChecker.canClaimCup;
import static com.tasty.count.utils.VisibilityChecker.canDelete;
import static com.tasty.count.utils.VisibilityChecker.canRevertChanges;
import static com.tasty.count.utils.VisibilityChecker.isReturningClient;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RunWith(Enclosed.class)
public class VisibilityCheckerTest {

    @RunWith(Parameterized.class)
    public static class AlertVisibilityTest {
        LocalDate input;
        int expectedResult;
        int setting;
        @Rule
        public final MockitoRule rule = MockitoJUnit.rule();

        public AlertVisibilityTest(LocalDate input, int setting, int expectedResult) {
            this.input = input;
            this.expectedResult = expectedResult;
            this.setting = setting;
        }

        @Parameterized.Parameters
        public static List<Object> parameters() {
            return Arrays.asList(new Object[][]{
                    {LocalDate.now().plusDays(1), -1, 4},
                    {LocalDate.now().minusDays(1), -1, 0},
                    {LocalDate.now(), -1, 0},
                    {LocalDate.now().plusDays(1), 0, 4},
                    {LocalDate.now().minusDays(1), 0, 0},
                    {LocalDate.now(), 0, 4},
                    {LocalDate.now().plusDays(1), 1, 4},
                    {LocalDate.now().minusDays(1), 1, 4},
                    {LocalDate.now(), 1, 4},
            });
        }

        @Test
        @Parameterized.Parameters()
        public void alertVisibility() {
            assertThat(isReturningClient(input, setting)).isEqualTo(expectedResult);
        }
    }


    @RunWith(Parameterized.class)
    public static class ClaimButtonVisibilityTest {
        int input;
        int expectedResult;
        int setting;
        @Rule
        public final MockitoRule rule = MockitoJUnit.rule();

        public ClaimButtonVisibilityTest(int input, int setting, int expectedResult) {
            this.input = input;
            this.expectedResult = expectedResult;
            this.setting = setting;
        }

        @Parameterized.Parameters
        public static List<Object> parameters() {
            return Arrays.asList(new Object[][]{
                    {10, 5, 0}, {0, 5, 4}, {0, 5, 4},
                    {0, -1, 4}, {-5, -1, 0}, {-1, -1, 0}
            });
        }

        @Test
        @Parameterized.Parameters()
        public void claimButtonVisibility() {
            assertThat(canClaimCup(input, setting)).isEqualTo(expectedResult);
        }
    }

    @RunWith(Parameterized.class)
    public static class RevertChangesButtonVisibilityTest {
        int cups;
        int expectedResult;
        CharSequence currentValue;
        @Rule
        public final MockitoRule rule = MockitoJUnit.rule();

        public RevertChangesButtonVisibilityTest(int cups, String currentValue, int expectedResult) {
            this.cups = cups;
            this.expectedResult = expectedResult;
            this.currentValue = currentValue;
        }

        @Parameterized.Parameters
        public static List<Object> parameters() {
            return Arrays.asList(new Object[][]{
                    {0, "0", 4},
                    {2, "0", 0},
                    {2, "abd", 0},
                    {2, "", 0}
            });
        }

        @Test
        @Parameterized.Parameters()
        public void revertButtonVisibility() {
            assertThat(canRevertChanges(cups, currentValue)).isEqualTo(expectedResult);
        }
    }

    @RunWith(Parameterized.class)
    public static class DeleteButtonVisibilityTest {
        boolean permission;
        int expectedResult;
        @Rule
        public final MockitoRule rule = MockitoJUnit.rule();

        public DeleteButtonVisibilityTest(boolean permission, int expectedResult) {
            this.permission = permission;
            this.expectedResult = expectedResult;
        }

        @Parameterized.Parameters
        public static List<Object> parameters() {
            return Arrays.asList(new Object[][]{
                    {false, 4},
                    {true, 0}
            });
        }

        @Test
        @Parameterized.Parameters()
        public void deleteButtonVisibility() {
            assertThat(canDelete(permission)).isEqualTo(expectedResult);
        }
    }
}
