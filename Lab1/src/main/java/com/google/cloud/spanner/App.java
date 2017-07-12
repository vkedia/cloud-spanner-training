package com.google.cloud.spanner;


/**
 * Lab 1.
 */
public class App {

  private static final String INSTANCE_ID = "test-instance";
  private static final String DATABASE_ID = "example-db";

  static double getAccountBalance(DatabaseClient client, long accountId) {
    return 0;
  }

  static double getCustomerBalance(DatabaseClient client, long customerId) {
    return 0;
  }

  static void depositMoney(DatabaseClient client, long accountId, double amount) {
  }

  /**
   * Returns false if money could not be withdrawn due to insufficient balance.
   */
  static boolean withdrawMoney(DatabaseClient client, long accountId, double amount) {
    return false;
  }

  public static void main(String[] args ) {
    if (args.length < 2) {
      System.out.println("Insufficient number of arguments");
      System.out.println("Usage:");
      System.out.println("<command> account-balance <account-id>");
      System.out.println("<command> customer-balance <customer-id>");
      System.out.println("<command> deposit <account-id> <amount>");
      System.out.println("<command> withdraw <account-id> <amount>");
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
          System.out.println(getAccountBalance(client, accountId));
          break;
        case "customer-balance":
          long customerId = Long.parseLong(args[1]);
          System.out.println(getCustomerBalance(client, customerId));
          break;
        case "deposit":
          accountId = Long.parseLong(args[1]);
          double amount = Double.parseDouble(args[2]);
          depositMoney(client, accountId, amount);
          break;
        case "withdraw":
          accountId = Long.parseLong(args[1]);
          amount = Double.parseDouble(args[2]);
          boolean success = withdrawMoney(client, accountId, amount);
          if (!success) {
            System.out.println("Insufficient balance");
          }
          break;
        default:
          System.out.println("Unrecognized command: " + command);
      }
    } finally {
      spanner.close();
    }
  }
}
