import java.util.Scanner;

/**
 * Entry point for the Wilderness Survival System (WSS).
 * Prompts the user for map size and difficulty,
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

        System.out.print("  Enter map width  (recommended: 20): ");
        int width = scanner.nextInt();

        System.out.print("  Enter map height (recommended: 10): ");
        int height = scanner.nextInt();

        System.out.println();
        System.out.println("  Select difficulty:");
        System.out.println(
            "    1.  Easy   -- mostly plains, nice traders"
        );
        System.out.println(
            "    2.  Medium -- mixed terrain"
        );
        System.out.println(
            "    3.  Hard   -- mountains/desert, stingy traders"
        );
        System.out.print("  Enter choice (1-3): ");
        int choice = scanner.nextInt();
        scanner.nextLine();     // consume the leftover Enter after nextInt

        Difficulty difficulty;
        switch (choice) {
            case 1:  difficulty = new Easy();   break;
            case 3:  difficulty = new Hard();   break;
            default: difficulty = new Medium(); break;
        }

        difficulty.setBrain();
        difficulty.setTrader();

        System.out.println();
        System.out.println("  Generating map...");

        Game game = new Game();
        game.setDifficulty(difficulty);
        game.setSize(width, height);
        game.createPlayer();
        game.run(scanner);      // share the one Scanner

        scanner.close();
    }
}