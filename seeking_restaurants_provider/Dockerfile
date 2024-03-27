FROM openjdk:20
WORKDIR application
COPY temp/seeking_restaurants_provider.main.jar ./app.jar
ENTRYPOINT ["java","-jar","app.jar"]