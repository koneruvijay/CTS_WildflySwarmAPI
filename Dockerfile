FROM openjdk:8

RUN mkdir -p /opt/vijay/WildflySwarmAPI

ADD target/CTS_WildflySwarmAPI-1.0-SNAPSHOT-swarm.jar /opt/vijay/WildflySwarmAPI/

EXPOSE 8080

CMD ["java", "-jar", "/opt/vijay/WildflySwarmAPI/CTS_WildflySwarmAPI-1.0-SNAPSHOT-swarm.jar"]
