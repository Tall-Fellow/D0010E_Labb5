package lab5.sim;
import java.util.Observable;
import java.util.Observer;
import lab5.SimState;
import lab5.SimState.StoreState;


public abstract class SimView implements Observer{
  private SimState simState;
  private StoreState storeState;
  
  public SimView(SimState simState, StoreState storeState){
    simState.addObserver(this);
    storeState.addObserver(this);
  }
  
  abstract public void update(Observable o, Object arg);
}
