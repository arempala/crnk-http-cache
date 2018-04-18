#Http Caching with Crnk
This PoC uses a native aspectj to implement http caching for Crnk.
this version uses load-time-weaving - LTW

mvn clean install

java -javaagent:/c/projects/dev/.m2/repository/org/aspectj/aspectjweaver/1.9.0/aspectjweaver-1.9.0.jar -jar target/*.jar


http://localhost:8080/v1/accounts