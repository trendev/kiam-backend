###############
# Build the war
###############
FROM maven:3.6 as maven

COPY ./pom.xml ./pom.xml

RUN mvn dependency:go-offline -B

COPY ./src ./src

RUN mvn install -Ppreprod

#########################################
# Configure the server and deploy the war
#########################################
FROM payara/server-full:5.201

LABEL maintainer="jsie@trendev.fr"

ENV AS_ADMIN $PAYARA_DIR/bin/asadmin
ENV DOMAIN production
ENV ADMIN_USER admin
ENV ADMIN_PASSWORD admin
ARG NEW_ADMIN_PASSWORD

# Tune the production settings
RUN $AS_ADMIN start-domain $DOMAIN && \
$AS_ADMIN create-jvm-options --passwordfile=${PASSWORD_FILE} "-XX\:MaxRAMPercentage=30.0" && \
$AS_ADMIN create-jvm-options --passwordfile=${PASSWORD_FILE} "-XX\:+UseContainerSupport" && \
$AS_ADMIN create-jvm-options --passwordfile=${PASSWORD_FILE} -server:-Dfish.payara.classloading.delegate=false:-Duser.timezone=Europe/Paris && \
echo 'AS_ADMIN_PASSWORD='${ADMIN_PASSWORD}'\nAS_ADMIN_NEWPASSWORD='${NEW_ADMIN_PASSWORD}'\n' > /tmp/tmpfile && \
echo 'AS_ADMIN_PASSWORD='${NEW_ADMIN_PASSWORD}'\n' > ${PASSWORD_FILE} && \
$AS_ADMIN --user $ADMIN_USER  --passwordfile=/tmp/tmpfile disable-secure-admin && \
$AS_ADMIN --user $ADMIN_USER  --passwordfile=/tmp/tmpfile change-admin-password && \
$AS_ADMIN --user $ADMIN_USER --passwordfile=${PASSWORD_FILE} enable-secure-admin && \
$AS_ADMIN --user $ADMIN_USER --passwordfile=${PASSWORD_FILE} stop-domain

# Disable dynamic reloading of applications
RUN echo 'set configs.config.server-config.admin-service.das-config.dynamic-reload-enabled=false' >> $POSTBOOT_COMMANDS

# Enable monitoring
RUN echo 'set-monitoring-console-configuration --enabled true' >> $POSTBOOT_COMMANDS

# Configure the HTTP listeners
# RUN echo 'set configs.config.server-config.network-config.network-listeners.network-listener.http-listener-1.jk-enabled=true' >> $POSTBOOT_COMMANDS
# RUN echo 'set configs.config.server-config.network-config.network-listeners.network-listener.http-listener-2.enabled=false' >> $POSTBOOT_COMMANDS
RUN echo 'set configs.config.server-config.network-config.protocols.protocol.http-listener-1.http.max-connections=500' >> $POSTBOOT_COMMANDS
RUN echo 'set configs.config.server-config.network-config.protocols.protocol.http-listener-1.http.timeout-seconds=60' >> $POSTBOOT_COMMANDS
RUN echo 'set configs.config.server-config.network-config.protocols.protocol.http-listener-1.http.file-cache.enabled=true' >> $POSTBOOT_COMMANDS
RUN echo 'set configs.config.server-config.network-config.protocols.protocol.http-listener-1.http.file-cache.max-age-seconds=36000' >> $POSTBOOT_COMMANDS
RUN echo 'set configs.config.server-config.network-config.transports.transport.tcp.acceptor-threads=4' >> $POSTBOOT_COMMANDS
RUN echo 'set configs.config.server-config.thread-pools.thread-pool.http-thread-pool.max-thread-pool-size=500' >> $POSTBOOT_COMMANDS
RUN echo 'set configs.config.server-config.thread-pools.thread-pool.http-thread-pool.min-thread-pool-size=25' >> $POSTBOOT_COMMANDS

# Configure the EJB container
RUN echo 'set configs.config.server-config.ejb-container.pool-resize-quantity=20' >> $POSTBOOT_COMMANDS
RUN echo 'set configs.config.server-config.ejb-container.max-pool-size=500' >> $POSTBOOT_COMMANDS
RUN echo 'set configs.config.server-config.ejb-container.steady-pool-size=50' >> $POSTBOOT_COMMANDS

# Autodeploy the project
COPY --from=maven ./target/*.war $DEPLOY_DIR/
