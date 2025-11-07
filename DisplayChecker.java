public class DisplayChecker {
    public static void main(String[] args) {
        // Get the DISPLAY environment variable from the system
        String displayVariable = System.getenv("DISPLAY");

        // Print the result
        System.out.println("Java sees the DISPLAY variable as: " + displayVariable);
    }
}