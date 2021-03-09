package lab5.events;
import lab5.sim.*;
import lab5.store.*;

/**
 * Description
 * 
 * @author Lucas Pettersson,
 * @author Pontus Eriksson Jirbratt, 
 * @author Jesper Johansson Oskarsson, 
 * @author Markus Blomqvist
 *
 */
public class StartEvent extends Event {
	private ArrivalEvent arrivalEvent;

	public StartEvent(SimState state, EventQueue eventQueue) 
	{
		super(state, eventQueue);
		this.time = 0d;
		this.name = "Start";
		this.customer = null;
	}
	
	public void run() 
	{
		StoreState model = (StoreState) this.state;
		model.toggleIsOpen();

		double arrivalTime = model.getTimeFactory().generateArrivalTime();
		arrivalEvent = new ArrivalEvent(this.state, this.eventQueue, arrivalTime);
		eventQueue.addEvent(arrivalEvent);

		state.update(this);
	}
}
