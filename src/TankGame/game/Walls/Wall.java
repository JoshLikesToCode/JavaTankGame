package TankGame.game.Walls;

import TankGame.game.GameObject;

import java.awt.*;

public abstract class Wall extends GameObject {
    private Rectangle hitBox;
    public abstract Rectangle getHitBox();
    public abstract void drawImage(Graphics g);
    public void update() {

    }
}
