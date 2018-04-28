//
// This file was generated by the Jeddict
//
package fr.trendev.comptandye.entities;

import java.io.Serializable;

public class ProductPK implements Serializable {

    private String professional;

    private String productReference;

    public ProductPK() {
    }

    public ProductPK(String professional, String productReference) {
        this.professional = professional;
        this.productReference = productReference;
    }

    public String getProfessional() {
        return this.professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getProductReference() {
        return this.productReference;
    }

    public void setProductReference(String productReference) {
        this.productReference = productReference;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!java.util.Objects.equals(getClass(), obj.getClass())) {
            return false;
        }
        final ProductPK other = (ProductPK) obj;
        if (!java.util.Objects.equals(this.getProfessional(), other.
                getProfessional())) {
            return false;
        }
        if (!java.util.Objects.equals(this.getProductReference(), other.
                getProductReference())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.getProfessional() != null ? this.
                getProfessional().hashCode() : 0);
        hash = 97 * hash + (this.getProductReference() != null ? this.
                getProductReference().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ProductPK{" + " professional=" + professional
                + ", productReference=" + productReference + '}';
    }

}