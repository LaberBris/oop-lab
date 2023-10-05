package edu.hitsz.template;

import edu.hitsz.SwingGUI.RankList;
import edu.hitsz.application.Game;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.factory.EnemyFactory.BossEnemyFactory;
import edu.hitsz.factory.EnemyFactory.EliteEnemyFactory;
import edu.hitsz.factory.EnemyFactory.MobEnemyFactory;
import edu.hitsz.rankingList.PrintRankingList;
import edu.hitsz.thread.MusicThread;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class EasyGame extends Game {
    private double[] ratioArray = {0.6, 0.8, 1.0};
    private double[] enemyHPs = {20, 40, 120};
    private double enhancement = 1;
    private int enemyMaxNumber = 5;

    @Override
    public double[] beHarderEnemyHp(double[] enemyHp) {
        for(int i=0; i<enemyHp.length; i++) {
            enemyHp[i] *= enhancement;
        }
        return enemyHp;
    }

    @Override
    public int beHarderEnemyNumber() {
        return this.enemyMaxNumber;
    }

    @Override
    public void degreeDefaultSet() {
        setSpawnBoss(false);
        setSpawnArray(ratioArray);
        setCycleDuration(600);
        setEnemyHP(enemyHPs);
    }
}
