import java.util.Scanner;

/**
 * Controls the overall flow of the WSS game.
 * Sets up the difficulty, map, and player, then
 * runs the player through the map one turn at a time,
 * waiting for the user to press n before each turn.
 */
public class Game {

    private static final int MAX_TURNS = 2000;

    private Difficulty difficulty;
    private Brain brain;          // chosen by the user; null = use difficulty's
    private Map map;
    private Player player;
    private int turn;

    /**
     * Sets the difficulty for this game.
     *
     * @param difficulty the chosen difficulty
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Sets the brain (strategy) the player will use.
     * If never called, the difficulty's default brain is used.
     *
     * @param brain the chosen brain
     */
    public void setBrain(Brain brain) {
        this.brain = brain;
    }

    /**
     * Creates the map using the selected difficulty.
     *
     * @param width  the width of the map
     * @param height the height of the map
     */
    public void setSize(int width, int height) {
        difficulty.setMap(width, height);
        this.map = difficulty.getMap();
    }

    /**
     * Creates the player with the chosen brain, or the
     * difficulty's default brain if none was chosen.
     */
    public void createPlayer() {
        if (brain == null) {
            brain = difficulty.getBrain();
        }
        Vision vision = new WideVision(map);
        player = new Player(20, 15, 15, vision, brain, map);
    }

    /**
     * Runs the game loop until the player escapes, dies,
     * or the user quits. Each turn the screen is drawn,
     * then the game waits for the user to type n (next)
     * or q (quit).
     *
     * @param scanner the shared input scanner from Main
     */
    public void run(Scanner scanner) {
        turn = 1;
        boolean quit = false;

        while (!player.hasEscaped() && !player.isDead()) {
            drawScreen();

            if (!waitForNext(scanner)) {
                quit = true;
                break;
            }

            player.takeTurn();      // events are buffered, shown next draw
            turn++;

            if (turn > MAX_TURNS) {
                Display.printEvent("Turn limit reached.");
                break;
            }
        }

        // Final screen
        drawScreen();

        if (player.hasEscaped()) {
            Display.printWin(turn);
        } else if (quit) {
            System.out.println("  You left the wilderness after "
                               + turn + " turns.");
        } else {
            Display.printGameOver(turn);
        }
    }

    /**
     * Clears the screen and draws the map, stats, warnings,
     * and everything that happened on the last turn.
     */
    private void drawScreen() {
        Display.clearScreen();
        Display.drawMap(map, player.getRow(), player.getCol());
        Display.drawStats(player, turn);
        printWarnings();
        Display.flushEvents();
    }

    /**
     * Waits until the user types n (next turn) or q (quit).
     *
     * @param scanner the shared input scanner
     * @return true to play the next turn, false to quit
     */
    private boolean waitForNext(Scanner scanner) {
        while (true) {
            System.out.print("  [n] next turn   [q] quit: ");

            if (!scanner.hasNextLine()) {
                return false;       // input closed
            }
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("n")) {
                return true;
            }
            if (input.equals("q")) {
                return false;
            }
            System.out.println("  Please type n or q.");
        }
    }

    /**
     * Buffers resource warnings if any stat is critically low.
     */
    private void printWarnings() {
        double strRatio =
            (double) player.getCurrentStrength() / player.getMaxStrength();
        double waterRatio =
            (double) player.getCurrentWater() / player.getMaxWater();
        double foodRatio =
            (double) player.getCurrentFood() / player.getMaxFood();

        if (strRatio < 0.3) {
            Display.printWarning("Strength is critically low!");
        }
        if (waterRatio < 0.3) {
            Display.printWarning("Water is critically low!");
        }
        if (foodRatio < 0.3) {
            Display.printWarning("Food is critically low!");
        }
    }
}