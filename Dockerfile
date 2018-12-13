FROM openjdk:8-jre-alpine
RUN apk --update add tzdata
RUN cp /usr/share/zoneinfo/Europe/London /etc/localtime
RUN echo "Europe/London" > /etc/timezone
MAINTAINER cryptoentity <matt@mjad.org>

RUN mkdir /testVertx
COPY target /testVertx
WORKDIR /testVertx
CMD java -jar vertxcfgmap-0.1-fat.jar -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.Log4jLogDelegateFactory run org.mjad.util.k8s.vertxcfgmap.MainVerticle
