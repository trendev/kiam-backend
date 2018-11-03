/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.purchaseexpense.entities.PurchaseExpense;
import fr.trendev.comptandye.utils.ExpenseType;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class PurchaseExpenseTest {

    public PurchaseExpenseTest() {
    }

    @Test
    public void testConstructor() {
        PurchaseExpense instance = new PurchaseExpense();

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

        assert instance.getInvoiceRef() == null;
        assert instance.getPurchasedItems() != null;
        assert instance.getPurchasedItems().isEmpty();

        assert ExpenseType.PURCHASE_EXPENSE.equals(instance.getCltype());

    }

}
