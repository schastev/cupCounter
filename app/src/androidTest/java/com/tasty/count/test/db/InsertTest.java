package com.tasty.count.test.db;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tasty.count.database.Customer;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class InsertTest extends BaseTest{
    @Test
    public void insertTest() {
        Customer expected = new Customer();
        String numberEnding = expected.getPhoneNumber();
        numberEnding = "%" + numberEnding.substring(numberEnding.length() - 4);
        customerDAO.insert(expected);
        List<Customer> byName = customerDAO.findByShortNumber(numberEnding);
        assertEquals(byName.get(0), expected);
    }
}
