package edu.hitsz.observer;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.Game;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.porp.BaseProp;

import java.util.List;

public class EnemyList implements Subscriber{
    private List<AbstractAircraft> enemyAircrafts;
    private List<BaseBullet> enemyBullets;
    public EnemyList(List<AbstractAircraft> enemyAircrafts, List<BaseBullet> enemyBullets){
        this.enemyAircrafts = enemyAircrafts;
        this.enemyBullets = enemyBullets;
    }

    @Override
    public void update() {
        for(AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (!(enemyAircraft instanceof BossEnemy)) {
                enemyAircraft.decreaseHp(99999);
            } else {
                enemyAircraft.decreaseHp(80);
            }

        }
    }
}
