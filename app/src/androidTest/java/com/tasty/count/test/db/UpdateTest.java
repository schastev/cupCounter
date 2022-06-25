package com.tasty.count.test.db;

import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tasty.count.database.Customer;
import com.tasty.count.utils.CustomerGenerator;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class UpdateTest extends BaseTest {

    @Test
    public void updateCupsTest() {
        int random = (int) Math.floor(Math.random() * (allCustomers.size()));
        Customer expected = allCustomers.get(random).setCups(4);
        customerDAO.update(expected);
        String phoneNumber = expected.getPhoneNumber();
        List<Customer> actual = new ArrayList<>(customerDAO.findByShortNumber("%" + phoneNumber.substring(phoneNumber.length() - 4)));
        assertTrue(actual.contains(expected));
    }

    @Test
    public void updateNumberTest() {
        int random = (int) Math.floor(Math.random() * (allCustomers.size()));
        Customer expected = allCustomers.get(random);
        String oldNumber = expected.getPhoneNumber();
        String newNumber = CustomerGenerator.randomNumber();
        expected.setPhoneNumber(newNumber);

        customerDAO.update(expected);
        List<Customer> actual = new ArrayList<>(customerDAO.findByShortNumber("%" + newNumber.substring(newNumber.length() - 4)));
        List<Customer> searchOld = new ArrayList<>(customerDAO.findByShortNumber("%" + oldNumber.substring(oldNumber.length() - 4)));
        assertTrue(actual.contains(expected));
        assertTrue(searchOld.isEmpty());
    }
}
