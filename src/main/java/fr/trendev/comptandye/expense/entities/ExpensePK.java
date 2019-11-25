package fr.trendev.comptandye.expense.entities;

import java.io.Serializable;
import java.util.Objects;

public class ExpensePK implements Serializable {

    private String id;
    private String professional;

    public ExpensePK() {
    }

    public ExpensePK(String id, String professional) {
        this.id = id;
        this.professional = professional;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        final ExpensePK other = (ExpensePK) obj;
        if (!java.util.Objects.equals(this.getId(), other.getId())) {
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
        hash = 31 * hash + Objects.hashCode(this.getId());
        hash = 31 * hash + Objects.hashCode(this.getProfessional());
        return hash;
    }

    @Override
    public String toString() {
        return "ExpensePK{" + " id=" + id + ", professional=" + professional + '}';
    }

}