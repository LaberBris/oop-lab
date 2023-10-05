package edu.hitsz.template;

import edu.hitsz.application.Game;

public class HardGame extends Game {
    private double[] ratioArray = {0.5, 1.0, 0.5};
    private double[] enemyHPs = {30, 50, 150};
    private double enhancement = 1.2;
    private int enemyMaxNumber = 9;

    @Override
    public double[] beHarderEnemyHp(double[] enemyHp) {
        for(int i=0; i<enemyHp.length; i++) {
            enemyHp[i] *= enhancement;
        }
        System.out.println("游戏变难了!\n敌机血量增加了百分之20!\n敌机数量上限增加了1!");
        return enemyHp;
    }

    @Override
    public int beHarderEnemyNumber() {
        this.enemyMaxNumber++;
        return this.enemyMaxNumber;
    }

    @Override
    public void degreeDefaultSet() {
        setSpawnBoss(true);
        setSpawnThreshold(100);
        setSpawnArray(ratioArray);
        setCycleDuration(600);
        setEnemyHP(enemyHPs);
    }
}
