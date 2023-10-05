package edu.hitsz.factory.EnemyFactory;

import edu.hitsz.aircraft.AbstractAircraft;
public interface EnemyFactory {
    public abstract AbstractAircraft spawnEnemy(int enemyHP);
}
