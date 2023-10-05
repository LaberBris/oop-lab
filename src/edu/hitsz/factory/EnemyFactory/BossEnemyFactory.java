package edu.hitsz.factory.EnemyFactory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.thread.MusicThread;

public class BossEnemyFactory implements  EnemyFactory{

    @Override
    public AbstractAircraft spawnEnemy(int enemyHP) {
        return new BossEnemy((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.BOSS_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                2,
                0,
                enemyHP);
    }
}
