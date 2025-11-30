import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Handles File work
 */
public class FileIO {

    /**
     * Reads general image files and outputs BufferedImage
     * @param path address of the file, file name and its type
     * @return constructed raster in BufferedImage
     */
    public static BufferedImage readImage(String path) throws IOException {
        File file = new File(path);
        return ImageIO.read(file);
    }

    /**
     * Writes regular image files
     * @param path address of the file, file name and its type
     * @param image image raster in BufferedImage
     */
    public static void writeImage(String path, BufferedImage image) throws IOException {
        File file = new File(path);
        String fileType = path.substring(path.lastIndexOf('.') + 1);
        ImageIO.write(image, fileType, file);
    }

    /**
     * Reads raw bytes from files, used for .pif
     * @param path address of the file, file name and its type
     * @return Array of all bytes in the file
     */
    public static byte[] readBytes(String path) throws IOException {
        FileInputStream reader = new FileInputStream(path);
        byte[] contents = reader.readAllBytes();
        reader.close();
        return contents;
    }

    /**
     * Writes raw bytes into files, used for .pif
     * @param path address of the file, file name and its type
     * @param contents Array of all bytes outputted to the file
     */
    public static void writeBytes(String path, byte[] contents) throws IOException {
        FileOutputStream writer = new FileOutputStream(path);
        for (byte b : contents) {
            writer.write(b);
        }
        writer.close();
    }
}
