//
// This file was generated by the Jeddict
//
package fr.trendev.comptandye.entities;

import java.io.Serializable;

public class PaymentPK implements Serializable {

    private Long id;

    private BillPK bill;

    public PaymentPK() {
    }

    public PaymentPK(Long id, BillPK bill) {
        this.id = id;
        this.bill = bill;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BillPK getBill() {
        return this.bill;
    }

    public void setBill(BillPK bill) {
        this.bill = bill;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!java.util.Objects.equals(getClass(), obj.getClass())) {
            return false;
        }
        final PaymentPK other = (PaymentPK) obj;
        if (!java.util.Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        if (!java.util.Objects.equals(this.getBill(), other.getBill())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        hash = 23 * hash
                + (this.getBill() != null ? this.getBill().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "PaymentPK{" + " id=" + id + ", bill=" + bill + '}';
    }

}