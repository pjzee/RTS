package nl.rug.oop.rts;

/**
 * Interface for subjects that need to be observed (model).
 */
public interface Subject {
    void addObserver(Observer observer);

    void updateObservers();
}
