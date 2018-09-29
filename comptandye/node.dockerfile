FROM ubuntu:18.04

RUN apt-get update && apt-get install -y openssh-server openjdk-8-jdk
RUN mkdir /var/run/sshd

RUN echo 'root:payara' |chpasswd

RUN sed -ri 's/^#?PermitRootLogin\s+.*/PermitRootLogin yes/' /etc/ssh/sshd_config
RUN sed -ri 's/UsePAM yes/#UsePAM yes/g' /etc/ssh/sshd_config

RUN mkdir /root/.ssh

RUN apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# EXPOSE 22 5000-6000 20000-30000
EXPOSE 1-65000/udp 1-65000/tcp

CMD    ["/usr/sbin/sshd", "-D"]
