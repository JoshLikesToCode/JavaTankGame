/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame.game;


import TankGame.GameConstants;
import TankGame.Launcher;
import TankGame.game.PowerUps.DogePowerUp;
import TankGame.game.PowerUps.PowerUp;
import TankGame.game.Walls.BlankWall;
import TankGame.game.Walls.BreakWall;
import TankGame.game.Walls.UnBreakWall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;


import static javax.imageio.ImageIO.read;

public class TRE extends JPanel implements Runnable {

    /** FOR MAPS
     * 0 -> MEANS NOTHING
     * 2 -> Breakable Wall
     * 3 -> Health Power up
     * 9 -> unbreakable walls, walls that are not used in collision
     */

    private BufferedImage world;
    static long tick = 0;

    //public static BufferedImage bulletImage;
    private Graphics2D buffer;
    private JFrame jFrame;
    private Tank t2;
    private Tank t1;
    private Launcher lf;
    static ArrayList<GameObject> gameObjects;
    static ArrayList<Bullet> gameAmmo;

    public static void add_bullet(Bullet b)
    {
        //System.out.println("Bullet added to gameAmmo");
        gameAmmo.add(b);
    }

    public TRE(Launcher lf){
        this.lf = lf;
    }

    @Override
    public void run(){
       try {
           this.resetGame();
           while (true) {
                this.tick++;
                this.t1.update(); // update tank
                this.t2.update();
                this.repaint();   // redraw game

               // tank collision handling
                if(this.t1.getHitBox().intersects(this.t2.getHitBox())) {
                    if(this.t1.isUpPressed()){
                        this.t1.setY(this.t1.getY() - this.t1.getVy());
                        this.t1.setX(this.t1.getX() - this.t1.getVx());
                    }else if(this.t1.isDownPressed()){
                        this.t1.setY(this.t1.getY() + this.t1.getVy());
                        this.t1.setX(this.t1.getX() + this.t1.getVx());
                    }
                    if(this.t2.isUpPressed()){
                        this.t2.setY(this.t2.getY() - this.t2.getVy());
                        this.t2.setX(this.t2.getX() - this.t2.getVx());
                    }else if(this.t2.isDownPressed()){
                        this.t2.setY(this.t2.getY() + this.t2.getVy());
                        this.t2.setX(this.t2.getX() + this.t2.getVx());
                    }
                }

                // for wall collision handling
               for (GameObject element : gameObjects) {
                   if (element != null && (!(element instanceof PowerUp)) && this.t1.getHitBox().intersects(element.getHitBox())) {
                       // if wall is broken already, we should be able to drive over it
                       if (element instanceof BreakWall && ((BreakWall) element).getState() <= 1)
                           continue;
                       if (this.t1.isUpPressed()) {
                           this.t1.setY(this.t1.getY() - this.t1.getVy());
                           this.t1.setX(this.t1.getX() - this.t1.getVx());
                       } else if (this.t1.isDownPressed()) {
                           this.t1.setY(this.t1.getY() + this.t1.getVy());
                           this.t1.setX(this.t1.getX() + this.t1.getVx());
                       }
                   }
               }
               // collision for running into objects
               for (GameObject object : gameObjects) {
                   if (object != null && (!(object instanceof PowerUp)) && this.t2.getHitBox().intersects(object.getHitBox())) {
                       // if wall is broken already, we should be able to drive over it
                       if (object instanceof BreakWall && ((BreakWall) object).getState() <= 1)
                           continue;
                       else if (this.t2.isUpPressed()) {
                           this.t2.setY(this.t2.getY() - this.t2.getVy());
                           this.t2.setX(this.t2.getX() - this.t2.getVx());
                       } else if (this.t2.isDownPressed()) {
                           this.t2.setY(this.t2.getY() + this.t2.getVy());
                           this.t2.setX(this.t2.getX() + this.t2.getVx());
                       }
                   }
               }
               // for bullet collisions
               for (Bullet item : gameAmmo) {
                   if ((item != null) && item.getHitBox().intersects(this.t1.getHitBox()) && ((Bullet) item).getShooter() == t2) {
                       //System.out.println("Bullet collision.");
                       this.t1.setHealth(this.t1.getHealth() - 5);
                   }
               }
               for (Bullet value : gameAmmo) {
                   if ((value != null) && value.getHitBox().intersects(this.t2.getHitBox()) && ((Bullet) value).getShooter() == t1) {
                       //System.out.println("Bullet collision");
                       this.t2.setHealth(this.t2.getHealth() - 5);
                   }
               }

               // for breakable wall collisions
               for (Bullet bullet : gameAmmo) {
                   for (GameObject gameObject : gameObjects) {
                       if (bullet != null && gameObject instanceof BreakWall && bullet.getHitBox().intersects(gameObject.getHitBox())) {
                           //System.out.println("Breakwall collision");
                           ((BreakWall) gameObject).setState(((BreakWall) gameObject).getState() - 1);
                       }
                   }
               }

               // Power up restores health back to 100
               for (GameObject gameObject : gameObjects) {
                   if (gameObject instanceof PowerUp && this.t1.getHitBox().intersects(gameObject.getHitBox())) {
                       this.t1.setHealth(100);
                   }
               }
               for (GameObject gameObject : gameObjects) {
                   if (gameObject instanceof PowerUp && this.t2.getHitBox().intersects(gameObject.getHitBox())) {
                       this.t2.setHealth(100);
                   }
               }



               Thread.sleep(1000 / 144); //sleep for a few milliseconds
                /*
                 * simulate an end game event
                 * we will do this with by ending the game when drawn 2000 frames have been drawn
                 */
                if(tick > 2500){
                    this.lf.setFrame("end");
                    return;
                }
            }
       } catch (InterruptedException ignored) {
           System.out.println(ignored);
       }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame(){
        tick = 0;
        this.t1.setX(96);
        this.t1.setY(488);
        this.t1.setRemaining_lives(3);
        this.t1.setHealth(100);
        this.t2.setX(1159);
        this.t2.setY(478);
        this.t2.setRemaining_lives(3);
        this.t2.setHealth(100);
    }


    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void gameInitialize() {
        this.world = new BufferedImage(GameConstants.WORLD_WIDTH,
                                       GameConstants.WORLD_HEIGHT,
                                       BufferedImage.TYPE_INT_RGB);

        /*BufferedImage t1img = null;
        BufferedImage t2img = null;
        BufferedImage breakWall = null;
        BufferedImage unbreakWall = null;
        BufferedImage blankWall = null;*/
        gameObjects = new ArrayList<>();
        gameAmmo = new ArrayList<>();
        try {
            /*
             * note class loaders read files from the out folder (build folder in Netbeans) and not the
             * current working directory.
             */
            InputStreamReader isr = new InputStreamReader((Objects.requireNonNull(TRE.class.getClassLoader().getResourceAsStream("maps/map1"))));
            BufferedReader mapReader = new BufferedReader(isr);

            String row = mapReader.readLine();
            String[] mapInfo = row.split("\t");
            int numCols = Integer.parseInt(mapInfo[0]);
            int numRows = Integer.parseInt(mapInfo[1]);

            /* outer grabs a row from the file and inner loop is going to go through each column in the row we just read */
            for(int curRow = 0; curRow < numRows; curRow++)
            {
                row = mapReader.readLine();
                mapInfo = row.split("\t");
                for(int curCol = 0; curCol < numCols; curCol++) // -21
                {
                    switch(mapInfo[curCol])
                    {
                        // blank background
                        case "0":
                            gameObjects.add(new BlankWall(curCol * 30, curRow * 30, Resource.getResourceImage("grass")));
                            break;
                        // breakable
                        case "2":
                            gameObjects.add(new BreakWall(curCol * 30, curRow * 30, Resource.getResourceImage("brokenwall")));
                            break;
                        // power ups
                        case "3":
                            gameObjects.add(new DogePowerUp(curCol * 30, curRow * 30, Resource.getResourceImage("dogePU")));
                            break;
                        // unbreakable walls
                        case "9":
                            gameObjects.add(new UnBreakWall(curCol * 30, curRow * 30, Resource.getResourceImage("unbreakWall")));
                    }

                }
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        t1 = new Tank(96, 488, 0, 0, 0, Resource.getResourceImage("tank1"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE);
        t2 = new Tank(1159, 478, 0, 0, 180, Resource.getResourceImage("tank2"));
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_E);
        //this.setBackground(Color.BLACK);
        this.lf.getJf().addKeyListener(tc1);
        this.lf.getJf().addKeyListener(tc2);
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        buffer.setColor(Color.BLACK);
        buffer.fillRect(0, 0, GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT);
        gameObjects.forEach(gameObjects -> gameObjects.drawImage(buffer));
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);

        /* keeping the camera centered properly */
        int t1x = t1.getX() - GameConstants.SCREEN_WIDTH / 40;
        if (t1.getX() - GameConstants.SCREEN_WIDTH / 40 < 0) {
            t1x = 0;
        } else if ((t1.getX() + GameConstants.SCREEN_WIDTH / 40) > (GameConstants.WORLD_WIDTH)) {
            t1x = GameConstants.WORLD_WIDTH - GameConstants.SCREEN_WIDTH / 20;
        }

        int t1y = t1.getY() - GameConstants.SCREEN_HEIGHT / 2;
        if (t1.getY() - GameConstants.SCREEN_HEIGHT / 2 < 0) {
            t1y = 0;
        } else if ((t1.getY() + GameConstants.SCREEN_HEIGHT / 2) > (GameConstants.WORLD_HEIGHT)) {
            t1y = GameConstants.WORLD_HEIGHT - GameConstants.SCREEN_HEIGHT;
        }
        int t2x = t2.getX() - GameConstants.SCREEN_WIDTH / 40;
        if (t2.getX() - GameConstants.SCREEN_WIDTH / 40 < 0) {
            t2x = 0;
        } else if ((t1.getX() + GameConstants.SCREEN_WIDTH / 40) > (GameConstants.WORLD_WIDTH)) {
            t2x = GameConstants.WORLD_WIDTH - GameConstants.SCREEN_WIDTH / 20;
        }

        int t2y = t2.getY() - GameConstants.SCREEN_HEIGHT / 2;
        if (t2.getY() - GameConstants.SCREEN_HEIGHT / 2 < 0) {
            t2y = 0;
        } else if ((t2.getY() + GameConstants.SCREEN_HEIGHT / 2) > (GameConstants.WORLD_HEIGHT)) {
            t2y = GameConstants.WORLD_HEIGHT - GameConstants.SCREEN_HEIGHT;
        }
        AffineTransform oldXForm = g2.getTransform();

        /* split screen */
        BufferedImage leftHalf = world.getSubimage(t1x, t1y, GameConstants.SCREEN_WIDTH / 2, GameConstants.SCREEN_HEIGHT);
        BufferedImage rightHalf = world.getSubimage(t2x, t2y, GameConstants.SCREEN_WIDTH / 2, GameConstants.SCREEN_HEIGHT);
        /* for mini map */
        BufferedImage mm = world.getSubimage(0, 0, GameConstants.WORLD_WIDTH / 2 + 290, GameConstants.WORLD_HEIGHT / 2 - 40);
        g2.drawImage(leftHalf, 0, 0, null);
        g2.drawImage(rightHalf, GameConstants.SCREEN_WIDTH / 2, 0, null);
        g2.drawImage(mm.getScaledInstance(GameConstants.SCREEN_WIDTH * 3 / 10, GameConstants.SCREEN_WIDTH * 2 / 10, BufferedImage.TYPE_INT_RGB), -20, 0, null);
        /* for displaying player health & lives */
        g2.setColor(Color.RED);
        // Math.max() below to prevent negative lives from being posted to screen
        g2.drawString("T1 Lives Left: " + (Math.max(this.t1.getRemaining_lives(), 0)), GameConstants.SCREEN_WIDTH / 10 - 50, GameConstants.SCREEN_HEIGHT / 2 + 285);
        g2.drawString((this.t1.getRemaining_lives() > 0) ? "T1 HP: " : "LOSERS HP: " + this.t1.getHealth(), GameConstants.SCREEN_WIDTH / 10 - 50, GameConstants.SCREEN_HEIGHT / 2 + 300);
        g2.drawRect(GameConstants.SCREEN_WIDTH / 10 - 50, GameConstants.SCREEN_HEIGHT / 2 + 300, 100, 10);
        g2.setColor(Color.GREEN);
        for (int i = 0; i < this.t1.getHealth() && i < 100; i++) {
                g2.drawRect(GameConstants.SCREEN_WIDTH / 10 - 50 + i, GameConstants.SCREEN_HEIGHT / 2 + 300, 1, 10);
        }
        g2.setColor(Color.RED);
        g2.drawString("T2 Lives Left: " + (Math.max(this.t2.getRemaining_lives(), 0)), GameConstants.SCREEN_WIDTH / 10 + 745, GameConstants.SCREEN_HEIGHT / 2 + 285);
        g2.drawString((this.t2.getRemaining_lives() > 0) ? "T2 HP: " : "LOSERS HP: " + this.t2.getHealth(), GameConstants.SCREEN_WIDTH / 10 + 745, GameConstants.SCREEN_HEIGHT / 2 + 300);
        g2.drawRect(GameConstants.SCREEN_WIDTH / 10 + 745, GameConstants.SCREEN_HEIGHT / 2 + 300, 100, 10);
        g2.setColor(Color.GREEN);
        for (int i = 0; i < this.t2.getHealth() && i < 100; i++) {
            g2.drawRect(GameConstants.SCREEN_WIDTH / 10 + 745 + i, GameConstants.SCREEN_HEIGHT / 2 + 300, 1, 10);
        }
        /* for keeping track of game time */
        g2.setTransform(oldXForm);
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("TimesRoman", Font.BOLD, 30));
        g2.drawString("Time Left: " + (2500-tick), GameConstants.SCREEN_WIDTH/2-150, GameConstants.SCREEN_HEIGHT/2-350);
        /* for game over */
        boolean game_over_flag = false; // to prevent one tank from "winning" after the other has already "won"
        if(this.t1.getRemaining_lives() <= 0 && !game_over_flag)
        {
            g2.setTransform(oldXForm);
            g2.setColor(Color.RED);
            g2.setFont(new Font("TimesRoman", Font.BOLD, 30));
            g2.drawString("GAME OVER, T2 WINS!", GameConstants.SCREEN_WIDTH/2-225, GameConstants.SCREEN_HEIGHT/2);
            game_over_flag = true;
        } else if(this.t2.getRemaining_lives() <= 0 && !game_over_flag)
        {
            g2.setTransform(oldXForm);
            g2.setColor(Color.RED);
            g2.setFont(new Font("TimesRoman", Font.BOLD, 30));
            g2.drawString("GAME OVER, T1 WINS!", GameConstants.SCREEN_WIDTH/2-225, GameConstants.SCREEN_HEIGHT/2);
            game_over_flag = true;
        }
    }
}


