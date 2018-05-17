//
// This file was generated by the Jeddict
//
package fr.trendev.comptandye.entities;

import java.io.Serializable;

public class ExpensePK implements Serializable {

    private Long id;

    private String professional;

    public ExpensePK() {
    }

    public ExpensePK(Long id, String professional) {
        this.id = id;
        this.professional = professional;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfessional() {
        return this.professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!java.util.Objects.equals(getClass(), obj.getClass())) {
            return false;
        }
        final ExpensePK other = (ExpensePK) obj;
        if (!java.util.Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        if (!java.util.Objects.equals(this.getProfessional(), other.
                getProfessional())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        hash = 43 * hash + (this.getProfessional() != null ? this.
                getProfessional().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ExpensePK{" + " id=" + id + ", professional=" + professional
                + '}';
    }

}