package com.example.cupcounter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cupcounter.database.AppDatabase;
import com.example.cupcounter.database.Customer;
import com.example.cupcounter.database.CustomerDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private CustomerDAO customerDAO;
    private AppDatabase db;
    List <Customer> allCustomers = new ArrayList<>();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .build();
        customerDAO = db.customerDao();
        for (int i = 0; i < 10; i++) {
            Customer customer = new Customer();
            int id = (int) customerDAO.insert(customer);
            customer.setId(id);
            allCustomers.add(customer);
        }
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertTest() {
        Customer expected = new Customer();
        String numberEnding = expected.getPhoneNumber();
        numberEnding = "%" + numberEnding.substring(numberEnding.length() - 4);
        customerDAO.insert(expected);
        List<Customer> byName = customerDAO.findByShortNumber(numberEnding);
        assertEquals(byName.get(0), expected);
    }

    @Test
    public void getAllTest() {
        assertEquals(customerDAO.getAll(), allCustomers);
    }

    @Test
    public void getById() {
        int random = (int) Math.floor(Math.random()*(allCustomers.size()));
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
        int random = (int) Math.floor(Math.random()*(allCustomers.size()));
        Customer expected = allCustomers.get(random);
        String phoneNumber = expected.getPhoneNumber();
        List<Customer> actual = customerDAO.findByShortNumber("%" + phoneNumber.substring(phoneNumber.length() - 4));
        assertTrue(actual.contains(expected));
    }

    @Test
    public void findByFullNumberTest() {
        int random = (int) Math.floor(Math.random()*(allCustomers.size()));
        Customer expected = allCustomers.get(random);
        List<Customer> actual = customerDAO.findByShortNumber(expected.getPhoneNumber());
        assertTrue(actual.contains(expected));
    }

    @Test
    public void findByAlphaTest() {
        int random = (int) Math.floor(Math.random()*(allCustomers.size()));
        Customer expected = allCustomers.get(random);
        List<Customer> actual = customerDAO.findByShortNumber("%" + expected.getName());
        assertTrue(actual.isEmpty());
        actual = customerDAO.findByFullNumber(expected.getName());
        assertTrue(actual.isEmpty());
    }

    @Test
    public void deleteTest() {
        int random = (int) Math.floor(Math.random()*(allCustomers.size()));
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

    @Test
    public void updateCupsTest() {
        int random = (int) Math.floor(Math.random()*(allCustomers.size()));
        Customer expected = allCustomers.get(random);
        expected.setCups(4);
        customerDAO.update(expected);
        String phoneNumber = expected.getPhoneNumber();
        List<Customer> actual = customerDAO.findByShortNumber("%" + phoneNumber.substring(phoneNumber.length() - 4)).stream().collect(Collectors.toList());
        assertTrue(actual.contains(expected));
    }

    @Test
    public void updateNumberTest() {
        int random = (int) Math.floor(Math.random()*(allCustomers.size()));
        Customer expected = allCustomers.get(random);
        String oldNumber = expected.getPhoneNumber();
        String newNumber = CustomerGenerator.randomNumber();
        expected.setPhoneNumber(newNumber);

        customerDAO.update(expected);
        List<Customer> actual = customerDAO.findByShortNumber("%" + newNumber.substring(newNumber.length() - 4)).stream().collect(Collectors.toList());
        List<Customer> searchOld = customerDAO.findByShortNumber("%" + oldNumber.substring(oldNumber.length() - 4)).stream().collect(Collectors.toList());
        assertTrue(actual.contains(expected));
        assertTrue(searchOld.isEmpty());
    }
}

