import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ImageResizer implements Runnable {
    private String dstFolder;
    private List<File> files;
    private long start;

    public ImageResizer(List<File> files, long start, String dstFolder) {
        this.files = files;
        this.start = start;
        this.dstFolder = dstFolder;
    }

    @Override
    public void run() {
        for (File file : files) {
            BufferedImage image = null;
            try {
                image = ImageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            File newFile = new File(dstFolder + "/" + file.getName());
            int newWidth = 512;
            int newHeight = (int) Math.round(image.getHeight() / (image.getWidth() / (double) newWidth));

            BufferedImage newImage = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH,
                    newWidth, newHeight, Scalr.OP_ANTIALIAS);

            try {
                ImageIO.write(newImage, getImageFormat(file), newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Duration: " + (System.currentTimeMillis() - start));
    }

    private static String getImageFormat(File imageFile) {
        ImageInputStream imageInputStream = null;
        try {
            imageInputStream = ImageIO.createImageInputStream(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<ImageReader> imageReadersList = ImageIO.getImageReaders(imageInputStream);
        if (!imageReadersList.hasNext()) {
            throw new RuntimeException("Image Readers Not Found");
        }

        ImageReader reader = imageReadersList.next();
        String format = "";
        try {
            format = reader.getFormatName();
            imageInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return format;
    }
}
