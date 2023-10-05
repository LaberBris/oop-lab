package edu.hitsz.observer;

import edu.hitsz.basic.AbstractFlyingObject;

import java.util.ArrayList;

public class Publisher extends AbstractPublisher{

    @Override
    public void vertifyAll() {
        for (Object subs: subscribers) {
            ((Subscriber)subs).update();
        }
    }
}
