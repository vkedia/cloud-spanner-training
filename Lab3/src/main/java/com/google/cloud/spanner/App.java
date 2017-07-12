package com.google.cloud.spanner;

public class App {
	
	private static final String INSTANCE_ID = "test-instance";
	private static final String DATABASE_ID = "lab3bankdb";

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Insufficient number of arguments");
			printUsage();
			return;
		}
		SpannerOptions options = SpannerOptions.newBuilder().setNumChannels(1).build();
		Spanner spanner = options.getService();
		DatabaseId db = DatabaseId.of(options.getProjectId(), INSTANCE_ID, DATABASE_ID);
		try {
			if (args[0].equals("setup")) {
				System.out.println("Setting up database");
				new SetupDatabase().setupDatabase(db, spanner);
			} else if (args[0].equals("large-transactions")) {
				new Lab3().getLargeTransactions(db, spanner);
			} else if (args[0].equals("customer-balance")) {
				long customerId = Long.parseLong(args[1]);
				new Lab3().findBalance(customerId, db, spanner);
			} else if (args[0].equals("customer-transactions")) {
				long customerId = Long.parseLong(args[1]);
				new Lab3().printTransactions(customerId, db, spanner);
			} else {
				System.err.println("Invalid command:" + args[0]);
				printUsage();
			}
		} finally {
			spanner.close();
		}

	}
	
	private static void printUsage() {
		System.err.println("Usage:");
		System.err.println("<command> setup");
		System.err.println("<command> large-transactions");
		System.err.println("<command> customer-balance <customerId>");
		System.err.println("<command> customer-transactions <customerId>");
	}

}
