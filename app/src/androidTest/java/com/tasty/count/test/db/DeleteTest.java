package com.tasty.count.test.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tasty.count.database.Customer;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DeleteTest extends BaseTest {

    @Test
    public void deleteTest() {
        int random = (int) Math.floor(Math.random() * (allCustomers.size()));
        Customer expected = allCustomers.get(random);
        customerDAO.delete(expected);
        List<Customer> actual = customerDAO.getAll();
        Customer notFound = customerDAO.getById(expected.getId());
        assertNull(notFound);
        assertFalse(actual.contains(expected));
    }

    @Test
    public void deleteNonExistentItemTest() {
        Customer expected = new Customer();
        customerDAO.delete(expected);
        List<Customer> actual = customerDAO.getAll();
        assertEquals(actual, allCustomers);
    }
}
