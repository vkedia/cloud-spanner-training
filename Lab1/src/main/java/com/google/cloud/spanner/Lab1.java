package com.google.cloud.spanner;

public class Lab1 {

	/**
	 * Returns the balance for the given account.
	 */
	double getAccountBalance(DatabaseClient client, long accountId) {
		return 0;
	}

	/**
	 * Deposits money into the given account.
	 * @param amount Amount to deposit in cents.
	 */
	void depositMoney(DatabaseClient client, long accountId, long amountInCents) {
	}

	/**
	 * Withdraws money from the given account. Returns false if money could not be withdrawn
	 * due to insufficient balance.
	 */
	boolean withdrawMoney(DatabaseClient client, long accountId, long amountInCents) {
		return false;
	}
}
