package com.google.cloud.spanner;

import java.util.List;

import com.google.cloud.spanner.Lab2.Transaction;

/**
 * Lab 1.
 */
public class App {

  private static final String INSTANCE_ID = "test-instance";
  private static final String DATABASE_ID = "example-db";

  public static void main(String[] args ) {
    if (args.length < 2) {
      System.out.println("Insufficient number of arguments");
      printUsage();
      return;
    }
    SpannerOptions options = SpannerOptions.newBuilder().build();
    Spanner spanner = options.getService();
    try {
      DatabaseId db = DatabaseId.of(options.getProjectId(), INSTANCE_ID, DATABASE_ID);
      DatabaseClient client = spanner.getDatabaseClient(db);
      String command = args[0];
      switch (command) {
        case "account-balance":
          long accountId = Long.parseLong(args[1]);
          System.out.println(new Lab1().getAccountBalance(client, accountId));
          break;
        case "deposit":
          accountId = Long.parseLong(args[1]);
          long amount = Long.parseLong(args[2]);
          new Lab1().depositMoney(client, accountId, amount);
          break;
        case "withdraw":
          accountId = Long.parseLong(args[1]);
          amount = Long.parseLong(args[2]);
          boolean success = new Lab1().withdrawMoney(client, accountId, amount);
          if (!success) {
            System.out.println("Insufficient balance");
          }
          break;
        case "customer-balance":
          long customerId = Long.parseLong(args[1]);
          System.out.println(new Lab2().getCustomerBalance(client, customerId));
          break;
        case "last-transactions":
        	accountId = Long.parseLong(args[1]);
        	int n = Integer.parseInt(args[2]);
        	List<Transaction> transactions = new Lab2().getLastNTransactions(client, accountId, n);
        	for (Transaction transaction : transactions) {
        		System.out.printf("%s %s %d", transaction.transactionTime, transaction.memo, transaction.amountInCents);
        	}
        default:
          System.out.println("Unrecognized command: " + command);
          printUsage();
      }
    } finally {
      spanner.close();
    }
  }
  
  private static void printUsage() {
	  System.out.println("Usage:");
      System.out.println("<command> account-balance <account-id>");
      System.out.println("<command> deposit <account-id> <amount>");
      System.out.println("<command> withdraw <account-id> <amount>");
      System.out.println("<command> customer-balance <customer-id>");
      System.out.println("<command> last-transactions <account-id> <n>");
  }
}
