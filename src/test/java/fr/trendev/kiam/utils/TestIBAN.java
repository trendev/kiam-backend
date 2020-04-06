package fr.trendev.kiam.utils;

import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestIBAN {

    @Test
    /**
     * https://en.wikipedia.org/wiki/International_Bank_Account_Number#Validating_the_IBAN
     */
    public void testIBAN() {
        assert (new BigDecimal("3214282912345698765432161182").remainder(
                new BigDecimal(97))).compareTo(BigDecimal.ONE) == 0;

        assertTrue(321428291 % 97 == 70);
        assertTrue(702345698 % 97 == 29);
        assertTrue(297654321 % 97 == 24);
        assertTrue(2461182 % 97 == 1);

    }

}
