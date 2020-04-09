/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author jsie
 */
@Singleton
@Startup
@DataSourceDefinition(name = "java:global/kiam/MySQLDataSource",
        className = "com.mysql.cj.jdbc.MysqlXADataSource",
        user = "${ENV=KIAM_DB_USER}", // defined as system env
        password = "${ENV=KIAM_DB_PASSWORD}", // defined as system env
        databaseName = "${ENV=KIAM_DB_NAME}", // defined as system env
        serverName = "${ENV=KIAM_DB_HOST}", // defined as system env
        portNumber = 3306,
        minPoolSize = 20,
        maxPoolSize = 500,
        maxIdleTime = 300,
        initialPoolSize = 20,
        properties = {
            "useSSL=false",
            "zeroDateTimeBehavior=CONVERT_TO_NULL",
            "fish.payara.is-connection-validation-required=true",
            "fish.payara.connection-validation-method=custom-validation",
            "fish.payara.validation-classname=org.glassfish.api.jdbc.validation.MySQLConnectionValidation",
            "fish.payara.fail-all-connections=true"
        }
)
public class DBConfig {

    private static final Logger LOG = Logger.getLogger(DBConfig.class.getName());

    @PostConstruct
    public void init() {
        LOG.info("DB Config : ok");
    }

    @PreDestroy()
    public void close() {
        LOG.info("DB Config : closing");
    }

}
