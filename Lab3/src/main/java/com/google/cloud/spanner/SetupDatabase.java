package com.google.cloud.spanner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.cloud.Timestamp;
import com.google.cloud.WaitForOption;
import com.google.spanner.admin.database.v1.CreateDatabaseMetadata;

/**
 * Sets up the database with data for Lab3.
 *
 */
public class SetupDatabase 
{

	private static final String[] STATEMENTS = new String[]{
			"CREATE TABLE Customers (CustomerId INT64 NOT NULL, LastName STRING(MAX) NOT NULL, FirstName STRING(MAX) NOT NULL,) PRIMARY KEY(CustomerId)",
			"CREATE TABLE Accounts (CustomerId INT64 NOT NULL, AccountId INT64 NOT NULL, CreationTime TIMESTAMP NOT NULL,Type INT64 NOT NULL,Balance INT64 NOT NULL,) PRIMARY KEY(CustomerId, AccountId),INTERLEAVE IN PARENT Customers ON DELETE CASCADE",
			"CREATE TABLE Transactions (AccountId INT64 NOT NULL,TransactionTime TIMESTAMP NOT NULL,Amount INT64 NOT NULL,Memo STRING(MAX),) PRIMARY KEY(AccountId, TransactionTime)",
			"CREATE INDEX TransactionsByAmount ON Transactions(Amount DESC)"
	};
   private static final Long SAVINGS = 1L;
   private static final Long CHECKING = 0L;
   
   private void createCustomer(DatabaseClient client, long customerId) {
	   Mutation mutation = Mutation.newInsertBuilder("Customers")
			   .set("CustomerId").to(customerId)
			   .set("FirstName").to("Vikas")
			   .set("LastName").to("Kedia")
			   .build();
	   client.write(Arrays.asList(mutation));
   }
   
   private void addAccount(DatabaseClient client, long customerId, long accountId, long type) {
	   Mutation mutation = Mutation.newInsertBuilder("Accounts")
			   .set("CustomerId").to(customerId)
			   .set("AccountId").to(accountId)
			   .set("Type").to(type)
			   .set("CreationTime").to(Timestamp.parseTimestamp("2014-01-01T00:00:00Z"))
			   .set("Balance").to(0)
			   .build();
	   client.write(Arrays.asList(mutation));
   }
   
   private void addTransactions(DatabaseClient client, long customerId, long accountId,
		   int numTransactions) {
	   Random random = new Random();
	   long seconds = Instant.parse("2014-01-01T00:00:00Z").getEpochSecond();
	   int totalTransactions = 0;
	   while (totalTransactions < numTransactions) {
		   int currentBalance = (int) client.singleUse()
				   .readRow("Accounts", Key.of(customerId, accountId), Arrays.asList("Balance")).getLong(0);
		   List<Mutation> mutations = new ArrayList<>();
		   int numMutations = Math.min(1000, numTransactions - totalTransactions);
		   totalTransactions += numMutations;
		   for (int i = 0; i < numMutations; i++) {
			   long amount = 0;
			   String memo = "";    
			   if (random.nextBoolean() && currentBalance > 0) {
				   // Debit
				   amount = -1 * random.nextInt(currentBalance);
				   memo = random.nextBoolean() ? "ATM withdrawal" : "Cheque withdrawal";
			   } else {
				   // Credit
				   amount = random.nextInt(100000);
				   memo = random.nextBoolean() ? "Cash deposit" : "Cheque deposit";
			   }
			   currentBalance += amount;
			   Mutation mutation = Mutation.newInsertBuilder("Transactions")
					   .set("AccountId").to(accountId)
					   .set("Amount").to(amount)
					   .set("TransactionTime").to(Timestamp.ofTimeSecondsAndNanos(seconds, 0))
					   .set("Memo").to(memo)
					   .build();
			   mutations.add(mutation);
			   seconds += 2 * 3600;
		   }
		   mutations.add(Mutation.newUpdateBuilder("Accounts")
				   .set("CustomerId").to(customerId)
				   .set("AccountId").to(accountId)
				   .set("Balance").to(currentBalance)
				   .build());
		   client.write(mutations);
	   }
   }

   private void recreateDb(DatabaseId db, DatabaseAdminClient client) {
	   String instanceId = db.getInstanceId().getInstance();
	   String databaseId = db.getDatabase();
	   try {
		   client.dropDatabase(instanceId, databaseId);
	   } catch (SpannerException e) {
		   if (e.getErrorCode() != ErrorCode.NOT_FOUND) {
			   throw e;
		   }
	   }
	   Operation<Database, CreateDatabaseMetadata> op = client.createDatabase(instanceId, databaseId,
			   Arrays.asList(STATEMENTS));
	   while (!op.isDone()) {
		   op = op.waitFor(WaitForOption.timeout(60, TimeUnit.SECONDS));
	   }
	   System.out.println(op.getResult().toString());
   }
   
   public void setupDatabase(DatabaseId db, Spanner spanner) {
	      recreateDb(db, spanner.getDatabaseAdminClient());
	      DatabaseClient client = spanner.getDatabaseClient(db);
	      Random random = new Random();
	      for (int i = 0; i < 100; i++) {
	    	  long customerId = Math.abs(UUID.randomUUID().getLeastSignificantBits());
	    	  long savingsId = Math.abs(UUID.randomUUID().getLeastSignificantBits());
	    	  long checkingId = Math.abs(UUID.randomUUID().getLeastSignificantBits());
	    	  createCustomer(client, customerId);
	    	  addAccount(client, customerId, savingsId, SAVINGS);
	    	  addAccount(client, customerId, checkingId, CHECKING);
	    	  addTransactions(client, customerId, savingsId, getNumTransactions(random));
	    	  addTransactions(client, customerId, checkingId, getNumTransactions(random));
	      }
   }
   
   public int getNumTransactions(Random random) {
	   int type = random.nextInt(10);
	   return type < 9 ? random.nextInt(500) : random.nextInt(10000);
   }
}
