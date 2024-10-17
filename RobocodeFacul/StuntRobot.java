package RobocodeFacul;

import robocode.*;
import java.awt.Color;
import java.util.Random;

public class StuntRobot extends AdvancedRobot {

    Random random = new Random();
    private String targetRobot;
    private boolean circling;

    public void run() {
        setColors(Color.LIGHT_GRAY, Color.BLACK, Color.BLACK);

        while (true) {
            setTurnGunRight(360);  
            double energy = getEnergy();
            
            if (energy > 30) {
                if (circling) {
                    double moveAmount = 100;
                    setTurnRight(90);
                    setAhead(moveAmount);
                } else {
                    setAhead(150 + random.nextInt(100));
                    setTurnRight(30 + random.nextInt(30));
                }
            } else {
                setTurnRight(90);
                setAhead(150);
            }
            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        double distance = e.getDistance();

        if (targetRobot == null || !targetRobot.equals(e.getName())) {
            targetRobot = e.getName();
            circling = true;
        }

        double gunTurn = getHeading() - getGunHeading() + e.getBearing();
        setTurnGunRight(gunTurn);

        double energy = getEnergy();
        if (energy > 30 && targetRobot.equals(e.getName())) {
            if (distance < 50) {
                fire(3);
            } else if (distance < 300) {
                fire(2);
            } else {
                fire(1);
            }
        }

        setAhead(50 + random.nextInt(50));
        setTurnRight(20);
        execute();
    }

    public void onBulletHit(BulletHitEvent e) {
        targetRobot = e.getName();
    }

    public void onBulletMissed() {
        targetRobot = null;
        circling = false;
    }

    public void onHitByBullet(HitByBulletEvent e) {
        double energy = getEnergy();
        if (energy < 30) {
            int direction = random.nextInt(2) == 0 ? 1 : -1;
            setTurnRight(direction * 90);
            setAhead(150);
        }
        execute();
    }

    public void onHitWall(HitWallEvent e) {
        back(50);
        setTurnRight(90);
        setAhead(150);
        execute();
    }

    public void onHitRobot(HitRobotEvent e) {
        double energy = getEnergy();
        if (energy < 30) {
            back(150);
        } else {
            if (e.isMyFault()) {
                int direction = random.nextInt(2);
                if (direction == 0) {
                    setTurnRight(100);
                } else {
                    setTurnLeft(100);
                }
            }
        }
        execute();
    }
}
