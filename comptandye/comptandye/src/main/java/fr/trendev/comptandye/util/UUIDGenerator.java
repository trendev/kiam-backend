/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.util;

import java.util.UUID;

/**
 *
 * @author jsie
 */
public class UUIDGenerator {

    public static String generate() {
        return UUID.randomUUID().toString();
    }

    public static String generate(boolean compact) {
        String uuid = generate();
        return compact ? uuid.replaceAll("-", "") : uuid;
    }

    public static String generate(String header, boolean compact) {
        String uuid = generate(compact);
        return (header != null) ? header.concat(uuid) : uuid;
    }

}
