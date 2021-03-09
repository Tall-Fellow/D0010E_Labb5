package lab5.sim;

import java.util.Observable;

/**
 * The general state of the simulation, keep tracks of time, events and if the simulation is still running
 * @author Pontus Eriksson Jirbratt,
 * @author Lucas Pettersson,
 * @author Jesper Johansson Oskarsson,
 * @author Markus Blomqvist
 *
 */
public class SimState extends Observable {
	double currentTime;
	int numberOfEvents = 0;
	boolean runFlag;
	Event lastEvent;

	/**
	 * Constructor set the time to zero and set the flag for the program to true
	 */
	public SimState() {
		currentTime = 0;
		runFlag = true;
	}

	/**
	 * shuts down the simulation
	 */
	public void setRunflagFalse() {
		runFlag = false;
	}
	
	/**
	 * Increments number of events
	 */
	private void newEvent()
	{
		numberOfEvents++;
	}

	/**
	 * a total time of the simulation
	 * @param time the time the events needed to finnish
	 */
	public void setTime(double time) {
		currentTime = time;
	}

	/**
	 * updates the observer
	 */
	public void update(Event e)
	{
		lastEvent = e;
		newEvent();
		setChanged();
		notifyObservers();
	}

	/**
	 * @return if the program is running 
	 */
	boolean getSimRunning() 
	{
		return runFlag;
	}
	
	/**
	 * @return the current time in the program
	 */
	public double getCurrentTime()
	{
		return currentTime;
	}

	/**
	 * @return the last event to occur
	 */
	public Event getLastEvent() {
		return lastEvent;
	}
}