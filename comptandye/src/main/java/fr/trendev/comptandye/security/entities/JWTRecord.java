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
    private Date creationTime;
    private Date expirationDate;

    public JWTRecord(String token, Date creationTime, Date expirationDate) {

        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException(
                    "token String must not be null or empty");
        }

        if (creationTime == null || expirationDate == null) {
            throw new IllegalArgumentException(
                    "creationTime and expirationDate must not be null");
        }

        if (creationTime.equals(expirationDate)) {
            throw new IllegalArgumentException(
                    "creationTime and expirationDate must not be the same");
        }

        if (expirationDate.before(creationTime)) {
            throw new IllegalArgumentException(
                    "expirationDate must not be before creationTime");
        }

        this.token = token;
        this.creationTime = creationTime;
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

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
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
        hash = 97 * hash + Objects.hashCode(this.creationTime);
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
        if (!Objects.equals(this.creationTime, other.creationTime)) {
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
            int creationTimeDiff = this.creationTime.compareTo(o.creationTime);
            if (creationTimeDiff != 0) {
                return creationTimeDiff;
            } else {
                return this.expirationDate.compareTo(o.expirationDate);
            }
        }
    }

}
