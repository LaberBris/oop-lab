package edu.hitsz.factory.PropFactory;

import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.porp.BaseProp;
import edu.hitsz.porp.Prop_Bomb;

public class Prop_BombFactory implements PropFactory{
    @Override
    public BaseProp spawnProp(double locX, double locY) {
        return new Prop_Bomb(
                (int) locX,
                (int) locY,
                0,
                5);
    }
}
