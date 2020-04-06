/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.useraccount.controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;

/**
 *
 * @author jsie
 */
@Stateless
public class EmailValidator {
    
    private final static String REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    
    private final Pattern pattern;

    public EmailValidator() {
        this.pattern = Pattern.compile(REGEX);
    }
    
    public boolean valid(String email){
        
        if(email == null ||  email.isEmpty()){
            return false;
        }
        
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
}
