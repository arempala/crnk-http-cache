
mvn clean install

java -javaagent:/c/projects/dev/.m2/repository/org/springframework/spring-instrument/4.3.15.RELEASE/spring-instrument-4.3.15.RELEASE.jar -javaagent:/c/projects/dev/.m2/repository/org/aspectj/aspectjweaver/1.9.0/aspectjweaver-1.9.0.jar -jar target/*.jar


http://localhost:8080/v1/accounts