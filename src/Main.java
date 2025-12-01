public class Main {
    /**
     * Handles main operations
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            Selector.cycle();
        }
        try {
            switch (args[0].toLowerCase()) {
                case "encode" -> {
                    String authorInitials = "", signature = "";
                    if (args.length > 5) {
                        if (args[5].equals("--initials")) {
                            authorInitials = args[6];
                        } else if (args[5].equals("--signature")) {
                            signature = args[6];
                        }
                    }
                    if (args.length > 7) {
                        if (args[7].equals("--initials")) {
                            authorInitials = args[8];
                        } else if (args[7].equals("--signature")) {
                            signature = args[8];
                        }
                    }
                    Selector.encode(args[2], args[4], authorInitials, signature);
                }
                case "decode" -> Selector.decode(args[2], args[4]);
                case "metadata" -> Selector.metadata(args[2]);
                case "hexdump" -> Selector.hexDump(args[2]);
                case "help", "-help", "--help", "h", "-h", "--h" -> System.out.println("""
                        Encoding (regular to .pif)
                            Mandatory
                                encode -i InputPath -o OutputPath
                            May include (after mandatory)
                                --initials AuthorInitials (max 2 characters)
                                --signature Signature (max 16 characters)
                        
                        Decoding
                            decode -i InputPath -o OutputPath
                        
                        Metadata
                            metadata -i InputPath
                        
                        Hexdump
                            hexdump -i InputPath
                        
                        """);
                default -> System.out.println("Invalid argument(s)");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error with reading arguments");
        } catch (Exception e) {
            System.out.println("Unhandled error" + e.getMessage());
        }
    }
}