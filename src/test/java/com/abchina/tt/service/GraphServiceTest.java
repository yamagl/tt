package com.abchina.tt.service;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;

class GraphServiceTest {

    @Test
    void test() throws Exception {
        Collection<File> maps = FileUtils.listFiles(new File("map/"), new String[] {"png"}, false);
        Iterator<File> iterator = maps.iterator();
        BufferedImage image1 = ImageIO.read(new File("map/1556725668133.png"));
//        BufferedImage image2 = ImageIO.read(new File("map/1556725671263.png"));

        int LIMIT = 20;
        BufferedImage[] images = new BufferedImage[LIMIT];
        for (int i = 0; i < LIMIT && iterator.hasNext(); i++) {
            images[i] = ImageIO.read(iterator.next());
        }

        for (float alpha = 0.1f; alpha <= 1; alpha += 0.1) {
            BufferedImage target = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = target.createGraphics();
//            Graphics2D g2d = image1.createGraphics();
            //SRC_ATOP
//            g2d.setPaintMode();
//            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_ATOP, alpha));
            for (BufferedImage image : images) {
                g.drawImage(image, 0, 0, null);
            }
            boolean written = ImageIO.write(target, "png", new File("target/composite/" + (int) (alpha * 10) + ".png"));
            System.out.println(written);
            g.dispose();
        }
    }
}