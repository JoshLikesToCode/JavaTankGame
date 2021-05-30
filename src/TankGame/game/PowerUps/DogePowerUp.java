package TankGame.game.PowerUps;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DogePowerUp extends PowerUp {
    int x, y;
    BufferedImage dogeImage;
    public Rectangle hitBox;

    public DogePowerUp(int x, int y, BufferedImage wallImage) {
        this.x = x;
        this.y = y;
        this.dogeImage = wallImage;
        this.hitBox = new Rectangle(x,y, wallImage.getWidth(), wallImage.getHeight());
    }

    @Override
    public Rectangle getHitBox() {
        return hitBox.getBounds();
    }

    @Override
    public void drawImage(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.dogeImage, x, y, null);
    }}
