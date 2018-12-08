package fr.trendev.comptandye.security.entities;

import java.util.Set;

public class JWTWhiteMapEntry {

    private String email;
    private Set<JWTRecord> records;

    public JWTWhiteMapEntry(String email, Set<JWTRecord> records) {
        this.email = email;
        this.records = records;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<JWTRecord> getRecords() {
        return records;
    }

    public void setRecords(Set<JWTRecord> records) {
        this.records = records;
    }
}
