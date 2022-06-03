package com.tasty.count;


import static com.tasty.count.VisibilityChecker.isReturningClient;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import java.time.LocalDate;

public class VisibilityCheckerTest {
    @Test
    public void check() {
        int visibility = isReturningClient(LocalDate.now().minusDays(1), 10);
        assertThat(visibility).isEqualTo(4);
    }
}
