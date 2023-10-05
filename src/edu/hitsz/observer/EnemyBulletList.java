package edu.hitsz.observer;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public class EnemyBulletList implements Subscriber{
    private List<AbstractAircraft> enemyAircrafts;
    private List<BaseBullet> enemyBullets;
    public EnemyBulletList(List<AbstractAircraft> enemyAircrafts, List<BaseBullet> enemyBullets){
        this.enemyAircrafts = enemyAircrafts;
        this.enemyBullets = enemyBullets;
    }

    @Override
    public void update() {
        enemyBullets.removeAll(enemyBullets);
    }
}
