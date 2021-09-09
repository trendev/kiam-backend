/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import javax.security.enterprise.identitystore.IdentityStore;

/**
 *
 * @author jsie
 */
//@ApplicationScoped
/**
 * DEV/TEST purposes only, should not be provided on PRODUCTION
 *
 * @author jsie
 */
public class InMemoryIdentityStoreDEV implements IdentityStore {

    private final Map<String, Set<String>> accounts;
    private final Map<String, String> passwords;
    private final String email;

    public InMemoryIdentityStoreDEV() {
        this.accounts = new HashMap<>();
        this.passwords = new TreeMap<>();
        this.email = "trendevfr@gmail.com";

        accounts.put(email,
                new HashSet<>(Arrays.asList("Administrator")));
        passwords.put(email, "BimBamB00M");
    }

    @Override
    public int priority() {
        return 100;
    }

    public CredentialValidationResult validate(
            UsernamePasswordCredential credential) {

        String user = credential.getCaller();

        if (credential.compareTo(user,
                passwords.get(user))) {
            return new CredentialValidationResult(user, accounts.get(user));
        }
        return INVALID_RESULT;
    }
}
