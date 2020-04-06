/** * This file was generated by the JPA Modeler */
package fr.trendev.kiam.basicexpense.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.kiam.expense.entities.Expense;
import fr.trendev.kiam.expense.entities.ExpenseType;
import javax.persistence.Entity;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BasicExpense extends Expense {

    public BasicExpense() {
        this.cltype = ExpenseType.BASIC_EXPENSE;
    }

}