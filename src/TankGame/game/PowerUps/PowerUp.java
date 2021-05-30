package TankGame.game.PowerUps;

import TankGame.game.GameObject;

import java.awt.*;

public abstract class PowerUp extends GameObject {
    private Rectangle hitBox;
    public abstract Rectangle getHitBox();
    public abstract void drawImage(Graphics g);
    public void update() {
    }
}