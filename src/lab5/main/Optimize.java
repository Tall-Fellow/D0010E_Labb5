package lab5.main;
import java.util.ArrayList;
import java.util.Random;
import lab5.events.StartEvent;
import lab5.K;
import lab5.events.ClosingEvent;
import lab5.events.StopEvent;
import lab5.sim.*;
import lab5.store.Customer;
import lab5.store.StoreState;

/**
 * This class is used to optimize the operations of a simulated environment.
 * It runs simulations to find the optimal amount of checkouts while the other parameters is set.
 * @author Markus Blomqvist.
 */
public class Optimize {	
    public static void main(String[] args) {
        Optimize opt = new Optimize();
        long SEED = K.SEED;

        // testar
        System.out.println("Minsta antal kassor som ger minimalt antal missade kunder (" +
        		opt.metod2(SEED).get(0) + "): " + opt.metod2(SEED).get(1));
        System.out.println(opt.metod3(SEED));
    }

    public StoreState metod1(long SEED, int CHECKOUTS, int MAX_CUSTOMERS,
                      double lowerGather, double upperGather, double ARRIVAL_SPEED,
                      double lowerRegister, double upperRegister,  double SIM_TIME){

        StoreState model = new StoreState(
                SEED,
                CHECKOUTS,
                MAX_CUSTOMERS,
                ARRIVAL_SPEED,
                lowerGather, upperGather,
                lowerRegister, upperRegister);

        EventQueue queue = new EventQueue();
        queue.addEvent(new StopEvent(model, queue));
        queue.addEvent(new ClosingEvent(model, queue, SIM_TIME));
        queue.addEvent(new StartEvent(model, queue));

        Simulator sim = new Simulator(model, queue);
        sim.run();

        return model;
    }

    public ArrayList<Integer> metod2(long SEED){
        // The first method's parameters.
        int MAX_CUSTOMERS = K.M;
        double SIM_TIME = K.END_TIME;
        double ARRIVAL_SPEED = K.L;
        double lowerGather = K.LOW_COLLECTION_TIME;
        double upperGather = K.HIGH_COLLECTION_TIME;
        double lowerRegister = K.LOW_PAYMENT_TIME;
        double upperRegister = K.HIGH_PAYMENT_TIME;

        // The minimal amount of checkouts can not be bigger than MAX_CUSTOMERS.
        int checkouts = MAX_CUSTOMERS;
        StoreState initStore = metod1(SEED, checkouts, MAX_CUSTOMERS,
              lowerGather, upperGather, ARRIVAL_SPEED,
              lowerRegister, upperRegister, SIM_TIME);

        int optimizedCheckouts = 0;
        int optimizedCustomers = 0;
        ArrayList<Integer> values = new ArrayList<Integer>();
        
        while (checkouts >= 1)
        {
        	StoreState newStore = metod1(SEED, checkouts, MAX_CUSTOMERS,
                    lowerGather, upperGather, ARRIVAL_SPEED,
                    lowerRegister, upperRegister, SIM_TIME);
        	
            if (newStore.getMissedCustomers() <= initStore.getMissedCustomers())
            {
            	optimizedCheckouts = newStore.getcheckOuts();
            	optimizedCustomers = newStore.getMissedCustomers();
            }
            
        	checkouts--;
        }
        values.add(0, optimizedCustomers);
        values.add(1, optimizedCheckouts);
        return values;
    }

    public int metod3(long SEED){
        // Sets a random seed number.
        Random rand = new Random(SEED);
        int maxMinCheckouts = 0;
        int counter = 0;
        
        // Det undersöks om det minsta antal som returneras (från metod2) är högre än tidigare och i
        // så fall sparas det som nytt högsta minsta antal.

        // Runs 100 times if the maximum of the minimum number of checkouts has not changed.
        while(counter < 100){

           // Creates a new amount of checkouts by sending in a new random SEED into the second method.
           int newCheckouts = metod2(rand.nextLong()).get(1);
           
           // If true, the counter resets. If false then the counter counts up by 1.
           if(maxMinCheckouts < newCheckouts){
              maxMinCheckouts = newCheckouts;
              counter = 0;
           }
           else{
        	   counter++;
           }
        }

        return maxMinCheckouts;
     }
}
