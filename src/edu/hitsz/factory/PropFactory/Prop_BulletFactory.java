package edu.hitsz.factory.PropFactory;


import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.porp.BaseProp;
import edu.hitsz.porp.Prop_Bullet;

public class Prop_BulletFactory implements PropFactory{
    @Override
    public BaseProp spawnProp(double locX, double locY) {
        return new Prop_Bullet(
                (int) locX,
                (int) locY,
                0,
                5);
    }
}
