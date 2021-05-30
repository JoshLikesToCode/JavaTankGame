package TankGame.game;



import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import TankGame.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Tank extends GameObject {

    private int x;
    private int y;

    private int vx;
    private int vy;
    private float angle;

    private final int R = 2;
    private final float ROTATIONSPEED = 3.0f;

    private int health;

    private Rectangle hitBox;

    private ArrayList<Bullet> ammo;

    private BufferedImage img;

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean shoot_pressed;
    private boolean move_up;
    private boolean move_down;
    private boolean move_left;
    private boolean move_right;

    private int remaining_lives;


    Tank(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        this.health = 100;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.hitBox = new Rectangle(x,y, this.img.getWidth(), this.img.getHeight());
        this.ammo = new ArrayList<>();
        this.move_down = true;
        this.move_left = true;
        this.move_right = true;
        this.move_up = true;
        remaining_lives = 3;
    }

    public boolean isMove_up() {
        return move_up;
    }

    public boolean isMove_down() {
        return move_down;
    }

    public boolean isMove_left() {
        return move_left;
    }


    public boolean isMove_right() {
        return move_right;
    }

    public int getHealth() {
        return health;
    }

    public void setMove_up(boolean move_up) {
        this.move_up = move_up;
    }

    public void setMove_down(boolean move_down) {
        this.move_down = move_down;
    }

    public void setMove_left(boolean move_left) {
        this.move_left = move_left;
    }

    public void setMove_right(boolean move_right) {
        this.move_right = move_right;
    }

    public ArrayList<Bullet> getAmmo() {
        return ammo;
    }

    public Rectangle getHitBox() {
        return hitBox.getBounds();
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    void setX(int x){ this.x = x; }

    void setY(int y) { this. y = y;}

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void toggleShootPressed() { this.shoot_pressed = true;}
    void unToggleShootPressed() { this.shoot_pressed = false;}

    public boolean isUpPressed() {
        return UpPressed;
    }

    public boolean isDownPressed() {
        return DownPressed;
    }

    public int getRemaining_lives() {
        return remaining_lives;
    }

    public void setRemaining_lives(int remaining_lives) {
        this.remaining_lives = remaining_lives;
    }


    @Override
    public void update() {

        if (this.health < 0)
        {
            //this.img = Resource.getResourceImage("grass.png");
            ClassLoader CLDR = this.getClass().getClassLoader();
            InputStream soundName = CLDR.getResourceAsStream("Explosion_large.wav");
            AudioStream audioStream = null;
            try {
                assert soundName != null;
                audioStream = new AudioStream(soundName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            AudioPlayer.player.start(audioStream);
            //System.out.println("Decrementing lives");
            /* decrement a life and reset health */
            this.remaining_lives--;
            this.health = 100;
            // game over sound
            if(this.remaining_lives == 0)
            {
                InputStream gameOver = CLDR.getResourceAsStream("game-over.wav");
                try {
                    assert gameOver != null;
                    audioStream = new AudioStream(gameOver);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AudioPlayer.player.start(audioStream);
            }
        }

        if (this.UpPressed && move_up) {
            this.moveForwards();
        }
        if (this.DownPressed && move_down) {
            this.moveBackwards();
        }

        if (this.LeftPressed && move_left) {
            this.rotateLeft();
        }
        if (this.RightPressed && move_right) {
            this.rotateRight();
        }

        if(this.shoot_pressed && TRE.tick % 20 == 0)
        {
            //System.out.println("x = " + this.x + " and y = " + this.y);
            //sound.playSound("resources/Explosion_small.wav");
            ClassLoader CLDR = this.getClass().getClassLoader();
            InputStream soundName = CLDR.getResourceAsStream("Explosion_small.wav");
            AudioStream audioStream = null;
            try {
                assert soundName != null;
                audioStream = new AudioStream(soundName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            AudioPlayer.player.start(audioStream);
            Bullet b = new Bullet(x,y,angle,Resource.getResourceImage("bullet"), this);
            this.ammo.add(b);
            TRE t = null;
            assert t != null;
            t.add_bullet(b);
        }
        this.ammo.forEach(bullet -> bullet.update());
    }


    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
        this.hitBox.setLocation(x, y);
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitBox.setLocation(x, y);
    }




    private void checkBorder() {
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getVx() {
        return vx;
    }

    public int getVy() {
        return vy;
    }

    public float getAngle() {
        return angle;
    }



    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    @Override
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        this.ammo.forEach(bullet -> bullet.drawImage(g));
        // draw something that represents the rectangle hitbox (BUT IS NOT THE HITBOX)
        //g2d.setColor(Color.CYAN);
        //g2d.drawRect(x, y, this.img.getWidth(), this.img.getHeight());
    }



}
