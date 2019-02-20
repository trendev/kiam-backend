/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.mock;

import fr.trendev.comptandye.security.controllers.jwt.dto.JWTRevokedSetDTO;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class MockJWTRevokedSetDTO implements JWTRevokedSetDTO {

    private static final Logger LOG =
            Logger.getLogger(MockJWTRevokedSetDTO.class.getName());

    @Override
    public void init() {
        LOG.log(Level.INFO, "{0} initialized",
                JWTRevokedSetDTO.class.getSimpleName());
    }

    @Override
    public void close() {
        LOG.log(Level.INFO, "{0} closed",
                JWTRevokedSetDTO.class.getSimpleName());
    }

}
