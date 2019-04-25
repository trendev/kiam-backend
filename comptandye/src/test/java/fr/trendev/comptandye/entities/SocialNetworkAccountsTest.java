/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.socialnetworkaccounts.entities.SocialNetworkAccounts;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class SocialNetworkAccountsTest {

    public SocialNetworkAccountsTest() {
    }

    @Test
    public void testConstructors() {

        SocialNetworkAccounts instance = new SocialNetworkAccounts();

        assert instance.getId() == null;
        assert instance.getFacebook() == null;
        assert instance.getTwitter() == null;
        assert instance.getInstagram() == null;
        assert instance.getPinterest() == null;

        String facebook = "A facebook account";
        String twitter = "A twitter account";
        String instagram = "A instagram account";
        String pinterest = "A pinterest account";

        instance = new SocialNetworkAccounts(facebook, twitter, instagram,
                pinterest);

        assert instance.getId() == null;
        assert facebook.equals(instance.getFacebook());
        assert twitter.equals(instance.getTwitter());
        assert instagram.equals(instance.getInstagram());
        assert pinterest.equals(instance.getPinterest());

    }

}
