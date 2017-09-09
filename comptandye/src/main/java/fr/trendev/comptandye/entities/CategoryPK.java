//
// This file was generated by the Jeddict
//
package fr.trendev.comptandye.entities;

import java.io.Serializable;

public class CategoryPK implements Serializable {

    private Long id;

    private String professionalFromCategory;

    public CategoryPK() {
    }

    public CategoryPK(Long id, String professionalFromCategory) {
        this.id = id;
        this.professionalFromCategory = professionalFromCategory;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfessionalFromCategory() {
        return this.professionalFromCategory;
    }

    public void setProfessionalFromCategory(String professionalFromCategory) {
        this.professionalFromCategory = professionalFromCategory;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!java.util.Objects.equals(getClass(), obj.getClass())) {
            return false;
        }
        final CategoryPK other = (CategoryPK) obj;
        if (!java.util.Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        if (!java.util.Objects.equals(this.getProfessionalFromCategory(), other.
                getProfessionalFromCategory())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        hash = 61 * hash + (this.getProfessionalFromCategory() != null ? this.
                getProfessionalFromCategory().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "CategoryPK{" + " id=" + id + ", professionalFromCategory="
                + professionalFromCategory + '}';
    }

}