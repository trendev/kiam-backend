/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.entities;

import fr.trendev.kiam.bill.entities.BillType;
import fr.trendev.kiam.clientbill.entities.ClientBill;
import fr.trendev.kiam.payment.entities.Payment;
import fr.trendev.kiam.paymentmode.entities.PaymentMode;
import fr.trendev.kiam.professional.entities.Professional;
import fr.trendev.kiam.purchasedoffering.entities.PurchasedOffering;
import fr.trendev.kiam.service.entities.Service;
import fr.trendev.kiam.utils.UUIDGenerator;
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
public class ClientBillTest {

    private UUIDGenerator UUIDGenerator;

    public ClientBillTest() {
        this.UUIDGenerator = new UUIDGenerator();
    }

    @Test
    public void testConstructors() {
        ClientBill instance = new ClientBill();

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
        assert instance.getClient() == null;
        assert BillType.CLIENT.equals(instance.getCltype());

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
                .mapToObj(i -> new Service("Service #" + i, 1000, 10,
                professional))
                .map(o -> new PurchasedOffering(1, o))
                .collect(Collectors.toList());

    }
}
