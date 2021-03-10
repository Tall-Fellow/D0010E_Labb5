package lab5.events;

import lab5.store.StoreState;
import lab5.store.Customer;
import lab5.sim.Event;
import lab5.sim.EventQueue;

/**
 * Description
 * 
 * @author Lucas Pettersson,
 * @author Pontus Eriksson Jirbratt, 
 * @author Jesper Johansson Oskarsson, 
 * @author Markus Blomqvist
 *
 */
public class GatherEvent extends Event {
	public GatherEvent(StoreState state, EventQueue eventQueue, Customer customer, double executionTime) {
		super(state, eventQueue);
		this.time = executionTime;
		this.name = "Plock";
		this.customer = customer;
	}

	/**
	 * execution of gatherEvent
	 * if checkouts are available it will be occupied and a purchase event will be created.
	 * else the customer will be put into a queue.
	 */
	public void run() {
		StoreState store = (StoreState) this.state;
		store.update(this);

		if (store.checkAvailableCheckout()) {
			store.createCheckoutFreeTime(time);
			store.occupideCheckout();
			eventQueue.addEvent(new PurchaseEvent(
					store,
					eventQueue,
					customer,
					store.getTimeFactory().generateRegisterTime())
			);
		}

		else {
			customer.setQueueTime(time);
			store.getCheckoutQueue().add(customer);

			if (!store.getCheckoutQueue().isEmpty()) {
				//double queueTime = store.getCheckoutQueue().getTotalQueueTime(time);
				//store.setTotQueueTime(queueTime);
			}
		}
	}
}