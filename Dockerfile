###############
# Build the war
###############
FROM maven:3.6 as maven

COPY ./pom.xml ./pom.xml

RUN mvn dependency:go-offline -B

COPY ./src ./src

RUN mvn install -Pprod

#########################################
# Configure the server and deploy the war
#########################################
FROM payara/server-full:5.184

MAINTAINER TRENDev trendevfr@gmail.com

ENV VERSION 2.2.4
ENV COMPTANDYE comptandye-$VERSION

ENV AS_ADMIN $PAYARA_DIR/bin/asadmin
ENV DOMAIN production
ENV ADMIN_USER admin
ENV ADMIN_PASSWORD admin
ENV NEW_ADMIN_PASSWORD qsec0fr
ENV JCONNECTOR_VERSION 5.1.47
ENV MEMORY_SIZE 4096

# Copy the mysql connector to the Glassfish libs
# Check the libs path before uncomment
COPY mysql-connector-java-$JCONNECTOR_VERSION.jar $PAYARA_DIR/glassfish/domains/$DOMAIN/lib

# Reset the admin password
RUN echo 'AS_ADMIN_PASSWORD='$ADMIN_PASSWORD'\n\
AS_ADMIN_NEWPASSWORD='$NEW_ADMIN_PASSWORD'\n\
EOF\n'\
> /tmp/tmpfile

RUN echo 'AS_ADMIN_PASSWORD='$NEW_ADMIN_PASSWORD'\n\
EOF\n'\
> ${PASSWORD_FILE}

RUN $AS_ADMIN start-domain $DOMAIN && \
$AS_ADMIN --user $ADMIN_USER --passwordfile=/tmp/tmpfile change-admin-password && \
$AS_ADMIN --user $ADMIN_USER --passwordfile=${PASSWORD_FILE} enable-secure-admin && \
$AS_ADMIN restart-domain $DOMAIN && \
$AS_ADMIN create-javamail-resource --passwordfile=${PASSWORD_FILE} --mailhost smtp\.gmail\.com --mailuser comptandye\@gmail\.com --fromaddress comptandye\@gmail\.com --debug=true --enabled=true --property="mail-password=ptNIDIFKMX4MACaJCwpo:mail-auth=true:mail.smtp.user=comptandye@gmail.com:mail.smtp.password=ptNIDIFKMX4MACaJCwpo:mail.smtp.auth=true:mail.smtp.port=465:mail.smtp.socketFactory.port=465:mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory" java/mail/comptandye && \
$AS_ADMIN create-jdbc-connection-pool --passwordfile=${PASSWORD_FILE} --allownoncomponentcallers=false --associatewiththread=false --creationretryattempts=0 --creationretryinterval=10 --leakreclaim=false --leaktimeout=0 --validationmethod=auto-commit --datasourceclassname=com.mysql.jdbc.jdbc2.optional.MysqlDataSource --failconnection=false --idletimeout=300 --isconnectvalidatereq=false --isisolationguaranteed=false --lazyconnectionassociation=false --lazyconnectionenlistment=false --matchconnections=false --maxconnectionusagecount=0 --maxpoolsize=100 --maxwait=0 --nontransactionalconnections=false --poolresize=20 --restype=javax.sql.DataSource --statementtimeout=-1 --steadypoolsize=20 --validateatmostonceperiod=0 --wrapjdbcobjects=true --property serverName=db-mysql:portNumber=3306:databaseName=comptandye_master:User=admin_comptandye_20170328:Password=SfBuVPRw0S:URL=jdbc\\:mysql\\://db-mysql\\:3306/comptandye_master?zeroDateTimeBehavior\\=convertToNull\&useSSL\\=false:driverClass=com.mysql.jdbc.Driver mysql_comptandye_master_admin_comptandye_20170328Pool && \
$AS_ADMIN create-jdbc-resource --passwordfile=${PASSWORD_FILE} --enabled=true --connectionpoolid=mysql_comptandye_master_admin_comptandye_20170328Pool jdbc/MySQLDataSourceComptaNdye && \
$AS_ADMIN create-auth-realm --passwordfile=${PASSWORD_FILE} --classname com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm --property="jaas-context=jdbcRealm:encoding=Base64:password-column=PASSWORD:datasource-jndi=jdbc/MySQLDataSourceComptaNdye:group-table=USER_ACCOUNT_USER_GROUP:charset=UTF-8:user-table=USER_ACCOUNT:group-name-column=userGroups_NAME:group-table-user-name-column=userAccounts_EMAIL:digest-algorithm=SHA-256:user-name-column=EMAIL" comptandye-security-realm && \
$AS_ADMIN set configs.config.server-config.security-service.activate-default-principal-to-role-mapping=true --passwordfile=${PASSWORD_FILE} && \
$AS_ADMIN set configs.config.server-config.admin-service.das-config.dynamic-reload-enabled=false --passwordfile=${PASSWORD_FILE} && \
$AS_ADMIN delete-jvm-options --passwordfile=${PASSWORD_FILE} -client:-Xmx512m && \
$AS_ADMIN create-jvm-options --passwordfile=${PASSWORD_FILE} -server:-Xmx${MEMORY_SIZE}m:-Xms${MEMORY_SIZE}m:-Dfish.payara.classloading.delegate=false:-Duser.timezone=Europe/Paris
#$AS_ADMIN restart-domain $DOMAIN

# Copy the tuning script
USER root
COPY tuning-script.sh ${SCRIPT_DIR}
RUN chmod +x ${SCRIPT_DIR}/tuning-script.sh
USER payara

# Autodeploy the project
COPY --from=maven ./target/*.war $DEPLOY_DIR/
