/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.configs;

import java.util.Map;
import java.util.TreeMap;
import javax.annotation.security.DeclareRoles;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author jsie
 */
@DeclareRoles({
    "Administrator",
    "Professional",
    "Individual"
})
//@DatabaseIdentityStoreDefinition(
//        dataSourceLookup = "jdbc/MySQLDataSourceComptaNdye",
//        callerQuery = "select PASSWORD from USER_ACCOUNT where EMAIL=?",
//        groupsQuery = "select userGroups_NAME from USER_ACCOUNT_USER_GROUP where userAccounts_EMAIL = ?",
//        priority = 30
//)
//@FormAuthenticationMechanismDefinition(
//        loginToContinue =
//        @LoginToContinue(
//                useForwardToLogin = false,
//                loginPage = "/login.html",
//                errorPage = "/login-error.html"
//        ))
@BasicAuthenticationMechanismDefinition(realmName = "comptandye-security")
@ApplicationScoped
@ApplicationPath("api")
public class JAXRSConfiguration extends Application {

    @Inject
    private Pbkdf2PasswordHash pbkdf2Hash;

    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> map = new TreeMap<>();
        map.put("jersey.config.jsonFeature", "JacksonFeature");
        System.out.println("Qsec0fr@3 ====> " + this.pbkdf2Hash.generate(
                "Qsec0fr@3".toCharArray()));
        return map;
    }
}
