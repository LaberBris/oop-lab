package edu.hitsz.template;

import edu.hitsz.application.Game;

public class NormalGame extends Game {
    private double[] ratioArray = {0.7, 1.0, 0.7};
    private double[] enemyHPs = {25, 45, 150};
    private double enhancement = 1.1;
    private int enemyMaxNumber = 7;

    @Override
    public double[] beHarderEnemyHp(double[] enemyHp) {
        for(int i=0; i<enemyHp.length; i++) {
            enemyHp[i] *= enhancement;
        }
        System.out.println("游戏变难了!\n敌机血量增加了十分之一!");
        return enemyHp;
    }

    @Override
    public int beHarderEnemyNumber() {
        return this.enemyMaxNumber;
    }

    @Override
    public void degreeDefaultSet() {
        setSpawnBoss(true);
        setSpawnThreshold(300);
        setSpawnArray(ratioArray);
        setCycleDuration(600);
        setEnemyHP(enemyHPs);
    }
}
