package lab5.store;

import lab5.events.ArrivalEvent;
import lab5.events.StopEvent;
import lab5.sim.Event;
import lab5.sim.SimState;

import java.util.ArrayList;

/**
 * Has the state of the store, all relevant information generated by the events
 * is stored here.
 *
 * @author Pontus Eriksson Jirbratt,
 * @author Lucas Pettersson,
 * @author Jesper Johansson Oskarsson,
 * @author Markus Blomqvist
 *
 */

public class StoreState extends SimState {
	private final long SEED;
	private final int MAX_CUSTOMERS;
	private final int CHECKOUTS;
	private final double ARRIVAL_SPEED;
	private final double LOWER_GATHER;
	private final double UPPER_GATHER;
	private final double LOWER_CHECKOUT;
	private final double UPPER_CHECKOUT;

	private int missedCustomers;
	private int purchases;
	private int availableCheckouts;
	private int totCustomers;
	private double totQueueTime;
	private double emptyCheckoutTime;
	private double totTimeAtCheckout;
	private boolean isOpen = false;

	private Time timeFactory;
	private CreateCustomer customerFactory;
	private ArrayList<Customer> customerList;
	private FIFO checkoutQueue;

	/**
	 * initializes the store state, sets number of checkouts, max customers and
	 * creates objects for time and checkout queue
	 * 
	 * @param SEED          a seed value for the timeFactory
	 * @param CHECKOUTS     The number of checkouts in the store
	 * @param MAX_CUSTOMERS The maximum number of customers that can be inside the
	 *                      store
	 * @param ARRIVAL_SPEED a constant for the timeFactory
	 * @param lowerGather   the lower range for the time of gather events
	 * @param upperGather   the upper range for the time of gather events
	 * @param lowerCheckout the lower range for the time of checkout queues
	 * @param upperCheckout the upper range for the time of checkout queues
	 */
	public StoreState(long SEED, int CHECKOUTS, int MAX_CUSTOMERS, double ARRIVAL_SPEED, double lowerGather,
			double upperGather, double lowerCheckout, double upperCheckout) {
		super();
		this.MAX_CUSTOMERS = MAX_CUSTOMERS;
		this.CHECKOUTS = CHECKOUTS;
		availableCheckouts = CHECKOUTS;
		timeFactory = new Time(this, SEED, lowerCheckout, upperCheckout, lowerGather, upperGather, ARRIVAL_SPEED);
		customerList = new ArrayList<>();
		customerFactory = new CreateCustomer();
		checkoutQueue = new FIFO();

		this.SEED = SEED;
		this.ARRIVAL_SPEED = ARRIVAL_SPEED;

		LOWER_CHECKOUT = lowerCheckout;
		UPPER_CHECKOUT = upperCheckout;
		LOWER_GATHER = lowerGather;
		UPPER_GATHER = upperGather;
	}

	/**
	 * Updates the total checkout and queue free time, then updates the view and
	 * saves the last called event
	 * 
	 * @param e The event that called update
	 */
	public void update(Event e) {
		_updateCheckoutFreeTime(e);
		_updateTotQueueTime(e);
		super.update(e);
		setLastEvent(e);
	}

	/**
	 * Updates total queue time
	 *
	 * @param e the current event
	 */
	private void _updateTotQueueTime(Event e) {
		try {
			totQueueTime += (e.getTime() - getLastEvent().getTime()) * checkoutQueue.size();
		}

		catch (NullPointerException exception) {
			totQueueTime = 0;
		}
	}

	/**
	 * Updates the total checkout free time
	 *
	 * @param e the current event
	 */
	private void _updateCheckoutFreeTime(Event e) {
		double currentTime = e.getTime();
		if (e.getClass() != StopEvent.class) {
			if (!(!isOpen() && e.getClass() == ArrivalEvent.class) && checkAvailableCheckout()) {
				try {
					emptyCheckoutTime += (currentTime - getLastEvent().getTime()) * availableCheckouts;
					setTimeAtCheckout(currentTime);
				}

				catch (NullPointerException exception) {
					emptyCheckoutTime = 0;
				}
			}
		}
	}

