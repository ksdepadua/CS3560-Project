import java.util.Scanner;

/**
 * Entry point for the Wilderness Survival System (WSS).
 * Prompts the user for map size, difficulty, and brain,
 * then runs the game.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Display.clearScreen();
        Display.printTitle();

        // Recommend a sensible default size
        System.out.println(
            "  Tip: use a small map to start (e.g. 20 wide, 10 tall)"
        );
        System.out.println(
            "  Your terminal needs to be wide enough to fit the map."
        );
        System.out.println();

        int width  = readInt(scanner,
            "  Enter map width  (recommended: 20): ", 2, 100);
        int height = readInt(scanner,
            "  Enter map height (recommended: 10): ", 2, 100);

        // ---------- Difficulty ----------
        System.out.println();
        System.out.println("  Select difficulty:");
        System.out.println("    1.  Easy   -- mostly plains, nice traders");
        System.out.println("    2.  Medium -- mixed terrain");
        System.out.println("    3.  Hard   -- mountains/desert, stingy traders");
        int difficultyChoice = readInt(scanner, "  Enter choice (1-3): ", 1, 3);

        Difficulty difficulty;
        switch (difficultyChoice) {
            case 1:  difficulty = new Easy();   break;
            case 3:  difficulty = new Hard();   break;
            default: difficulty = new Medium(); break;
        }
        difficulty.setBrain();
        difficulty.setTrader();

        // ---------- Brain ----------
        System.out.println();
        System.out.println("  Select a brain (player strategy):");
        System.out.println("    1.  Cautious      -- keeps resources topped up, rests often");
        System.out.println("    2.  Goal-Oriented -- heads east as fast as it can");
        int brainChoice = readInt(scanner, "  Enter choice (1-2): ", 1, 2);

        Brain brain;
        switch (brainChoice) {
            case 1:  brain = new CautiousBrain();     break;
            default: brain = new GoalOrientedBrain(); break;
        }

        System.out.println();
        System.out.println("  Generating map...");

        Game game = new Game();
        game.setDifficulty(difficulty);
        game.setBrain(brain);
        game.setSize(width, height);
        game.createPlayer();
        game.run(scanner);      // share the one Scanner

        scanner.close();
    }

    /**
     * Asks for a whole number until the user types one in range.
     * Reads a full line each time, so no leftover Enter is left
     * behind for the game's n/q prompt.
     *
     * @param scanner the input scanner
     * @param prompt  text to show
     * @param min     smallest allowed value
     * @param max     largest allowed value
     * @return the number the user entered
     */
    private static int readInt(Scanner scanner, String prompt,
                               int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // not a number, fall through and ask again
            }
            System.out.println("  Please enter a number from "
                               + min + " to " + max + ".");
        }
    }
}