package edu.hitsz.porp;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.observer.*;
import edu.hitsz.porp.BaseProp;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.thread.MusicThread;

import java.util.List;

public class Prop_Bomb extends BaseProp{
    public Prop_Bomb(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }
    @Override
    public void func(HeroAircraft heroAircraft, List<AbstractAircraft> enemies, List<BaseBullet> enemyBullet){
        System.out.println("Bomb!");

        AbstractPublisher abstractPublisher = new Publisher();
        Subscriber sub1, sub2;

        sub1 = new EnemyBulletList(enemies, enemyBullet);
        sub2 = new EnemyList(enemies, enemyBullet);

        abstractPublisher.attach(sub1);
        abstractPublisher.attach(sub2);

        abstractPublisher.vertifyAll();
    }
}
