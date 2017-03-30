//
// This file was generated by the JPA Modeler
//
package fr.trendev.comptandye.entities;

import java.io.Serializable;
import java.util.Date;




public class BillPK implements Serializable {

    private Long reference;

    private Date deliveryDate;

    private String professional;

    public BillPK(){
    }

    public BillPK(Long reference,Date deliveryDate,String professional){
        this.reference=reference;
        this.deliveryDate=deliveryDate;
        this.professional=professional;
    }


   public Long getReference() {
        return this.reference;
    }

    public void setReference (Long reference) {
        this.reference = reference;
    }

   public Date getDeliveryDate() {
        return this.deliveryDate;
    }

    public void setDeliveryDate (Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

   public String getProfessional() {
        return this.professional;
    }

    public void setProfessional (String professional) {
        this.professional = professional;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {return false;}
        if (!java.util.Objects.equals(getClass(), obj.getClass())) {return false;}
        final BillPK other = (BillPK) obj;
        if (!java.util.Objects.equals(this.getReference(), other.getReference())) {        return false;        }
        if (!java.util.Objects.equals(this.getDeliveryDate(), other.getDeliveryDate())) {        return false;        }
        if (!java.util.Objects.equals(this.getProfessional(), other.getProfessional())) {        return false;        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.getReference() != null ? this.getReference().hashCode() : 0);
        hash = 71 * hash + (this.getDeliveryDate() != null ? this.getDeliveryDate().hashCode() : 0);
        hash = 71 * hash + (this.getProfessional() != null ? this.getProfessional().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "BillPK{" + " reference=" + reference + ", deliveryDate=" + deliveryDate + ", professional=" + professional + '}';
    }

}