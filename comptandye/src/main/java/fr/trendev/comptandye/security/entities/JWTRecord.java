/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author jsie
 */
public class JWTRecord implements Serializable, Comparable<JWTRecord> {

    private String token;
    private Date creationDate;
    private Date expirationDate;

    public JWTRecord(String token, Date creationDate, Date expirationDate) {

        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException(
                    "token String must not be null or empty");
        }

        if (creationDate == null || expirationDate == null) {
            throw new IllegalArgumentException(
                    "creationDate and expirationDate must not be null");
        }

        if (creationDate.equals(expirationDate)) {
            throw new IllegalArgumentException(
                    "creationDate and expirationDate must not be the same");
        }

        if (expirationDate.before(creationDate)) {
            throw new IllegalArgumentException(
                    "expirationDate must not be before creationDate");
        }

        this.token = token;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
    }

    public JWTRecord() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.token);
        hash = 97 * hash + Objects.hashCode(this.creationDate);
        hash = 97 * hash + Objects.hashCode(this.expirationDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JWTRecord other = (JWTRecord) obj;
        if (!Objects.equals(this.token, other.token)) {
            return false;
        }
        if (!Objects.equals(this.creationDate, other.creationDate)) {
            return false;
        }
        if (!Objects.equals(this.expirationDate, other.expirationDate)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(JWTRecord o) {
        int tokenDiff = this.token.compareTo(o.token);
        if (tokenDiff != 0) {
            return tokenDiff;
        } else {
            int creationDateDiff = this.creationDate.compareTo(o.creationDate);
            if (creationDateDiff != 0) {
                return creationDateDiff;
            } else {
                return this.expirationDate.compareTo(o.expirationDate);
            }
        }
    }

}
