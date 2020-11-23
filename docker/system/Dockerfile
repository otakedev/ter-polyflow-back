FROM tomcat:latest

WORKDIR /usr/local/tomcat

COPY ./docker/system/polyflow-backend.war ./webapps/ROOT.war

ENV CATALINA_OPTS="-Dspring.profiles.active=docker"
ENV JAVA_OPTS="-Dspring.profiles.active=docker"

EXPOSE 3000

CMD ["catalina.sh", "run"]