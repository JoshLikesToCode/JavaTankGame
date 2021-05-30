package TankGame.game;

import TankGame.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    int x;
    int y;
    int vx;
    int vy;
    float angle;
    int R = 7; // effects speed of bullet
    BufferedImage bulletImage;
    Rectangle hitBox;
    // for the shooter of the bullet
    private Tank shooter;

    public Bullet(int x, int y, float angle, BufferedImage bulletImage, Tank tank) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.bulletImage = bulletImage;
        this.hitBox = new Rectangle(x, y, this.bulletImage.getWidth(), this.bulletImage.getHeight());
        this.shooter = tank;
    }

    public Tank getShooter()
    {
        return shooter;
    }

    @Override
    public Rectangle getHitBox() {
        return this.hitBox.getBounds();
    }

    public void moveForward()
    {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitBox.setLocation(x,y);
    }

    public void checkBorder()
    {
        if (x < 30) {
            x = 30;
            //x = TRE.SCREEN_WIDTH / 4;
        }
        if (x >= GameConstants.WORLD_WIDTH - 88) {
            x = GameConstants.WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
            //y = TRE.SCREEN_HEIGHT / 2;
        }
        if (y >= GameConstants.WORLD_HEIGHT - 80) {
            y = GameConstants.WORLD_HEIGHT - 80;
        }
    }

    public void update()
    {
        moveForward();
    }

    public void drawImage(Graphics g)
    {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.bulletImage.getWidth() / 2.0, this.bulletImage.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.bulletImage, rotation, null);
        // draw something that represents the rectangle hitbox (BUT IS NOT THE HITBOX)
        //g2d.setColor(Color.BLUE);
        //g2d.drawRect(x, y, this.bulletImage.getWidth(), this.bulletImage.getHeight());

    }
}
