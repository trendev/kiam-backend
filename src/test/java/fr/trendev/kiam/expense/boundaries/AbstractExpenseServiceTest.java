/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.expense.boundaries;

import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author jsie
 */
public class AbstractExpenseServiceTest {

    public AbstractExpenseServiceTest() {
    }

    /**
     * Test date comparison.
     */
    @Test
    public void testPost() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1982);
        c.set(Calendar.MONTH, 10);
        c.set(Calendar.DATE, 28);

        Date mybirthday = c.getTime();
        Date now = new Date();

        assertTrue(mybirthday.before(now), "my birthday should be before now");
        assertTrue(now.after(mybirthday), "now should be after my birthday");
    }

    /**
     * Test of put method, of class AbstractExpenseService.
     */
    @Test
    public void testPut() {

    }

}