	/**
	 *
	 * @return the total free checkout time
	 */
	public double getCheckoutFreeTime() {
		return emptyCheckoutTime;
	}

	/**
	 * checks if a checkout is available
	 * 
	 * @return true if available false if occupied
	 */
	public boolean checkAvailableCheckout() {
		return availableCheckouts > 0;
	}

	/**
	 * Occupies a checkout
	 */
	public void occupiedCheckout() {
		availableCheckouts--;
	}

	/**
	 * Makes a checkout available after a customer has used it.
	 */
	public void emptyCheckout() {
		availableCheckouts++;
	}

	/**
	 * Increases the number of missed customers.
	 */
	public void missedCustomers() {
		missedCustomers++;
	}

	/**
	 * Checks if store is full
	 *
	 * @return true if store is full, else false
	 */
	public boolean isFull() {
		return MAX_CUSTOMERS == customerList.size();
	}

	/**
	 * @return true if the store is open false if its closed
	 */
	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * toggle if the store is open or closed
	 */
	public void toggleIsOpen() {
		isOpen = !isOpen;
	}

	/**
	 * number of purchase events same as number of customers who has gone trough
	 * checkouts
	 */
	public void updatePurchaseCount() {
		purchases++;
	}

	/**
	 * @return number of customers in the store
	 */
	public int getCustomers() {
		return customerList.size();
	}

	/**
	 * @return number of open checkouts
	 */
	public int getCHECKOUTS() {
		return CHECKOUTS;
	}

	/**
	 * @return number of available checkouts
	 */
	public int getAvailableCheckouts() {
		return availableCheckouts;
	}

	/**
	 * @return number of customers visiting the store
	 */
	public int getMaxCustomers() {
		return MAX_CUSTOMERS;
	}

	/**
	 * @return number of customers missed due to a full store
	 */
	public int getMissedCustomers() {
		return missedCustomers;
	}

	/**
	 * @return a list of customers inside the store
	 */
	public ArrayList<Customer> getCustomerList() {
		return customerList;
	}

	/**
	 * @return the Time object
	 */
	public Time getTimeFactory() {
		return timeFactory;
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
	public double getArrivalSpeed() {
		return ARRIVAL_SPEED;
	}

	/**
	 * @return the lower bound of gather events time
	 */
	public double getGatherLower() {
		return LOWER_GATHER;
	}

	/**
	 * @return the upper bound of gather events time
	 */
	public double getGatherUpper() {
		return UPPER_GATHER;
	}

	/**
	 * @return the lower bound of purchase events time
	 */
	public double getCheckoutLower() {
		return LOWER_CHECKOUT;
	}

	/**
	 * @return the upper bound of purchase events time
	 */
	public double getCheckoutUpper() {
		return UPPER_CHECKOUT;
	}

	/**
	 * @return the constant for the time complexity SEED
	 */
	public long getSEED() {
		return SEED;
	}

	/**
	 *
	 * @return total payments
	 */
	public int getPurchases() {
		return purchases;
	}

	/**
	 *
	 * @return total queue time
	 */
	public double getTotQueueTime() {
		return totQueueTime;
	}

	/**
	 *
	 * @return total amount of customers
	 */
	public int getTotCustomers() {
		return totCustomers;
	}

	/**
	 *
	 * @return the CreateCustomer object used to create new customers
	 */
	public CreateCustomer getCustomerFactory() {
		return customerFactory;
	}

	/**
	 * Increments total customers counter
	 */
	public void incrementCustomers() {
		totCustomers++;
	}

	/**
	 *
	 * @return the total time of free checkouts
	 */
	public double getTimeAtCheckout() {
		return totTimeAtCheckout;
	}

	/**
	 *
	 * @param timeAtCheckout the total time of free checkouts
	 */
	public void setTimeAtCheckout(double timeAtCheckout) {
		this.totTimeAtCheckout = timeAtCheckout;
	}
}
