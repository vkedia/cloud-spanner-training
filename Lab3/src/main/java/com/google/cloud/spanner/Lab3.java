package com.google.cloud.spanner;


/**
 * Lab3.
 */
public class Lab3 {
	
	/**
	 * Find all the transactions bigger than a certain amount. Also prints the query statistics
	 * which include elapsed_time.
	 * Task: Analyze the query plan in the UI and get elapsed_time under 10 milliseconds.
	 */
	public void getLargeTransactions(DatabaseId db, Spanner spanner) {
		String query = "select AccountId, Memo from Transactions where Amount > 99500";
		DatabaseClient client = spanner.getDatabaseClient(db);
		try (ResultSet rs = client.singleUse()
				.analyzeQuery(Statement.of(query), ReadContext.QueryAnalyzeMode.PROFILE)) {
			while (rs.next()) {
				System.out.printf("%d:%s\n", rs.getLong(0), rs.getString(1));
			}
			System.out.println(rs.getStats().getQueryStats().toString());
		}
	}
	
	/**
	 * Finds the balance for a given customer.
	 * Task: This function can be called multiple times for different customer ids.
	 * Optimise this function to make it run faster.
	 */
    public void findBalance(long customerId,DatabaseId db, Spanner spanner) {
    	String query = "select sum(Balance) from Accounts where"
    			+ "CustomerId=" + customerId;
    	try (ResultSet rs = spanner.getDatabaseClient(db).singleUse()
				.executeQuery(Statement.of(query))) {
			if (rs.next()) {
				System.out.printf("%d\n", rs.getLong(0));
			}
		}
    }
    
    /**
     * Finds all transactions for a given customer between some time period.
     * Task: Analyze the query plan in the UI and make the query execute faster.
     */
    public void printTransactions(long customerId, DatabaseId db, Spanner spanner) {
    	String query = "select Amount"
    			+ " from Transactions JOIN@{JOIN_TYPE=HASH_JOIN} Accounts"
    			+ " ON Transactions.AccountId=Accounts.AccountId"
    			+ " where TransactionTime < TIMESTAMP(\"2016-01-01\")"
    			+ " and TransactionTime > TIMESTAMP(\"2014-01-01\")"
    			+ " and Accounts.CustomerId=@id";
    	Statement stmnt = Statement.newBuilder(query).bind("id").to(customerId).build();
    	try (ResultSet rs = spanner.getDatabaseClient(db).singleUse()
				.analyzeQuery(stmnt, ReadContext.QueryAnalyzeMode.PROFILE)) {
    		while (rs.next()) {
    			System.out.println(rs.getLong(0));
    		}
    		System.out.println(rs.getStats().getQueryStats().toString());
    	}
    }

}
