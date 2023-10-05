package edu.hitsz.factory.PropFactory;

import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.porp.BaseProp;

public interface PropFactory {
    public abstract BaseProp spawnProp(double locX, double locY);
}
