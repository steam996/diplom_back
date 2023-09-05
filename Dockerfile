FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8082
ADD target/diplom_backend-0.0.1-SNAPSHOT.jar myapp.jar
ENTRYPOINT ["java","-jar","/myapp.jar"]