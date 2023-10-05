package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.strategy.ShootStrategy;

import java.util.List;

public class ShootContext {
    private ShootStrategy shootStrategy;

    public ShootContext(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public List<BaseBullet> excuteStrategy(AbstractAircraft aircraft, int direction, int shootNum, int power) {
        return shootStrategy.shootbullet(aircraft, direction, shootNum, power);
    }

}
