package fr.trendev.comptandye.security.entities;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class JWTWhiteMapEntry implements Serializable,
        Comparable<JWTWhiteMapEntry> {

    private String email;
    private Set<JWTRecord> records;

    public JWTWhiteMapEntry() {
    }

    public JWTWhiteMapEntry(String email, Set<JWTRecord> records) {
        this.email = email;
        this.records = records;
    }

    public JWTWhiteMapEntry(Map.Entry<String, Set<JWTRecord>> entry) {
        this(entry.getKey(), new TreeSet(entry.getValue()));
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

    @Override
    public int compareTo(JWTWhiteMapEntry o) {
        return this.email.compareTo(o.email);
    }

}
