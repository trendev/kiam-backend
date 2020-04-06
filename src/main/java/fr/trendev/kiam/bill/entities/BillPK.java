package fr.trendev.kiam.bill.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class BillPK implements Serializable {

    private String reference;
    private Date deliveryDate;
    private String professional;

    public BillPK() {
    }

    public BillPK(String reference, Date deliveryDate, String professional) {
        this.reference = reference;
        this.deliveryDate = deliveryDate;
        this.professional = professional;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Objects.equals(getClass(), obj.getClass())) {
            return false;
        }
        final BillPK other = (BillPK) obj;
        if (!java.util.Objects.equals(this.getReference(), other.getReference())) {
            return false;
        }
        if (!java.util.Objects.equals(this.getDeliveryDate(), other.getDeliveryDate())) {
            return false;
        }
        if (!java.util.Objects.equals(this.getProfessional(), other.getProfessional())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.getReference());
        hash = 31 * hash + Objects.hashCode(this.getDeliveryDate());
        hash = 31 * hash + Objects.hashCode(this.getProfessional());
        return hash;
    }

    @Override
    public String toString() {
        return "BillPK{" + " reference=" + reference + ", deliveryDate=" + deliveryDate + ", professional=" + professional + '}';
    }

}