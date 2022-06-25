package com.tasty.count.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tasty.count.database.Customer;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SelectTest extends BaseTest {

    @Test
    public void getAllTest() {
        assertEquals(customerDAO.getAll(), allCustomers);
    }

    @Test
    public void getById() {
        int random = (int) Math.floor(Math.random() * (allCustomers.size()));
        Customer expected = allCustomers.get(random);
        Customer actual = customerDAO.getById(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    public void getByNonExistentId() {
        Customer actual = customerDAO.getById(100);
        assertNull(actual);
    }

    @Test
    public void findByShortNumberTest() {
        int random = (int) Math.floor(Math.random() * (allCustomers.size()));
        Customer expected = allCustomers.get(random);
        String phoneNumber = expected.getPhoneNumber();
        List<Customer> actual = customerDAO.findByShortNumber("%" + phoneNumber.substring(phoneNumber.length() - 4));
        assertTrue(actual.contains(expected));
    }

    @Test
    public void findByFullNumberTest() {
        int random = (int) Math.floor(Math.random() * (allCustomers.size()));
        Customer expected = allCustomers.get(random);
        List<Customer> actual = customerDAO.findByShortNumber(expected.getPhoneNumber());
        assertTrue(actual.contains(expected));
    }

    @Test
    public void findByAlphaTest() {
        int random = (int) Math.floor(Math.random() * (allCustomers.size()));
        Customer expected = allCustomers.get(random);
        List<Customer> actual = customerDAO.findByShortNumber("%" + expected.getName());
        assertTrue(actual.isEmpty());
        actual = customerDAO.findByFullNumber(expected.getName());
        assertTrue(actual.isEmpty());
    }

}
