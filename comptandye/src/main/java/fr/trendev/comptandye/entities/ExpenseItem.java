/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExpenseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    @NotNull(message = "description field in ExpenseItem must not be null")
    private String description;

    @Basic
    private int amount;

    @Basic
    @Min(value = 1, message = "A quantity must be provided in the ExpenseItem")
    private int qty;

    @Column(scale = 2, precision = 5)
    @Basic
    @NotNull(message = "vatrate field in ExpenseItem must not be null")
    private BigDecimal vatRate;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getQty() {
        return this.qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getVatRate() {
        return this.vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

}