package fr.trendev.comptandye.classicexpense.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.expense.entities.Expense;
import fr.trendev.comptandye.expense.entities.ExpenseType;
import javax.persistence.Entity;

@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClassicExpense extends Expense {

    public ClassicExpense() {
        this.cltype = ExpenseType.CLASSIC_EXPENSE;
    }
}
