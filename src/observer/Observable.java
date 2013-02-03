package observer;

import java.util.ArrayList;

public abstract class Observable {
    
    protected ArrayList<Observer> observers;

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for(Observer o : this.observers) {
            o.update(null);
        }
    }
    
    public void notifyObservers(Object obj) {
        for(Observer o : this.observers) {
            o.update(obj);
        }
    }

    public void deleteObservers() {
        this.observers.clear();
    }
}
