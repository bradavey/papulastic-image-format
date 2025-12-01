import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Selector {

    /**
     * Encodes regular image file to .pif
     * @param input path address of the input file, file name and its type
     * @param output path address of the output file, file name and its type
     * @param authorInitials mandatory 3 bytes in .pif structure
     * @param signature signature mandatory 16 bytes in .pif structure
     */
    public static void encode(String input, String output, String authorInitials, String signature) {
        try {
            FileIO.writeBytes(output, PIFFormater.outputPIF(PIFFormater.toPIF(FileIO.readImage(input), authorInitials, signature)));
        } catch (IOException e) {
            System.out.println("Error with files, double check the paths, file names and extensions");
        }
    }

    /**
     * Decodes .pif to regular image file
     * @param input path address of the input file, file name and its type
     * @param output path address of the output file, file name and its type
     */
    public static void decode(String input, String output) {
        try {
            FileIO.writeImage(output, PIFFormater.toRegularImage(PIFFormater.inputPIF(FileIO.readBytes(input))));
        }  catch (IOException e) {
            System.out.println("Error with files, double check the paths, file names and extensions");
        }
    }

    /**
     * Prints metadata of .pif
     * @param input path address of the .pif file, file name and its type
     */
    public static void metadata(String input) {
        try {
            System.out.println(PIFFormater.inputPIF(FileIO.readBytes(input)).metadata());
        }  catch (IOException e) {
            System.out.println("Error with file, double check the path, file name and extension");
        }
    }

    /**
     * Prints hexdump of .pif
     * @param input path address of the .pif file, file name and its type
     */
    public static void hexDump(String input) {
        try {
            System.out.println(PIFFormater.inputPIF(FileIO.readBytes(input)));
        }  catch (IOException e) {
            System.out.println("Error with file, double check the path, file name and extension");
        }
    }

    /**
     * Handles cycle for work without arguments
     */
    public static void cycle() {
        int choice;
        do{
            System.out.println("""
                    
                    1. Convert regular image to .pif
                    2. Convert .pif to regular image
                    3. Read metadata of .pif
                    4. Hexdump of .pif
                    0. Exit
                    """);
            choice = readInt(0, 4, "Write a number between 0 and 4 and press enter.");
            switch(choice){
                case 1 -> {
                    String inputPath = readString("Write input image path (including extension).");
                    String outputPath = readString("Write output image path (including extension).");
                    String authorInitials = readString("Write author initial (max 2 characters, for non press enter).");
                    String signature = readString("Write signature (max 16 characters, for non press enter).");
                    encode(inputPath, outputPath, authorInitials, signature);
                }
                case 2 -> {
                    String inputPath = readString("Write input image path (including extension).");
                    String outputPath = readString("Write output image path (including extension).");
                    decode(inputPath, outputPath);
                }
                case 3 -> {
                    String inputPath = readString("Write input .pif path (including extension).");
                    metadata(inputPath);
                }
                case 4 -> {
                    String inputPath = readString("Write input .pif path (including extension).");
                    hexDump(inputPath);
                }
                case 0 -> System.exit(0);
            }
        }while(true);
    }

    /**
     * Reads integer in specified range
     * @param minimal minimal acceptable number
     * @param maximal maximal acceptable number
     * @param prompt if isn't null, print before scan
     * @return Scanned and verified int
     */
    public static int readInt(double minimal, double maximal, String prompt) {
        int number;
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                if(prompt != null) System.out.println(prompt);
                number = scanner.nextInt();
                boolean isValid = number >= minimal && number <= maximal;
                if (isValid) {
                    return number;
                } else {
                    System.out.println("\nEnter number in required range");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nPlease enter a valid number");
            } catch (Exception e) {
                System.out.println("\nUnexpected error");
            }
        }
    }

    /**
     * Reads String
     * @param prompt if isn't null, print before scan
     * @return Entered String
     */
    public static String readString(String prompt) {
        String string;
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                if(prompt != null) System.out.println(prompt);
                string = scanner.nextLine();
                return string;
            } catch (InputMismatchException e) {
                System.out.println("\nPlease enter a valid text");
            } catch (Exception e) {
                System.out.println("\nUnexpected error");
            }
        }
    }
}
