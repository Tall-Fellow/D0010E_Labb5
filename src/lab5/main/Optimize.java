package lab5.main;

import java.util.Random;
import lab5.events.StartEvent;
import lab5.K;
import lab5.events.ClosingEvent;
import lab5.events.StopEvent;
import lab5.sim.*;
import lab5.store.StoreState;

/**
 * This class is used to optimize the operations of a simulated environment.
 * It runs simulations to find the optimal amount of checkouts while the other parameters is set.
 *
 * @author Pontus Eriksson Jirbratt,
 * @author Lucas Pettersson,
 * @author Jesper Johansson Oskarsson,
 * @author Markus Blomqvist
 *
 */

public class Optimize {
	private int missedCustomersCounter;
	
    public static void main(String[] args) {
        Optimize opt = new Optimize();
        opt.run(K.SEED);
    }

    public void run(long SEED) {
        int method2Result = _method2(SEED);
        int method3Result = _method3(SEED);

        System.out.println(
                "Minsta antal kassor som ger minimalt antal missade (" +
                missedCustomersCounter + "): " + method3Result
        );
    }

    private StoreState _method1(
        long SEED,
        int CHECKOUTS,
        int MAX_CUSTOMERS,
        double ARRIVAL_SPEED,
        double lowerGather, double upperGather,
        double lowerRegister, double upperRegister,
        double SIM_TIME
    ) {
        StoreState model = new StoreState(
                SEED,
                CHECKOUTS,
                MAX_CUSTOMERS,
                ARRIVAL_SPEED,
                lowerGather, upperGather,
                lowerRegister, upperRegister
        );

        EventQueue queue = new EventQueue();
        queue.addEvent(new StopEvent(model, queue));
        queue.addEvent(new ClosingEvent(model, queue, SIM_TIME));
        queue.addEvent(new StartEvent(model, queue));

        Simulator sim = new Simulator(model, queue);
        sim.run();

        return model;
    }

    // FIND MIN CHECKOUTS
    private int _method2(long SEED) {
        int MAX_CUSTOMERS       = K.M;
        double ARRIVAL_SPEED    = K.L;
        double lowerGather      = K.LOW_COLLECTION_TIME;
        double upperGather      = K.HIGH_COLLECTION_TIME;
        double lowerRegister    = K.LOW_PAYMENT_TIME;
        double upperRegister    = K.HIGH_PAYMENT_TIME;
        double SIM_TIME         = K.END_TIME;
        // The minimal amount of checkouts can not be bigger than MAX_CUSTOMERS.
        int checkouts = 1;
        StoreState model;
        missedCustomersCounter = K.M;

        while (checkouts < MAX_CUSTOMERS)
        {
            System.out.println("Hello");
            model = _method1(
                SEED,
                checkouts,
                MAX_CUSTOMERS,
                ARRIVAL_SPEED,
                lowerGather, upperGather,
                lowerRegister, upperRegister,
                SIM_TIME
            );

            if (model.getMissedCustomers() < missedCustomersCounter) {
                missedCustomersCounter = model.getMissedCustomers();
            }

            else if (model.getMissedCustomers() == missedCustomersCounter) {
                checkouts--;
                break;
            }

            checkouts++;
        }

        return checkouts;
    }

    // FIND MIN CHECKOUTS WITH DIFFERENT SEEDS
    private int _method3(long SEED){
        // Sets a random seed number.
        Random rand = new Random(SEED);
        int lowestSoFar = K.M;
        int matchCounter = 0;
        while (true) {
            int minCheckouts = _method2(rand.nextLong());
            System.out.println("Another hello");
            if (minCheckouts < lowestSoFar) {
                lowestSoFar = minCheckouts;
            }

            else if (minCheckouts == lowestSoFar) {
                matchCounter++;
            }

            if (matchCounter == 100) {
                return lowestSoFar;
            }
        }
     }
}
