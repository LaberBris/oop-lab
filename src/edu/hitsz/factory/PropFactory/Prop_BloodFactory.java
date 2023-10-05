package edu.hitsz.factory.PropFactory;

import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.porp.BaseProp;
import edu.hitsz.porp.Prop_Blood;

public class Prop_BloodFactory implements PropFactory{
    @Override
    public BaseProp spawnProp(double locX, double locY) {
        return new Prop_Blood(
                (int) locX,
                (int) locY,
                0,
                5);
    }
}
