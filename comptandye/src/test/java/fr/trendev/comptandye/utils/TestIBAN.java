package fr.trendev.comptandye.utils;

import java.math.BigDecimal;
import org.junit.Test;

public class TestIBAN {

    @Test
    /**
     * https://en.wikipedia.org/wiki/International_Bank_Account_Number#Validating_the_IBAN
     */
    public void testIBAN() {
        assert (new BigDecimal("3214282912345698765432161182").remainder(
                new BigDecimal(97))).compareTo(BigDecimal.ONE) == 0;

        assert 321428291 % 97 == 70;
        assert 702345698 % 97 == 29;
        assert 297654321 % 97 == 24;
        assert 2461182 % 97 == 1;

    }

}
