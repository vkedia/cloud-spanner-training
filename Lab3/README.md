Setup:
```
gcloud config set project [PROJECT_ID]
gcloud auth application-default login
```

Compile:
```
mvn package
```

Execute:
```
java -jar target/Lab3-1.0-SNAPSHOT-jar-with-dependencies.jar setup
java -jar target/Lab3-1.0-SNAPSHOT-jar-with-dependencies.jar large-transactions
java -jar target/Lab3-1.0-SNAPSHOT-jar-with-dependencies.jar customer-balance <customer-id>
java -jar target/Lab3-1.0-SNAPSHOT-jar-with-dependencies.jar customer-transactions <customer-id>
```
