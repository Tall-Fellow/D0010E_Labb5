package lab5.sim;

import java.util.ArrayList;

/**
 * Creates an ArrayList of events
 * Add and removes events
 * Updates the list with the class SortedSequence
 *
 * @author Pontus Eriksson Jirbratt
 */

public class EventQueue
{

   ArrayList<Event> eventQueue;

   /**
    * Constructor that takes an ArrayList of events and set the current eventQueue to it.
    *
    * @param eveQueue
    */
   private EventQueue(ArrayList<Event> eveQueue)
   {
      eventQueue = eveQueue;
   }

   /**
    * Returns the eventQueue. 
    * @return eventQueue
    */
   private ArrayList<Event> getEventQueue()
   {
      return eventQueue;
   }
   
   /**
    * Adds a new event 
    * Creates a SortedSequence object to sort it
    * Sets the current eventQueue to the sorted eventQueue
    * @param event
    */
   private void addEvent(Event event)
   {
      eventQueue.add(event);
      SortedSequence ss = new SortedSequence();
      eventQueue = ss.sortList(eventQueue);
      System.out.println(eventQueue);
   }
   
   /**
    * Fetches the event at the first position 
    * stores it locally
    * removes it from the list
    * @return the event in the first position of eventQueue
    */
   Event popNextEvent()
   {
      final int NEXTEVENT = 0;
      Event event;

      event = eventQueue.get(NEXTEVENT);
      eventQueue.remove(NEXTEVENT);

      return event;

   }
	
	/*
	public static void main(String[] args)
	{
		ArrayList<Event> list = new ArrayList<Event>();
		EventQueue eq = new EventQueue(list);
		
		eq.printTest();
		
	}
	
	void printTest() {

		for(int i = 0; i < 5; i++) {
			eventQueue.add(i, new Event());
		}
		
		for(int i = 0; i < 5; i++) {
			System.out.println("First list: " + eventQueue.get(i).time);
		}
		
		addEvent(new Event());
		
		for(int i = 0; i < 6; i++) {
			System.out.println("Sorted list:" + eventQueue.get(i).time);
		}
	}
	*/
}
