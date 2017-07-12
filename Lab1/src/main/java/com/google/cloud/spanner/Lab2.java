package com.google.cloud.spanner;

import java.util.List;

import com.google.cloud.Timestamp;

/**
 * Lab2.
 *
 */
public class Lab2 {
	
	class Transaction {
		String memo;
		long amountInCents;
		Timestamp transactionTime;
	}
	
	/**
	 * Returns the total balance across all accounts for the given customer.
	 * @return Amount in cents.
	 */
	long getCustomerBalance(DatabaseClient client, long customerId) {
		return 0;
	}
	
	/**
	 * Returns last N transactions for the given account.
	 * @param n Number of transactions to returns
	 * @return
	 */
	List<Transaction> getLastNTransactions(DatabaseClient client, long accountId, int n) {
		return null;
    }

}
