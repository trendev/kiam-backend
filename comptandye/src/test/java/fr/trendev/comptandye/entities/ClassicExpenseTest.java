/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.utils.ExpenseType;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class ClassicExpenseTest {

    public ClassicExpenseTest() {
    }

    /**
     * Test of getId method, of class Expense.
     */
    @Test
    public void testConstructors() {
        ClassicExpense instance = new ClassicExpense();

        assert instance.getId() == null;
        assert instance.getDescription() == null;
        assert instance.getAmount() == 0;
        assert instance.getCurrency().equals("EUR");
        assert instance.getPaymentDate() == null;
        assert instance.getProvider() == null;
        assert instance.getCategories() != null;
        assert instance.getCategories().isEmpty();
        assert instance.getPayments() != null;
        assert instance.getPayments().isEmpty();
        assert instance.getExpenseItems() != null;
        assert instance.getExpenseItems().isEmpty();
        assert instance.getBusinesses() != null;
        assert instance.getBusinesses().isEmpty();
        assert instance.getIssueDate() != null;
        assert ExpenseType.CLASSIC_EXPENSE.equals(instance.getCltype());

        String name = "Expensive expense";
        int amount = 50000; // 1000 euros
        String invoiceRef = "Invoice12345ABC";

        instance = new ClassicExpense();
        instance.setDescription(name);
        instance.setAmount(amount);
        instance.setPaymentDate(new Date());
        instance.setCategories(Arrays.
                asList("Partner", "Provider"));
        instance.setProfessional(new Professional());
        instance.setPayments(
                Arrays.asList(new Payment(30000, new PaymentMode("CB")),
                        new Payment(30000, new PaymentMode("Espèces"))));
        instance.setBusinesses(Arrays.asList(new Business("Buziness")));

        int size = 10;
        instance.setCategories(IntStream.range(0, size).mapToObj(i ->
                ("Category #" + i)).collect(
                Collectors.toList()));

        assert instance.getId() == null;
        assert instance.getDescription().equals(name);
        assert instance.getAmount() == amount;
        assert instance.getCurrency().equals("EUR");
        assert instance.getPaymentDate() != null;
        assert instance.getCategories() != null;
        assert instance.getCategories().size() == size;
        assert instance.getPayments() != null;
        assert instance.getPayments().size() == 2;
        assert instance.getProfessional() != null;
        assert instance.getExpenseItems() != null;
        assert instance.getExpenseItems().isEmpty();
        assert instance.getBusinesses() != null;
        assert !instance.getBusinesses().isEmpty();
        assert instance.getIssueDate() != null;
        assert ExpenseType.CLASSIC_EXPENSE.equals(instance.getCltype());

    }

}
