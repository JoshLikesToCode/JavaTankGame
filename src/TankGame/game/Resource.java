package TankGame.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static javax.imageio.ImageIO.read;

public class Resource {
    private static Map<String, BufferedImage> image_resources;
    static {
        Resource.image_resources = new HashMap<>();
        try {
            Resource.image_resources.put("tank1", read(TRE.class.getClassLoader().getResource("tank1.png")));
            Resource.image_resources.put("tank2", read(TRE.class.getClassLoader().getResource("tank2.png")));
            Resource.image_resources.put("brokenwall", read(TRE.class.getClassLoader().getResource("brokenwall.png")));
            Resource.image_resources.put("unbreakWall", read(TRE.class.getClassLoader().getResource("wall.png")));
            Resource.image_resources.put("grass", read(TRE.class.getClassLoader().getResource("grass.png")));
            Resource.image_resources.put("bullet", read(TRE.class.getClassLoader().getResource("bullet.png")));
            Resource.image_resources.put("dogePU", read(TRE.class.getClassLoader().getResource("dodgepowerup1.png")));
            Resource.image_resources.put("shotwall", read(TRE.class.getClassLoader().getResource("shotwall.png")));
        } catch (IOException e) {
            e.printStackTrace();
            // if anything breaks, we want to leave right away
            System.exit(-5);
        }
    }

        public static BufferedImage getResourceImage(String key)
        {
            return Resource.image_resources.get(key);
        }

}

