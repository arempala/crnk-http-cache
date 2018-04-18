

mvn clean install

java -jar target/*.jar

http://localhost:8080/v1/accounts


# HTTP Caching
Http Caching can be enabled for any Crnk Resource/Endpoint. To do so, a response from a Crnk Resource Repository must include MetaInformation that contains both 'etag' and 'httpCacheValid' attributes. Hence, the Crnk Resource Repository is responsible for extracting 'If-None-Match' request header, and generating current 'etag' value to compare against. If they match, 'httpCacheValid' should be set to TRUE and response should be returned. See AccountRepo.findAll() for an example.