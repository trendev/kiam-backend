/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.utils.UUIDGenerator;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
public class BillTest {

    public BillTest() {
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

    @Test
    public void testConstructors() {
        Bill instance = new BillImpl();

        assert instance.getReference() == null;
        assert instance.getDeliveryDate() == null;
        assert instance.getAmount() == 0;
        assert instance.getDiscount() == 0;
        assert instance.getPaymentDate() == null;
        assert instance.getComments() != null;
        assert instance.getComments().isEmpty();
        assert instance.getProfessionalFromBill() == null;
        assert instance.getPayments() != null;
        assert instance.getPayments().isEmpty();
        assert instance.getOfferings() != null;
        assert instance.getOfferings().isEmpty();

        String reference = "Ref-123456";
        Date deliveryDate = new Date();
        int totalAmount = 10000;
        int amount = 9000; //90 euros
        int discount = 10; // 10%
        Date paymentDate = deliveryDate;
        List<String> comments = Arrays.asList("Comment #1", "Comment #2");
        Professional professional = new Professional("pro@company.com",
                "encrypted_pwd", "PRO01",
                UUIDGenerator.generate());
        Payment payment = new Payment(9000, "EUR",
                new PaymentMode("Credit Card"));
        List<Payment> payments = Arrays.asList(payment);
        List<Offering> offerings = IntStream
                .range(0, 10)
                .mapToObj(i -> new Service("Service #" + i, 1000, 10))
                .collect(Collectors.toList());

        instance = new BillImpl(reference, deliveryDate, amount, discount,
                paymentDate,
                comments,
                professional, payments, offerings);

        assert instance.getReference().equals(reference);
        assert instance.getDeliveryDate() != null;
        assert instance.getAmount() == amount;
        assert instance.getDiscount() == discount;
        assert instance.getPaymentDate() != null;
        assert instance.getComments() != null;
        assert instance.getComments().size() == 2;
        assert instance.getProfessionalFromBill().equals(professional);
        assert instance.getPayments() != null;

        assert instance.getPayments().contains(payment);
        assert instance.getOfferings() != null;
        assert instance.getOfferings().size() == 10;

        assert instance.getOfferings().stream().mapToInt(o -> o.getPrice()).
                sum() == totalAmount;

        assert instance.getPayments().stream().mapToInt(p -> p.getAmount()).
                sum() == amount;

        assert amount == (totalAmount - (discount * totalAmount / 100));

    }

    public class BillImpl extends Bill {

        public BillImpl(String reference, Date deliveryDate, int amount,
                int discount, Date paymentDate, List comments,
                Professional professional, List payments, List offerings) {
            super(reference, deliveryDate, amount, discount, paymentDate,
                    comments, professional, payments, offerings);
        }

        public BillImpl() {
        }

    }

}
