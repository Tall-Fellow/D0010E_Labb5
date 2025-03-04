package lab5.store;

import java.util.NoSuchElementException;
import java.util.ArrayList;

/**
 * This is a first-in first-out class and is used for the queue of customers.
 *
 * @author Pontus Eriksson Jirbratt,
 * @author Lucas Pettersson,
 * @author Jesper Johansson Oskarsson,
 * @author Markus Blomqvist
 */
public class FIFO {

	private ArrayList<Customer> FIFOQueue = new ArrayList<>();
	private int max = 0;
	private int totalQueuers;

	/**
	 * adds a customer to the queue and keeps track of the biggest size the list has
	 * been.
	 *
	 * @param customer to add to queue
	 */
	public void add(Customer customer) {
		FIFOQueue.add(customer);
		totalQueuers++;
		if (FIFOQueue.size() > max) {
			max = FIFOQueue.size();
		}
	}

	/**
	 * @throws NoSuchElementException if queue is empty
	 * @return the first customer in the queue.
	 */
	public Customer getFirst() throws NoSuchElementException {
		if (FIFOQueue.size() == 0) {
			throw new NoSuchElementException();
		}

		return FIFOQueue.get(0);
	}

	/**
	 * @throws NoSuchElementException if queue is empty removes the first customer
	 *                                in the queue.
	 */
	public void removeFirst() throws NoSuchElementException {
		if (FIFOQueue.size() == 0) {
			throw new NoSuchElementException();
		}

		FIFOQueue.remove(0);
	}

	/**
	 * @return true if the queue is empty else false
	 */
	public boolean isEmpty() {
		return FIFOQueue.size() == 0;
	}

	/**
	 * @return the current size of the queue
	 */
	public int size() {
		return FIFOQueue.size();
	}

	/**
	 * @return the queue as a string with the Customer objects in it.
	 */
	public String toString() {
		String s = "";

		for (Customer customer : FIFOQueue) {
			if (FIFOQueue.size() == 0) {
				return "[" + s + "]";
			}

			if (FIFOQueue.size() == 1) {
				return "[" + customer.toString() + "]";
			}

			if (customer == FIFOQueue.get(0)) {
				s += customer.toString();
				continue;
			}

			s = s + ", " + customer.toString();
		}

		return "[" + s + "]";
	}

	/**
	 *
	 * @return total number of Customer objects in queue
	 */
	public int getTotalQueuers() {
		return totalQueuers;
	}
}
