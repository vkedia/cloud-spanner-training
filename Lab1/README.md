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
java -jar target/Lab1-1.0-SNAPSHOT-jar-with-dependencies.jar account-balance <account-id>
java -jar target/Lab1-1.0-SNAPSHOT-jar-with-dependencies.jar deposit <account-id> <amount>
java -jar target/Lab1-1.0-SNAPSHOT-jar-with-dependencies.jar withdraw <account-id> <amount>
java -jar target/Lab1-1.0-SNAPSHOT-jar-with-dependencies.jar customer-balance <customer-id>
java -jar target/Lab1-1.0-SNAPSHOT-jar-with-dependencies.jar last-transactions <customer-id> <n>
java -jar target/Lab1-1.0-SNAPSHOT-jar-with-dependencies.jar add-interest
java -jar target/Lab1-1.0-SNAPSHOT-jar-with-dependencies.jar bank-balance
```
