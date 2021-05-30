package TankGame.game.Walls;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BlankWall extends Wall {
    int x, y;
    BufferedImage wallImage;
    public Rectangle hitBox;

    public BlankWall(int x, int y, BufferedImage wallImage) {
        this.x = x;
        this.y = y;
        this.wallImage = wallImage;
        this.hitBox = new Rectangle(0,0, 0, 0);
    }

    @Override
    public Rectangle getHitBox() {
        return hitBox.getBounds();
    }

    @Override
    public void drawImage(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.wallImage, x, y, null);
    }
}
