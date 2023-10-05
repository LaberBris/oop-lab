package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.strategy.ShootContext;
import edu.hitsz.strategy.ShootDivergent;
import edu.hitsz.strategy.ShootStraight;

import java.util.LinkedList;
import java.util.List;


public class BossEnemy extends AbstractAircraft{
    /**
     * BOSS敌机
     */
        /**攻击方式 */

        /**
         * 子弹一次发射数量
         */
        private int shootNum = 3;

        /**
         * 子弹伤害
         */
        private int power = 30;

        /**
         * 子弹射击方向 (向上发射：1，向下发射：-1)
         */
        private int direction = 1;

        /**
         * @param locationX BOSS敌机位置x坐标
         * @param locationY BOSS敌机位置y坐标
         * @param speedX BOSS敌机射出的子弹的基准速度
         * @param speedY BOSS敌机射出的子弹的基准速度
         * @param hp    初始生命值
         */
        public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
            super(locationX, locationY, speedX, speedY, hp);
        }

        @Override
        public void forward() {
            super.forward();
            // 判定 y 轴向下飞行出界
            if (locationY >= Main.WINDOW_HEIGHT ) {
                vanish();
            }
        }

        @Override
        /**
         * 通过射击产生子弹
         * @return 射击出的子弹List
         */
        public List<BaseBullet> shoot() {
            ShootContext shootContext = new ShootContext(new ShootDivergent());
            return shootContext.excuteStrategy(this, direction, shootNum, power);
        }
    }
