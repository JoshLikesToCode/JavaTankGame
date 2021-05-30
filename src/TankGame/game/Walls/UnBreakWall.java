package TankGame.game.Walls;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UnBreakWall extends Wall {
    int x, y;
    BufferedImage wallImage;
    private Rectangle hitBox;

    public UnBreakWall(int x, int y, BufferedImage wallImage) {
        this.x = x;
        this.y = y;
        this.wallImage = wallImage;
        this.hitBox = new Rectangle(x,y, wallImage.getWidth(), wallImage.getHeight());
    }

    public Rectangle getHitBox() {
        return hitBox.getBounds();
    }

    @Override
    public void drawImage(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.wallImage, x, y, null);
        //g2.setColor(Color.CYAN);
        //g2.drawRect(x, y, this.wallImage.getWidth(), this.wallImage.getHeight());
    }
}
