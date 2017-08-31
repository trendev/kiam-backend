/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Address;
import fr.trendev.comptandye.sessions.AddressFacade;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author jsie
 */
public class AddressService extends AbstractCommonService<Address, Long> {

    @Inject
    AddressFacade addressFacade;

    private static final Logger LOG = Logger.getLogger(
            AdministratorService.class.
                    getName());

    public AddressService() {
        super(Address.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }
}
