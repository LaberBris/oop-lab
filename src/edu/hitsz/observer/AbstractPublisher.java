package edu.hitsz.observer;

import java.util.ArrayList;

public abstract class AbstractPublisher {
    protected ArrayList subscribers = new ArrayList();

    public void attach(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public abstract void vertifyAll();
}
