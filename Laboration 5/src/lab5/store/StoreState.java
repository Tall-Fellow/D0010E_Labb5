package lab5.store;

import lab5.sim.Event;
import lab5.sim.SimState;

import java.util.ArrayList;

/**
 * Has the state of the store, all relevant information generated by the events is stored here.
 *
 * @author Pontus Eriksson Jirbratt,
 * @author Lucas Pettersson,
 * @author Jesper Johansson Oskarsson,
 * @author Markus Blomqvist
 *
 */

public class StoreState extends SimState {
	private int checkOuts;
	private int maxCheckouts;
	private int maxCustomersToday;
	private int missedCustomers;
	private int purchases;
	private int turnedAwayCustomers;
	private int availableCheckouts;
	private int occupideCheckouts;
	private final int MAXCUSTOMERS;
	private int totCustomers;

	boolean isOpen = false;

	private double totQueueTime;
	private final double ARRIVAL_SPEED;
	private final double LOWER_GATHER;
	private final double UPPER_GATHER;
	private final double LOWER_CHECKOUT;
	private final double UPPER_CHECKOUT;
	private final long SEED;

	private double emptyCheckoutTime;


	private Time timeFactory;
	private CreateCustomer customerFactory = new CreateCustomer();
	private ArrayList<Customer> customerList;
	private FIFO checkoutQueue;

	/**
	 * initializes the store state, sets number of checkouts, max customers and creates objects for time and checkout queue
	 * @param SEED a seed value for the timeFactory
	 * @param CHECKOUTS The number of checkouts in the store
	 * @param MAX_CUSTOMERS The maximum number of customers that can be inside the store
	 * @param ARRIVAL_SPEED a constant for the timeFactory
	 * @param lowerGather the lower range for the time of gather events
	 * @param upperGather the upper range for the time of gather events
	 * @param lowerCheckout the lower range for the time of checkout queues
	 * @param upperCheckout the upper range for the time of checkout queues
	 */
	public StoreState(
		long SEED,
		int CHECKOUTS,
		int MAX_CUSTOMERS,
		double ARRIVAL_SPEED,
		double lowerGather, double upperGather,
		double lowerCheckout, double upperCheckout
	) {
		super();
		MAXCUSTOMERS = MAX_CUSTOMERS;
		checkOuts = CHECKOUTS;
		availableCheckouts = CHECKOUTS;
		timeFactory = new Time(this, SEED, lowerCheckout, upperCheckout, lowerGather, upperGather, ARRIVAL_SPEED);
		customerList = new ArrayList<>();
		checkoutQueue = new FIFO();

		this.SEED = SEED;
		this.ARRIVAL_SPEED = ARRIVAL_SPEED;

		LOWER_CHECKOUT = lowerCheckout;
		UPPER_CHECKOUT = upperCheckout;
		LOWER_GATHER = lowerGather;
		UPPER_GATHER = upperGather;

	}
	
	
	/**
	 * checks if a checkout is available 
	 * @return true if available false if occupied
	 */
	public boolean checkAvailableCheckout()
	{
		if(availableCheckouts > 0) 
		{
			return true;
		}
		
		return false;
	}
	// TODO: kanske behöver multiplicera med antalet kassor
	public void createCheckoutFreeTime(double time)
	{
		double t;

		t = time - getCurrentTime()*getAvailableCheckouts();
		emptyCheckoutTime += t;
	}
	/**
	 * If a checkout is being occupide by a customer
	 */
	public void occupideCheckout()
	{
		if(availableCheckouts > 0) 
		{
			availableCheckouts--;
			occupideCheckouts++;
			//time
		}
	
	}

	public void setTotQueueTime(double totQueueTime)
	{
		this.totQueueTime += totQueueTime;
	}

	/**
	 * Makes a checkout available after a customer has used it. 
	 */
	public void emptyCheckout()
	{
		if(availableCheckouts + 1 <= checkOuts)
		{
			availableCheckouts++;
			
			if(occupideCheckouts - 1 >= 0)
				occupideCheckouts--;
			//time
		}
	}
	
	
	/**
	 * Opens checkouts
	 * @param n, number of checkouts to open
	 */
	void openCheckout(int n) 
	{
		checkOuts += n;
		
		if(checkOuts > maxCheckouts)
			maxCheckouts = checkOuts;
			
	}
	
