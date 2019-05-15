#!/bin/bash

AS_ADMIN_SECURE="$AS_ADMIN --passwordfile=$PASSWORD_FILE"
echo $AS_ADMIN_SECURE

# configure the EJB container
$AS_ADMIN_SECURE set configs.config.server-config.ejb-container.pool-resize-quantity=20;
$AS_ADMIN_SECURE set configs.config.server-config.ejb-container.max-pool-size=250;
$AS_ADMIN_SECURE set configs.config.server-config.ejb-container.steady-pool-size=50;
# $AS_ADMIN_SECURE get configs.config.server-config.ejb-container;

# Configure the HTTP listeners
$AS_ADMIN_SECURE set configs.config.server-config.network-config.network-listeners.network-listener.http-listener-2.enabled=false;
$AS_ADMIN_SECURE set configs.config.server-config.network-config.network-listeners.network-listener.http-listener-1.jk-enabled=true;
$AS_ADMIN_SECURE set configs.config.server-config.network-config.protocols.protocol.http-listener-1.http.max-connections=500;
$AS_ADMIN_SECURE set configs.config.server-config.network-config.protocols.protocol.http-listener-1.http.timeout-seconds=60;
$AS_ADMIN_SECURE set configs.config.server-config.network-config.protocols.protocol.http-listener-1.http.file-cache.enabled=true;
$AS_ADMIN_SECURE set configs.config.server-config.network-config.protocols.protocol.http-listener-1.http.file-cache.max-age-seconds=36000;
$AS_ADMIN_SECURE set configs.config.server-config.network-config.transports.transport.tcp.acceptor-threads=4;
$AS_ADMIN_SECURE set configs.config.server-config.thread-pools.thread-pool.http-thread-pool.max-thread-pool-size=350;
$AS_ADMIN_SECURE set configs.config.server-config.thread-pools.thread-pool.http-thread-pool.min-thread-pool-size=25;
# Set to false for Java EE 8 : disable HTTP2
# $AS_ADMIN_SECURE set configs.config.server-config.network-config.protocols.protocol.http-listener-1.http.http2-push-enabled=false;
# $AS_ADMIN_SECURE set configs.config.server-config.network-config.protocols.protocol.http-listener-2.http.http2-push-enabled=false;
# $AS_ADMIN_SECURE set configs.config.server-config.network-config.protocols.protocol.http-listener-1.http.http2-enabled=false;
# $AS_ADMIN_SECURE set configs.config.server-config.network-config.protocols.protocol.http-listener-2.http.http2-enabled=false;

# Enable Slack notifications
#$AS_ADMIN_SECURE notification-slack-configure --enabled=true --dynamic=true --token1=T9E7DHW8Z --token2=B9PPNLS3F --token3=wuqRNOHgbvgIyAS2hxHq4fl8
#$AS_ADMIN_SECURE monitoring-slack-notifier-configure --enabled true
#$AS_ADMIN_SECURE set-monitoring-configuration --logfrequency 1 --logfrequencyunit HOURS
#$AS_ADMIN_SECURE set-monitoring-configuration --addproperty 'name=HeapMemoryUsage value=java.lang:type=Memory description=JVM_Heap_Usage' --enabled true
#$AS_ADMIN_SECURE set-monitoring-configuration --dynamic true --enabled true
