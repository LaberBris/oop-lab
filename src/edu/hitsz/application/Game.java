package edu.hitsz.application;

import edu.hitsz.SwingGUI.RankList;
import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.factory.EnemyFactory.BossEnemyFactory;
import edu.hitsz.factory.EnemyFactory.EliteEnemyFactory;
import edu.hitsz.factory.EnemyFactory.EnemyFactory;
import edu.hitsz.factory.EnemyFactory.MobEnemyFactory;
import edu.hitsz.factory.PropFactory.PropFactory;
import edu.hitsz.factory.PropFactory.Prop_BloodFactory;
import edu.hitsz.factory.PropFactory.Prop_BombFactory;
import edu.hitsz.factory.PropFactory.Prop_BulletFactory;
import edu.hitsz.porp.BaseProp;
import edu.hitsz.porp.Prop_Bomb;
import edu.hitsz.rankingList.PrintRankingList;
import edu.hitsz.thread.MusicThread;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class Game extends JPanel {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 20;

    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<BaseProp> baseprops;


    /**
    * 工厂模式
    */
    protected EnemyFactory enemyFactory;
    AbstractAircraft abstractAircraft;

    PropFactory propFactory;
    BaseProp baseProp;

    /**
     * 屏幕中出现的敌机最大数量
     */
    private int enemyMaxNumber = 5;

    /**
     * 当前得分
     */
    private int score = 0;
    /**
     * 当前时刻
     */
    private int time = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration;
    private int cycleTime = 0;

    /**
    *  boss机是否存在
    * */
    private boolean bossenemyexist = false;

    /**
     * 游戏结束标志
     */
    private boolean gameOverFlag = false;

    /**
     * 是否生成boss机
     */
    private boolean spawnBoss;

    /**
     * Boss机生成阈值
     */
    private int spawnThreshold;



    /**
     * 敌机和道具生成比例
     */
    private double[] spawnArray = new double[3];
    private double[] enemyHP = new double[3];

    private String gameDegree;
    protected boolean bgmOn = true;

    public Game() {
        heroAircraft = HeroAircraft.getHeroAircraft();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        baseprops = new LinkedList<>();

        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    /**
     * 游戏不同难度要实现的抽象方法
     */
    public abstract void degreeDefaultSet();


    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        if (bgmOn) {
            MusicThread.setBgm(true);
            MusicThread.setBossbgm(false);
            new MusicThread("src/videos/bgm.wav").start();
        }


        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {
            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);

                // 增强难度
                if (time % 9000 == 0 && time >= 9000 ) {
                    enemyHP = beHarderEnemyHp(enemyHP);
                    enemyMaxNumber = beHarderEnemyNumber();
                }

                // 新敌机产生
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    double condition = Math.random();
                    if (condition <= spawnArray[0]) {
                        enemyFactory = new MobEnemyFactory();
                        enemyAircrafts.add(enemyFactory.spawnEnemy((int)enemyHP[0]));
                    } else if (condition > spawnArray[0] && condition < spawnArray[1]) {
                        enemyFactory = new EliteEnemyFactory();
                        enemyAircrafts.add(enemyFactory.spawnEnemy((int)enemyHP[1]));
                    }
                }
                if (spawnBoss && (score % spawnThreshold <= 30) && (!bossenemyexist) && (score > spawnThreshold)) {
                    enemyFactory = new BossEnemyFactory();
                    bossenemyexist = true;
                    enemyAircrafts.add(enemyFactory.spawnEnemy((int)enemyHP[2]));

                    if (bgmOn == true) {
                        MusicThread.setBgm(false);
                        MusicThread.setBossbgm(true);
                        new MusicThread("src/videos/bgm_boss.wav").start();
                    }
                }
                // 飞机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            //道具移动
            propsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            //每个时刻重绘界面
            repaint();

            // 游戏结束检查英雄机是否存活
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                MusicThread.setBgm(false);
                MusicThread.setBossbgm(false);

                if (bgmOn) {
                    new MusicThread("src/videos/game_over.wav").start();
                }
                MusicThread.setStop(true);


                executorService.shutdown();
                gameOverFlag = true;



                System.out.println("Game Over!");

                String username = JOptionPane.showInputDialog("Game Over!\n"+ "" + "您的分数是"+ score + "" + "\n请输入用户名:");

                PrintRankingList printRankingList = new PrintRankingList();
                printRankingList.printRankList(score, username , gameOverFlag, gameDegree);



                RankList rankList = new RankList(gameDegree);

                Main.cardPanel.add(rankList.getMainPanel());
                Main.cardLayout.last(Main.cardPanel);

            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }



    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // TODO 敌机射击
        for (AbstractAircraft enemy: enemyAircrafts) {
            enemyBullets.addAll(enemy.shoot());
        }
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());

        if (bgmOn == true) {
            new MusicThread("src/videos/bullet.wav").start();
        }


    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction() {
        for (BaseProp baseprop : baseprops) {
            baseprop.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 英雄机撞击到敌机子弹
                // 英雄机损失一定生命值

                if (bgmOn) {
                    new MusicThread("src/videos/bullet_hit.wav").start();
                }

                heroAircraft.decreaseHp(bullet.getPower());
                System.out.println(heroAircraft.getHp());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid() && enemyAircraft instanceof MobEnemy) {
                        score += 10;
                    } else if (enemyAircraft.notValid() && enemyAircraft instanceof EliteEnemy) {
                        // TODO 获得分数，产生道具补给
                        score += 20;
                        double locX = enemyAircraft.getLocationX();
                        double locY = enemyAircraft.getLocationY();
                        double cond = Math.random();
                        double condi = Math.random();
                        if (cond < spawnArray[2]) {
                            if (condi < 0.33) {
                                propFactory = new Prop_BloodFactory();
                                baseprops.add(propFactory.spawnProp(locX, locY));
                            }
                            if (condi > 0.33 && condi < 0.66) {
                                propFactory = new Prop_BombFactory();
                                baseprops.add(propFactory.spawnProp(locX, locY));
                            }
                            if (condi > 0.66) {
                                propFactory = new Prop_BulletFactory();
                                baseprops.add(propFactory.spawnProp(locX, locY));
                            }
                        }
                    } else if (enemyAircraft.notValid() && enemyAircraft instanceof BossEnemy) {
                        score += 100;
                        bossenemyexist = false;
                        double locX = enemyAircraft.getLocationX();
                        double locY = enemyAircraft.getLocationY();
                        propFactory = new Prop_BloodFactory();
                        baseprops.add(propFactory.spawnProp(locX - ImageManager.PROP_BLOOD_IMAGE.getWidth()-100, locY));
                        propFactory = new Prop_BulletFactory();
                        baseprops.add(propFactory.spawnProp(locX - ImageManager.PROP_BOMB_IMAGE.getWidth(), locY));
                        propFactory = new Prop_BombFactory();
                        baseprops.add(propFactory.spawnProp(locX - ImageManager.PROP_BULLET_IMAGE.getWidth()+100, locY));
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // Todo: 我方获得道具，道具生效
        for (BaseProp prop:baseprops) {
            if (heroAircraft.crash(prop)) {
                // 英雄机撞击到道具

                if (bgmOn) {
                    new MusicThread("src/videos/get_supply.wav").start();
                }

                if (prop instanceof Prop_Bomb) {
                    // score += 50;
                    if(bgmOn){
                        new MusicThread("src/videos/bomb_explosion.wav").start();
                    }
                }

                for(AbstractAircraft enemyAircraft : enemyAircrafts) {
                    if (enemyAircraft instanceof MobEnemy) {
                        score += 10;
                    } else if (enemyAircraft instanceof EliteEnemy) {
                        score += 20;
                    }
                }

                prop.func(heroAircraft, enemyAircrafts, enemyBullets);
                prop.vanish();
            }
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        baseprops.removeIf(AbstractFlyingObject::notValid);
    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);

        paintImageWithPositionRevised(g, enemyAircrafts);

        paintImageWithPositionRevised(g, baseprops);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("DEGREE:" + this.gameDegree, x, y);
        y = y + 20;
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }

    public void setGameDegree(String gameDegree) {
        this.gameDegree = gameDegree;
    }
    public String getGameDegree() { return gameDegree; }

    public void setBgmOn(boolean bgmOn) { this.bgmOn = bgmOn; }

    public void addScore(int scorePlus) {
        score += scorePlus;
    }

    protected void setSpawnBoss(boolean spawnBoss) {
        this.spawnBoss = spawnBoss;
    }
    protected void setSpawnThreshold(int spawnThreshold) {
        this.spawnThreshold = spawnThreshold;
    }

    protected void setSpawnArray(double[] spawnArray) {
        this.spawnArray = spawnArray;
    }

    protected void setEnemyHP(double[] enemyHP) {
        this.enemyHP = enemyHP;
    }

    protected void setCycleDuration(int cycleDuration) {
        this.cycleDuration = cycleDuration;
    }

    public double[] beHarderEnemyHp(double[] enemyHp) {
        for(int i=0; i<enemyHp.length; i++) {
            enemyHp[i] *= 1;
        }
        return enemyHp;
    }

    protected int beHarderEnemyNumber() {
        return 5;
    }
}
