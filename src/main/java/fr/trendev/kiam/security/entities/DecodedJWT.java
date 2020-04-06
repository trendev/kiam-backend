/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.entities;

import com.nimbusds.jwt.JWTClaimsSet;

/**
 *
 * @author jsie
 */
public class DecodedJWT {
    private final String jwt; // original signed JWT
    private final JWTClaimsSet claimsSet; // the decoded JWT structure

    public DecodedJWT(String jwt, JWTClaimsSet claimsSet) {
        this.jwt = jwt;
        this.claimsSet = claimsSet;
    }

    public String getJwt() {
        return jwt;
    }

    public JWTClaimsSet getClaimsSet() {
        return claimsSet;
    }
    
}
