import java.util.HexFormat;

/**
 * Handles the PIF structure
 */
public class PIF {
    private final Header header;
    private final Content content;
    private final Footer footer;

    /**
     * Creates PIF parameters, so they're not null
     */
    public PIF() {
        this.header = new Header();
        this.content = new Content();
        this.footer = new Footer();
    }

    public Header getHeader() {
        return header;
    }

    public Content getContent() {
        return content;
    }

    public Footer getFooter() {
        return footer;
    }

    /**
     * A header of the .pif
     */
    public static class Header{
        byte colorMapType;
        byte imageType;
        byte[] colorMapSpecification = new byte[5];
        byte[] imageSpecification = new byte[6];

        /**
         * Prepares for PIF toString
         * @return String of hex format of bytes
         */
        @Override
        public String toString() {
            HexFormat hex = HexFormat.of();

            StringBuilder result = new StringBuilder();
            result.append(hex.toHexDigits(colorMapType)).append(" ");
            result.append(hex.toHexDigits(imageType)).append(" ");
            for (byte b : colorMapSpecification) {
                result.append(hex.toHexDigits(b)).append(" ");
            }
            for (byte b : imageSpecification) {
                result.append(hex.toHexDigits(b)).append(" ");
            }
            return result.toString();
        }
    }

    /**
     * Contents of the .pif
     */
    public static class Content{
        byte[] image;
        byte[] authorInitials = new byte[3];
        byte[] dateAndTime = new byte[12];

        /**
         * Prepares for PIF toString
         * @return String of hex format of bytes
         */
        @Override
        public String toString() {
            HexFormat hex = HexFormat.of();
            StringBuilder result = new StringBuilder();
            for (byte b : image) {
                result.append(hex.toHexDigits(b)).append(" ");
            }
            for (byte b : authorInitials) {
                result.append(hex.toHexDigits(b)).append(" ");
            }
            for (byte b : dateAndTime) {
                result.append(hex.toHexDigits(b)).append(" ");
            }
            return result.toString();
        }
    }

    /**
     * A footer of the .pif
     */
    public static class Footer{
        byte[] signature = new byte[16];

        /**
         * Prepares for PIF toString
         * @return String of hex format of bytes
         */
        @Override
        public String toString() {
            HexFormat hex = HexFormat.of();
            StringBuilder result = new StringBuilder();
            for (byte b : signature) {
                result.append(hex.toHexDigits(b)).append(" ");
            }
            byte reservedChar = 46;
            result.append(hex.toHexDigits(reservedChar)).append(" ");
            byte comma = 0;
            result.append(hex.toHexDigits(comma));
            return result.toString();
        }
    }

    /**
     * @return metadata of .pif
     */
    public String metadata() {
        StringBuilder result = new StringBuilder();
        result.append("Date and Time: ");
        int year, month, day,  hour, minute, second;
        year = (Byte.toUnsignedInt(content.dateAndTime[5]) << 8) + Byte.toUnsignedInt(content.dateAndTime[4]);
        month = (Byte.toUnsignedInt(content.dateAndTime[1]) << 8) + Byte.toUnsignedInt(content.dateAndTime[0]);
        day = (Byte.toUnsignedInt(content.dateAndTime[3]) << 8) + Byte.toUnsignedInt(content.dateAndTime[2]);
        hour = (Byte.toUnsignedInt(content.dateAndTime[7]) << 8) + Byte.toUnsignedInt(content.dateAndTime[6]);
        minute = (Byte.toUnsignedInt(content.dateAndTime[9]) << 8) + Byte.toUnsignedInt(content.dateAndTime[8]);
        second = (Byte.toUnsignedInt(content.dateAndTime[11]) << 8) + Byte.toUnsignedInt(content.dateAndTime[10]);
        result.append(year).append("/").append(month).append("/").append(day).append(", ").append(hour).append(":").append(minute).append(":").append(second);

        result.append("\nAuthor Initials: ");
        result.append((char) content.authorInitials[0]).append((char) content.authorInitials[1]);

        result.append("\nSignature: ");
        for (byte b : footer.signature) {
            result.append((char) b);
        }
        return result.toString();
    }

    /**
     * Makes readable hexdump-like output
     * @return hexdump-like output
     */
    @Override
    public String toString() {
        HexFormat hex = HexFormat.of();

        String[] bytes = (String.valueOf(header) + content + footer).split(" ");

        StringBuilder result = new StringBuilder();
        result.append(" ".repeat(17)).append("00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F\n");
        long firstColumn = 0;
        for (int i = 0; i < bytes.length; i+=16) {
            result.append(hex.toHexDigits(firstColumn)).append(" ");
            firstColumn += 16;
            if(i + 16 < bytes.length) {
                for (int j = 0; j < 16; j++) {
                    result.append(bytes[i+j]).append(" ");
                }
            } else {
                for (int k = 0; k < (bytes.length-i)%16; k++) {
                    result.append(bytes[i + k]).append(" ");
                }
            }
            result.append("\n");
        }
        return result.toString();
    }
}
