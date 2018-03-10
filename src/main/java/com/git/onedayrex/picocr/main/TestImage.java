package com.git.onedayrex.picocr.main;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

/**
 * created by onedayrex
 * 2018/3/11
 **/
public class TestImage {
    private static final Logger logger = Logger.getLogger(TestImage.class);

    public static void main(String[] args) {
        try {
            File file = new File("D:/vds.jpg");
            BufferedImage write = ImageIO.read(file);
            int width = write.getWidth();
            int height = write.getHeight();
            BufferedImage read = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D readGraphics = read.createGraphics();
            readGraphics.setColor(Color.black);
            readGraphics.fillRect(0, 0, width, height);
            readGraphics.dispose();
            Graphics2D writeGraphics = write.createGraphics();
            writeGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
            writeGraphics.drawImage(read, 0, 0, null);
            writeGraphics.dispose();
            FileOutputStream fileOutputStream = new FileOutputStream("D:/111.png");
            ImageIO.write(write, "png", fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