	/**
	 * Closes checkouts so long its positive
	 * @param n, number of checkout to close
	 */
	public void loseCheckouts(int n)
	{
		if(n - checkOuts >= 0) 
			checkOuts -= n;
	}
	
	/**
	 * Increases the number of missed customers if the number of customers is over the max number of customers. 
	 * @param
	 */
	public void missedCustomers ()
	{
			missedCustomers++;
	}

	public boolean isFull() {
		return MAXCUSTOMERS == customerList.size();
	}

	/**
	 * @return true if the store is open false if its closed
	 */
	public boolean isOpen()
	{
		return isOpen;
	}

	/**
	 * toggle if the store is open or closed
	 */
	public void toggleIsOpen()
	{
		isOpen = !isOpen;
	}

	/**
	 * increment the number of customers who did not enter the store, either full store or they arrived after closing
	 */
	public void turnedAwayCustomer()
	{
		turnedAwayCustomers++;
	}

	/**
	 * number of purchase events same as number of customers who has gone trough checkouts
	 */
	public void updatePurchaseCount()
	{
		purchases++;
	}

	// Kanske borde flyttas in i customerFactory?
	public void addCustomer(Customer c)
	{
		totCustomers++;

		if(customerList.size() + 1 > maxCustomersToday)
			maxCustomersToday = customerList.size() + 1;

		customerList.add(c);
	};

	/**
	 * creates customers objects and return them
	 * @param cState
	 * @return the latest created customer
	 */
	public Customer createCustomer(CustomerState cState)
	{
		Customer c = customerFactory.createCustomer();
		//c.setState(cState);
		addCustomer(c);
		return c;
	}
	/**
	 * @return number of customers in the store
	 */
	public int getCustomers()
	{
		return customerList.size();
	}
	
	/**
	 * @return number of open checkouts
	 */
	public int getcheckOuts()
	{
		return checkOuts;
	}
	
	
	/**
	 * @return number of available checkouts
	 */
	public int getAvailableCheckouts()
	{
		return availableCheckouts;
	}
	
	/**
	 * @return number of occupied checkouts
	 */
	int getOccupideCheckouts() 
	{
		return occupideCheckouts;
	}
	
	/**
	 * @return number of customers visiting the store 
	 */
	public int getMaxCustomersToday()
	{
		return maxCustomersToday;
	}
	
	/**
	 * @return number of customers missed due to a full store
	 */
	public int getMissedCustomers()
	{
		return missedCustomers;
	}
	
	/**
	 * @return the total time of queuing
	 */
	public double gettotQueueTime()
	{
		return totQueueTime;
	}



	/**
	 * @return a list of customers inside the store
	 */
	public ArrayList<Customer> getCustomerList() {
		return customerList;
	}

	/**
	 * @return the class that "creates" time (timeFactory)
	 */
	public Time getTimeFactory() {
		return timeFactory;
	}


	/**
	 * @return the number of customers who could not enter the store
	 */
	public int getTurnedAwayCustomer() {
		return turnedAwayCustomers;
	}

	/**
	 * @return the object that represents the queue for the checkouts
	 */
	public FIFO getCheckoutQueue() {
		return checkoutQueue;
	}

	/**
	 * @return the speed constant for arrival
	 */
	public double getArrivalSpeed()
	{
		return ARRIVAL_SPEED;
	}

	/**
	 * @return the lower bound of gather events time
	 */
	public double getGatherLower()
	{
		return LOWER_GATHER;
	}

	/**
	 * @return the upper bound of gather events time
	 */
	public double getGatherUpper()
	{
		return UPPER_GATHER;
	}

	/**
	 * @return the lower bound of purchase events time
	 */
	public double getCheckoutLower()
	{
		return LOWER_CHECKOUT;
	}

	/**
	 * @return the upper bound of purchase events time
	 */
	public double getCheckoutUpper()
	{
		return UPPER_CHECKOUT;
	}

	/**
	 * @return the constant for the time complexity SEED
	 */
	public long getSEED()
	{
		return SEED;
	}

	public double getCheckoutFreeTime()
	{
		return emptyCheckoutTime;
	}

	public int getPurchases()
	{
		return purchases;
	}

	public double getTotQueueTime()
	{
		return totQueueTime;
	}

	public int getTotCustomers()
	{
		return totCustomers;
	}
}
