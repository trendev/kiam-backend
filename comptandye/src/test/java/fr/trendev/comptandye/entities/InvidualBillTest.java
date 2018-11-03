/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.service.entities.Service;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.paymentmode.entities.PaymentMode;
import fr.trendev.comptandye.payment.entities.Payment;
import fr.trendev.comptandye.purchasedoffering.entities.PurchasedOffering;
import fr.trendev.comptandye.individualbill.entities.IndividualBill;
import fr.trendev.comptandye.individual.entities.Individual;
import fr.trendev.comptandye.utils.BillType;
import fr.trendev.comptandye.utils.UUIDGenerator;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class InvidualBillTest {

    public InvidualBillTest() {
    }

    /**
     * Test of getIndividual method, of class IndividualBill.
     */
    @Test
    public void testConstructors() {
        IndividualBill instance = new IndividualBill();
        assert instance.getReference() == null;
        assert instance.getDeliveryDate() == null;
        assert instance.getAmount() == 0;
        assert instance.getCurrency().equals("EUR");
        assert instance.getDiscount() == 0;
        assert instance.getPaymentDate() == null;
        assert instance.getComments() != null;
        assert instance.getComments().isEmpty();
        assert instance.getProfessional() == null;
        assert instance.getPayments() != null;
        assert instance.getPayments().isEmpty();
        assert instance.getPurchasedOfferings() != null;
        assert instance.getPurchasedOfferings().isEmpty();
        assert instance.getIndividual() == null;

        assert BillType.INDIVIDUAL.equals(instance.getCltype());

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
        Payment payment = new Payment(9000,
                new PaymentMode("Credit Card"));
        List<Payment> payments = Arrays.asList(payment);
        List<PurchasedOffering> purchasedOfferings = IntStream
                .range(0, 10)
                .mapToObj(i -> new PurchasedOffering(1, new Service(
                        "Service #" + i, 1000, 10,
                        professional)))
                .collect(Collectors.toList());

        instance = new IndividualBill(reference, deliveryDate, amount,
                discount,
                paymentDate,
                comments, professional, payments, purchasedOfferings,
                new Individual());

        assert instance.getReference().equals(reference);
        assert instance.getDeliveryDate() != null;
        assert instance.getAmount() == amount;
        assert instance.getCurrency().equals("EUR");
        assert instance.getDiscount() == discount;
        assert instance.getPaymentDate() != null;
        assert instance.getComments() != null;
        assert instance.getComments().size() == 2;
        assert instance.getProfessional().equals(professional);
        assert instance.getPayments() != null;

        assert instance.getPayments().contains(payment);
        assert instance.getPurchasedOfferings() != null;
        assert instance.getPurchasedOfferings().size() == 10;

        assert instance.getPurchasedOfferings().stream().mapToInt(po -> po.
                getOffering().getPrice()).
                sum() == totalAmount;

        assert instance.getPayments().stream().mapToInt(p -> p.getAmount()).
                sum() == amount;

        assert amount == (totalAmount - (discount * totalAmount / 100));

        assert instance.getIndividual() != null;

        assert BillType.INDIVIDUAL.equals(instance.getCltype());
    }

}
