/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class ExpenseTest {

    public ExpenseTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getId method, of class Expense.
     */
    @Test
    public void testConstructors() {
        Expense instance = new Expense();

        assert instance.getId() == null;
        assert instance.getName() == null;
        assert instance.getAmount() == 0;
        assert instance.getPaymentDate() == null;
        assert instance.getInvoiceRef() == null;
        assert instance.getCategories() != null;
        assert instance.getCategories().isEmpty();
        assert instance.getPaymentModes() != null;
        assert instance.getPaymentModes().isEmpty();

        String name = "Expensive expense";
        int amount = 100000; // 1000 euros
        String invoiceRef = "Invoice12345ABC";

        instance = new Expense(name, amount, invoiceRef);

        assert instance.getId() == null;
        assert instance.getName().equals(name);
        assert instance.getAmount() == amount;
        assert instance.getPaymentDate() == null;
        assert instance.getInvoiceRef().equals(invoiceRef);
        assert instance.getCategories() != null;
        assert instance.getCategories().isEmpty();
        assert instance.getPaymentModes() != null;
        assert instance.getPaymentModes().isEmpty();

        instance = new Expense(name, amount, new Date(), invoiceRef,
                Arrays.asList(new PaymentMode(), new PaymentMode()));

        int size = 10;
        instance.setCategories(IntStream.range(0, size).mapToObj(i ->
                ("Category #" + i)).collect(
                Collectors.toList()));

        assert instance.getId() == null;
        assert instance.getName().equals(name);
        assert instance.getAmount() == amount;
        assert instance.getPaymentDate() != null;
        assert instance.getInvoiceRef().equals(invoiceRef);
        assert instance.getCategories() != null;
        assert instance.getCategories().size() == size;
        assert instance.getPaymentModes() != null;
        assert instance.getPaymentModes().size() == 2;

    }

}
