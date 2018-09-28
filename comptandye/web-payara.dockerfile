FROM payara/server-full:174

MAINTAINER TRENDev trendevfr@gmail.com

ENV AS_ADMIN $PAYARA_PATH/bin/asadmin
ENV DOMAIN domain1
ENV ADMIN_USER admin
ENV ADMIN_PASSWORD admin
ENV NEW_ADMIN_PASSWORD qsec0fr
ENV VERSION 2.2.3
ENV COMPTANDYE comptandye-$VERSION

# Copy the mysql connector to the Glassfish libs
COPY ./target/$COMPTANDYE/WEB-INF/lib/mysql-connector-java-5.1.46.jar $PAYARA_PATH/glassfish/domains/domain1/lib

# Reset the admin password
RUN echo 'AS_ADMIN_PASSWORD='$ADMIN_PASSWORD'\n\
AS_ADMIN_NEWPASSWORD='$NEW_ADMIN_PASSWORD'\n\
EOF\n'\
> /opt/tmpfile

RUN echo 'AS_ADMIN_PASSWORD='$NEW_ADMIN_PASSWORD'\n\
EOF\n'\
> /opt/pwdfile

# Setup the password, javamail, jdbc and the security realm
# DB Server is db-mysql and not localhost (which is the url with a standalone docker mysql) !!!
RUN $AS_ADMIN start-domain $DOMAIN && \
$AS_ADMIN --user $ADMIN_USER --passwordfile=/opt/tmpfile change-admin-password && \
$AS_ADMIN --user $ADMIN_USER --passwordfile=/opt/pwdfile enable-secure-admin && \
$AS_ADMIN restart-domain $DOMAIN && \
$AS_ADMIN create-javamail-resource --passwordfile=/opt/pwdfile --mailhost smtp\.gmail\.com --mailuser comptandye\@gmail\.com --fromaddress comptandye\@gmail\.com --debug=true --enabled=true --property="mail-password=ptNIDIFKMX4MACaJCwpo:mail-auth=true:mail.smtp.user=comptandye@gmail.com:mail.smtp.password=ptNIDIFKMX4MACaJCwpo:mail.smtp.auth=true:mail.smtp.port=465:mail.smtp.socketFactory.port=465:mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory" java/mail/comptandye && \
$AS_ADMIN create-jdbc-connection-pool --passwordfile=/opt/pwdfile --allownoncomponentcallers=false --associatewiththread=false --creationretryattempts=0 --creationretryinterval=10 --leakreclaim=false --leaktimeout=0 --validationmethod=auto-commit --datasourceclassname=com.mysql.jdbc.jdbc2.optional.MysqlDataSource --failconnection=false --idletimeout=300 --isconnectvalidatereq=false --isisolationguaranteed=false --lazyconnectionassociation=false --lazyconnectionenlistment=false --matchconnections=false --maxconnectionusagecount=0 --maxpoolsize=100 --maxwait=0 --nontransactionalconnections=false --poolresize=20 --restype=javax.sql.DataSource --statementtimeout=-1 --steadypoolsize=20 --validateatmostonceperiod=0 --wrapjdbcobjects=true --property serverName=db-mysql:portNumber=3306:databaseName=comptandye_master:User=admin_comptandye_20170328:Password=SfBuVPRw0S:URL=jdbc\\:mysql\\://db-mysql\\:3306/comptandye_master?zeroDateTimeBehavior\\=convertToNull\&useSSL\\=false:driverClass=com.mysql.jdbc.Driver mysql_comptandye_master_admin_comptandye_20170328Pool && \
$AS_ADMIN create-jdbc-resource --passwordfile=/opt/pwdfile --enabled=true --connectionpoolid=mysql_comptandye_master_admin_comptandye_20170328Pool jdbc/MySQLDataSourceComptaNdye && \
$AS_ADMIN create-auth-realm --passwordfile=/opt/pwdfile --classname com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm --property="jaas-context=jdbcRealm:encoding=Base64:password-column=PASSWORD:datasource-jndi=jdbc/MySQLDataSourceComptaNdye:group-table=USER_ACCOUNT_USER_GROUP:charset=UTF-8:user-table=USER_ACCOUNT:group-name-column=userGroups_NAME:group-table-user-name-column=userAccounts_EMAIL:digest-algorithm=SHA-256:user-name-column=EMAIL" comptandye-security-realm && \
$AS_ADMIN set configs.config.server-config.security-service.activate-default-principal-to-role-mapping=true --passwordfile=/opt/pwdfile && \
$AS_ADMIN set configs.config.server-config.admin-service.das-config.dynamic-reload-enabled=false --passwordfile=/opt/pwdfile && \
# $AS_ADMIN set configs.config.server-config.admin-service.das-config.autodeploy-enabled=false --passwordfile=/opt/pwdfile && \
$AS_ADMIN delete-jvm-options --passwordfile=/opt/pwdfile -client:-Xmx512m: && \
$AS_ADMIN create-jvm-options --passwordfile=/opt/pwdfile -server:-Xmx2048m:-Xms2048m:-Dfish.payara.classloading.delegate=false  && \
$AS_ADMIN restart-domain $DOMAIN

# Autodeploy the project
COPY ./target/$COMPTANDYE.war $AUTODEPLOY_DIR/comptandye.war

# Start the server in verbose mode
ENTRYPOINT $AS_ADMIN start-domain -v $DOMAIN
