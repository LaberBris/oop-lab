package edu.hitsz.porp;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.porp.BaseProp;

import java.util.List;

public class Prop_Bullet extends BaseProp{
    public Prop_Bullet(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void func(HeroAircraft heroAircraft, List<AbstractAircraft> enemies, List<BaseBullet> enemyBullet){
        System.out.println("BombSupply active!");
        heroAircraft.addShootNum(1);
    }
}
