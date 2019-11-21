/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.useraccount.controllers;

import java.util.UUID;
import javax.ejb.Stateless;

/**
 *
 * @author jsie
 */
@Stateless
public class UUIDGenerator {

    public String generate() {
        return UUID.randomUUID().toString();
    }

    public String generate(boolean compact) {
        String uuid = generate();
        return compact ? uuid.replaceAll("-", "") : uuid;
    }

    public String generate(String header, boolean compact) {
        String uuid = generate(compact);
        return (header != null) ? header.concat(uuid) : uuid;
    }

}
