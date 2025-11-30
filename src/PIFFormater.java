import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;

/**
 * Handles format conversions
 */
public class PIFFormater {
    /**
     * Constructs a .pif file structure
     * @param image raster of the image converted to .pif in BufferedImage
     * @param authorInitials mandatory 3 bytes in .pif structure
     * @param signature mandatory 16 bytes in .pif structure
     * @return .pif
     */
    public static PIF toPIF(BufferedImage image, String authorInitials, String signature) {
        HexFormat hex = HexFormat.of();
        PIF pif = new PIF();
        {
            pif.getHeader().colorMapType = 0;
            pif.getHeader().imageType = 2;
            pif.getHeader().colorMapSpecification = new byte[]{0, 0, 0, 0, 0};
            byte[] width = hex.parseHex(hex.toHexDigits(image.getWidth()));
            byte[] height = hex.parseHex(hex.toHexDigits(image.getHeight()));
            pif.getHeader().imageSpecification = new byte[]{width[3], width[2], height[3], height[2], 24, 32};
        }
        {
            pif.getContent().image = new byte[image.getWidth() * image.getHeight() * 3];
            int i = 0;
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    byte[] argb = hex.parseHex(hex.toHexDigits(image.getRGB(x, y)));
                    pif.getContent().image[i] = argb[3];
                    pif.getContent().image[i + 1] = argb[2];
                    pif.getContent().image[i + 2] = argb[1];
                    i += 3;
                }
            }

            byte[] initialBytes = new byte[3];
            int j = 0;
            for (int k = 0; k < initialBytes.length - 1; k++) {
                if(j < authorInitials.length()) {
                    initialBytes[k] = (byte) authorInitials.charAt(j++);
                } else {
                    initialBytes[k] = 32;
                }
            }
            pif.getContent().authorInitials = initialBytes;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy-HH-mm-ss");
            LocalDateTime now = LocalDateTime.now();
            String[] formatedDateAndTime = now.format(formatter).split("-");
            byte[] timeAndDate = new byte[12];
            for (int l = 0; l < timeAndDate.length/2; l++) {
                int currentData = Integer.parseInt(formatedDateAndTime[l]);
                String current = hex.toHexDigits(currentData);
                byte[] currentBytes = hex.parseHex(current);
                timeAndDate[l*2] = currentBytes[3];
                timeAndDate[l*2 + 1] = currentBytes[2];
            }
            pif.getContent().dateAndTime = timeAndDate;
        }
        {
            byte[] signatureBytes = new byte[16];
            int i = 0;
            for (int j = 0; j < signatureBytes.length; j++) {
                if (i < signature.length()) {
                    signatureBytes[j] = (byte) signature.charAt(i++);
                } else {
                    signatureBytes[j] = 32;
                }
            }
            pif.getFooter().signature = signatureBytes;
        }
        return pif;
    }

    /**
     * Formats .pif file to image raster
     * @param pif formated .pif
     * @return formated image raster in BufferedImage
     */
    public static BufferedImage toRegularImage(PIF pif) {
        int width = (Byte.toUnsignedInt(pif.getHeader().imageSpecification[1]) << 8) + Byte.toUnsignedInt(pif.getHeader().imageSpecification[0]);
        int height = (Byte.toUnsignedInt(pif.getHeader().imageSpecification[3]) << 8) + Byte.toUnsignedInt(pif.getHeader().imageSpecification[2]);
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        int i = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = (((255 << 8) + pif.getContent().image[i + 2] << 8) + pif.getContent().image[i + 1] << 8) +  pif.getContent().image[i];
                i+=3;
                result.setRGB(x, y, rgb);
            }
        }
        return result;
    }

    /**
     * Constructs PIF structure from .pif
     * @param pif read PIF bytes
     * @return constructed PIF
     */
    public static PIF inputPIF(byte[] pif) {
        PIF result = new PIF();
        int i = 0;
        result.getHeader().colorMapType = pif[i++];
        result.getHeader().imageType = pif[i++];
        for (int j = 0; j < 5; j++) {
            result.getHeader().colorMapSpecification[j] = pif[i++];
        }
        for (int k = 0; k < 6; k++) {
            result.getHeader().imageSpecification[k] = pif[i++];
        }

        result.getContent().image = new byte[pif.length - 46];
        for (int l = 0; l < pif.length - 46; l++) {
            result.getContent().image[l] = pif[i++];
        }
        for (int m = 0; m < 3; m++) {
            result.getContent().authorInitials[m] = pif[i++];
        }
        for (int n = 0; n < 12; n++) {
            result.getContent().dateAndTime[n] = pif[i++];
        }

        for (int o = 0; o < 16; o++) {
            result.getFooter().signature[o] = pif[i++];
        }
        return result;
    }

    /**
     * Constructs byte structure of .pif
     * @param pif constructed PIF
     * @return all bytes of the PIF structure
     */
    public static byte[] outputPIF(PIF pif) {
        byte[] result = new byte[46 + pif.getContent().image.length];
        int i = 0;
        result[i++] = pif.getHeader().colorMapType;
        result[i++] = pif.getHeader().imageType;
        for (byte b : pif.getHeader().colorMapSpecification) {
            result[i++] = b;
        }
        for (byte b : pif.getHeader().imageSpecification) {
            result[i++] = b;
        }

        for (byte b : pif.getContent().image) {
            result[i++] = b;
        }
        for (byte b : pif.getContent().authorInitials) {
            result[i++] = b;
        }
        for (byte b : pif.getContent().dateAndTime) {
            result[i++] = b;
        }

        for (byte b : pif.getFooter().signature) {
            result[i++] = b;
        }
        result[i++] = 46;
        result[i] = 0;
        return result;
    }
    
}
