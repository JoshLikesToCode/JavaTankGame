package TankGame.game.Walls;

import TankGame.game.Resource;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakWall extends Wall {
    int x, y;
    BufferedImage wallImage;
    int state;  // Each time the wall is shot this should go down by one and at zero the wall should break
    private Rectangle hitBox;

    public BreakWall(int x, int y, BufferedImage wallImage) {
        this.x = x;
        this.y = y;
        this.state = 2;
        this.wallImage = wallImage;
        this.hitBox = new Rectangle(x,y, wallImage.getWidth(), wallImage.getHeight());
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Rectangle getHitBox() {
        return hitBox.getBounds();
    }

    @Override
    public void drawImage(Graphics g)
    {
        if(state == 2)
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(this.wallImage, x, y, null);
            //g2.setColor(Color.CYAN);
            //g2.drawRect(x, y, this.wallImage.getWidth(), this.wallImage.getHeight());
        } else // draw new image when wall is shot
        {
            Graphics2D g2 = (Graphics2D) g;
            wallImage = Resource.getResourceImage("shotwall");
            g2.drawImage(this.wallImage, x, y, null);
            //g2.setColor(Color.CYAN);
            //g2.drawRect(0, 0, this.wallImage.getWidth(), this.wallImage.getHeight());
        }
    }
}

