package fr.trendev.kiam.product.entities;

import java.io.Serializable;
import java.util.Objects;

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
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getProductReference() {
        return productReference;
    }

    public void setProductReference(String productReference) {
        this.productReference = productReference;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Objects.equals(getClass(), obj.getClass())) {
            return false;
        }
        final ProductPK other = (ProductPK) obj;
        if (!java.util.Objects.equals(this.getProfessional(), other.getProfessional())) {
            return false;
        }
        if (!java.util.Objects.equals(this.getProductReference(), other.getProductReference())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.getProfessional());
        hash = 31 * hash + Objects.hashCode(this.getProductReference());
        return hash;
    }

    @Override
    public String toString() {
        return "ProductPK{" + " professional=" + professional + ", productReference=" + productReference + '}';
    }

}