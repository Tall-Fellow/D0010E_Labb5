package lab5.store;



public class StoreState {
	int customers;
	int checkOuts;
	int maxCheckouts;
	int maxCustomerToday;
	int missedCustomers;
	final int MAXCUSTOMERS;
	
	public StoreState(int openCheckout, int maxCustomers)
	{
		MAXCUSTOMERS = maxCustomers;
		checkOuts = openCheckout;
	}
	
	/**
	 * @return number of customers in the store
	 */
	int getCustomers() 
	{
		return customers;
	}
	
	/**
	 * @return number of open checkouts
	 */
	int checkOuts() 
	{
		return checkOuts;
	}
	
	/**
	 * Opens checkouts
	 * @param n, number of checkouts to open
	 */
	void openCheckout(int n) 
	{
		checkOuts += n;
	}
	
	/**
	 * Closes checkouts so long its positive
	 * @param n, number of checkout to close
	 */
	void closeCheckouts(int n) 
	{
		if(n - checkOuts >= 0) 
			checkOuts -= n;
		
		
		/*
		for(int i = 0; i <= n; i++) 
		{
			checkOuts--;
		}
		*/
	}
	
	/**
	 * Increases the number of missed customers
	 * @param
	 */
	void missedCustomers () 
	{
		missedCustomers++;
	}

}
