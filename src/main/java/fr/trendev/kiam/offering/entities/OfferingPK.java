package fr.trendev.kiam.offering.entities;

import java.io.Serializable;
import java.util.Objects;

public class OfferingPK implements Serializable {

    private String id;
    private String professional;

    public OfferingPK() {
    }

    public OfferingPK(String id, String professional) {
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
        final OfferingPK other = (OfferingPK) obj;
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
        return "OfferingPK{" + " id=" + id + ", professional=" + professional + '}';
    }

}