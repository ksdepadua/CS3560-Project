import java.util.ArrayList;
import java.util.List;

/**
 * Handles all terminal visual output for WSS.
 * Uses ANSI escape codes for colors and styling.
 * Events are buffered each turn and printed together
 * below the stats, so the map only redraws once per turn.
 */
public class Display {

    // ANSI color codes
    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String RED    = "\u001B[31m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE   = "\u001B[34m";
    private static final String CYAN   = "\u001B[36m";
    private static final String WHITE  = "\u001B[37m";
    private static final String BG_BLACK = "\u001B[40m";

    private static final int BAR_LENGTH = 10;

    /** Buffered events for the current turn. */
    private static final List<String> events = new ArrayList<>();

    /**
     * Clears the terminal screen.
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        events.clear();
    }

    /**
     * Prints the WSS title banner.
     */
    public static void printTitle() {
        System.out.println(BOLD + GREEN);
        System.out.println("  oooo oooo  oooooo  oooooo ");
        System.out.println("  `  W  W'  S'       S'     ");
        System.out.println("    \\  /     `ooooo   `ooooo");
        System.out.println("     ||           S'       S'");
        System.out.println("     ||    oooooo'   oooooo'");
        System.out.println(RESET);
        System.out.println(
            BOLD + "  Starving Games Simulator" + RESET
        );
        System.out.println();
    }

    /**
     * Draws the full game map with the player's position.
     * Each terrain type has its own color and symbol.
     * Uses compact single-char cells for readability.
     *
     * @param map       the game map
     * @param playerRow the player's current row
     * @param playerCol the player's current column
     */
    public static void drawMap(Map map, int playerRow, int playerCol) {
        System.out.println(
            BOLD + "  MAP  " + RESET +
            GREEN  + "." + RESET + "=plains  " +
            YELLOW + "~" + RESET + "=desert  " +
            WHITE  + "^" + RESET + "=mountain  " +
            BLUE   + "%" + RESET + "=swamp  " +
            GREEN  + "F" + RESET + "=forest  " +
            BOLD + CYAN + "@" + RESET + "=you  " +
            "*=item  T=trader"
        );
        System.out.println();

        // Top border
        System.out.print("  +");
        for (int x = 0; x < map.getWidth(); x++) {
            System.out.print("-");
        }
        System.out.println("+");

        for (int y = 0; y < map.getHeight(); y++) {
            System.out.print("  |");
            for (int x = 0; x < map.getWidth(); x++) {
                Square sq = map.getSquare(x, y);
                if (y == playerRow && x == playerCol) {
                    System.out.print(
                        BOLD + BG_BLACK + CYAN + "@" + RESET
                    );
                } else {
                    System.out.print(getTerrainDisplay(sq));
                }
            }
            System.out.println("|");
        }

        // Bottom border
        System.out.print("  +");
        for (int x = 0; x < map.getWidth(); x++) {
            System.out.print("-");
        }
        System.out.println("+");
        System.out.println();
    }

    /**
     * Returns the colored single character for a square's terrain.
     * Shows T for trader, * for item, terrain char otherwise.
     *
     * @param sq the square to display
     * @return colored single character string
     */
    private static String getTerrainDisplay(Square sq) {
        String name = sq.getTerrain().getTerrainName().toLowerCase();
        boolean hasItem   = !sq.getItems().isEmpty();
        boolean hasTrader = sq.hasTrader();

        String symbol;
        String color;

        switch (name) {
            case "plains":
                color  = GREEN;
                symbol = hasTrader ? "T"
                       : hasItem   ? "*" : ".";
                break;
            case "mountain":
                color  = WHITE;
                symbol = hasTrader ? "T"
                       : hasItem   ? "*" : "^";
                break;
            case "desert":
                color  = YELLOW;
                symbol = hasTrader ? "T"
                       : hasItem   ? "*" : "~";
                break;
            case "swamp":
                color  = BLUE;
                symbol = hasTrader ? "T"
                       : hasItem   ? "*" : "%";
                break;
            case "forest":
                color  = GREEN;
                symbol = hasTrader ? "T"
                       : hasItem   ? "*" : "F";
                break;
            default:
                color  = WHITE;
                symbol = "?";
        }

        return color + symbol + RESET;
    }

    /**
     * Draws the player's current resource stats as bars.
     *
     * @param player the player whose stats to display
     * @param turn   the current turn number
     */
    public static void drawStats(Player player, int turn) {
        System.out.println(
            BOLD + "  -- Turn " + turn +
            "  |  Goal: reach col " +
            (player.getMap().getWidth() - 1) +
            "  |  You are at col " + player.getCol() +
            " --" + RESET
        );
        System.out.println();

        System.out.println(
            "  " + BOLD + "STR " + RESET +
            buildBar(
                player.getCurrentStrength(),
                player.getMaxStrength(), GREEN, RED
            ) +
            "  " + player.getCurrentStrength() +
            "/" + player.getMaxStrength()
        );

        System.out.println(
            "  " + BOLD + "H2O " + RESET +
            buildBar(
                player.getCurrentWater(),
                player.getMaxWater(), CYAN, RED
            ) +
            "  " + player.getCurrentWater() +
            "/" + player.getMaxWater()
        );

        System.out.println(
            "  " + BOLD + "FOOD" + RESET +
            buildBar(
                player.getCurrentFood(),
                player.getMaxFood(), YELLOW, RED
            ) +
            "  " + player.getCurrentFood() +
            "/" + player.getMaxFood()
        );

        System.out.println(
            "  " + BOLD + "GOLD" + RESET +
            " " + YELLOW + "* " +
            player.getCurrentGold() + RESET
        );
        System.out.println();
    }

    /**
     * Builds a colored progress bar string.
     *
     * @param current   current value
     * @param max       maximum value
     * @param fullColor color for filled bars
     * @param lowColor  color when critically low
     * @return formatted bar string
     */
    private static String buildBar(
            int current, int max,
            String fullColor, String lowColor) {
        int filled = (int) Math.round(
            (double) current / max * BAR_LENGTH
        );
        filled = Math.max(0, Math.min(BAR_LENGTH, filled));
        int empty = BAR_LENGTH - filled;

        boolean critical = (double) current / max < 0.3;
        String barColor  = critical ? lowColor : fullColor;

        StringBuilder bar = new StringBuilder("[");
        bar.append(barColor);
        for (int i = 0; i < filled; i++) bar.append("#");
        bar.append(RESET);
        for (int i = 0; i < empty; i++) bar.append(".");
        bar.append("]");
        return bar.toString();
    }

    /**
     * Buffers a game event to be shown at end of turn.
     * Call flushEvents() to print them all at once.
     *
     * @param message the event message
     */
    public static void printEvent(String message) {
        events.add(CYAN + "> " + RESET + message);
    }

    /**
     * Buffers a warning message.
     *
     * @param message the warning
     */
    public static void printWarning(String message) {
        System.out.println(
            "  " + BOLD + RED + "! " + message + RESET
        );
    }

    /**
     * Prints all buffered events from this turn, then clears them.
     */
    public static void flushEvents() {
        if (!events.isEmpty()) {
            System.out.println(
                BOLD + "  -- What happened --" + RESET
            );
            for (String e : events) {
                System.out.println("  " + e);
            }
            events.clear();
            System.out.println();
        }
    }

    /**
     * Prints the map legend.
     */
    public static void printLegend() {
        System.out.println(
            "  " + BOLD + "LEGEND:" + RESET +
            "  " + GREEN + "." + RESET + " plains  " +
            WHITE + "^" + RESET + " mountain  " +
            YELLOW + "~" + RESET + " desert  " +
            BLUE + "%" + RESET + " swamp  " +
            GREEN + "F" + RESET + " forest  " +
            "* item  T trader  " +
            CYAN + "@" + RESET + " you"
        );
        System.out.println();
    }

    /**
     * Prints the win screen.
     *
     * @param turns number of turns taken
     */
    public static void printWin(int turns) {
        System.out.println();
        System.out.println(BOLD + GREEN);
        System.out.println("  +--------------------------------+");
        System.out.println("  |  YOU ESCAPED THE WILDERNESS!  |");
        System.out.println("  +--------------------------------+");
        System.out.println(RESET);
        System.out.println("  Turns taken: " + turns);
        System.out.println();
    }

    /**
     * Prints the game over screen.
     *
     * @param turns number of turns taken
     */
    public static void printGameOver(int turns) {
        System.out.println();
        System.out.println(BOLD + RED);
        System.out.println("  +--------------------------------+");
        System.out.println("  |          GAME  OVER           |");
        System.out.println("  +--------------------------------+");
        System.out.println(RESET);
        System.out.println("  You survived " + turns + " turns.");
        System.out.println();
    }

    /**
     * Pauses briefly between turns so the player can follow.
     *
     * @param ms milliseconds to pause
     */
    public static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}