import java.util.ArrayList;
import java.util.List;

/**
 * Handles all terminal visual output for WSS.
 * Uses the ANSI escape codes in AnsiCodes for colors and styling.
 * Events are buffered each turn and printed together
 * below the stats, so the map only redraws once per turn.
 */
public class Display {

    private static final int BAR_LENGTH = 10;

    /** Buffered events for the current turn. */
    private static final List<String> events = new ArrayList<>();

    /**
     * Clears the terminal screen and moves the cursor to the top-left.
     * Buffered events are kept so they can be shown on the new screen.
     */
    public static void clearScreen() {
        System.out.print(
            AnsiCodes.CURSOR_HOME
            + AnsiCodes.CLEAR_SCREEN
            + AnsiCodes.CLEAR_SCROLLBACK
        );
        System.out.flush();
    }

    /**
     * Prints the WSS title banner.
     */
    public static void printTitle() {
        System.out.println(AnsiCodes.BOLD + AnsiCodes.GREEN);
        System.out.println("  oooo oooo  oooooo  oooooo ");
        System.out.println("  `  W  W'  S'       S'     ");
        System.out.println("    \\  /     `ooooo   `ooooo");
        System.out.println("     ||           S'       S'");
        System.out.println("     ||    oooooo'   oooooo'");
        System.out.println(AnsiCodes.RESET);
        System.out.println(
            AnsiCodes.BOLD + "  Wilderness Survival System" + AnsiCodes.RESET
        );
        System.out.println();
    }

    // ---------- Map ----------

    /**
     * Draws the full game map with the player's position.
     * Each terrain type has its own color and symbol.
     *
     * @param map       the game map
     * @param playerRow the player's current row
     * @param playerCol the player's current column
     */
    public static void drawMap(Map map, int playerRow, int playerCol) {
        printLegend();
        printBorder(map.getWidth());

        for (int y = 0; y < map.getHeight(); y++) {
            System.out.print("  |");
            for (int x = 0; x < map.getWidth(); x++) {
                if (y == playerRow && x == playerCol) {
                    System.out.print(
                        AnsiCodes.BOLD + AnsiCodes.BG_BLACK + AnsiCodes.CYAN
                        + "@" + AnsiCodes.RESET
                    );
                } else {
                    System.out.print(getSquareDisplay(map.getSquare(x, y)));
                }
            }
            System.out.println("|");
        }

        printBorder(map.getWidth());
        System.out.println();
    }

    /**
     * Prints a horizontal map border like +-----+.
     *
     * @param width the map width
     */
    private static void printBorder(int width) {
        System.out.println("  +" + "-".repeat(width) + "+");
    }

    /**
     * Returns the colored single character for a square.
     * Shows T for a trader, * for an uncollected item,
     * and the terrain symbol otherwise.
     *
     * @param sq the square to display
     * @return colored single-character string
     */
    private static String getSquareDisplay(Square sq) {
        String terrain = sq.getTerrain().getTerrainName().toLowerCase();
        String color   = terrainColor(terrain);

        String symbol;
        if (sq.hasTrader()) {
            symbol = "T";
        } else if (hasUncollectedItem(sq)) {
            symbol = "*";
        } else {
            symbol = terrainSymbol(terrain);
        }
        return color + symbol + AnsiCodes.RESET;
    }

    /**
     * Returns true if the square has an item that hasn't been taken.
     * Collected one-time items no longer show as *.
     *
     * @param sq the square to check
     * @return true if an item is still available
     */
    private static boolean hasUncollectedItem(Square sq) {
        for (Item item : sq.getItems()) {
            if (!item.isTaken()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the color used for a terrain type.
     *
     * @param terrain lowercase terrain name
     * @return ANSI color code
     */
    private static String terrainColor(String terrain) {
        switch (terrain) {
            case "plains":   return AnsiCodes.GREEN;
            case "forest":   return AnsiCodes.DARK_GREEN;
            case "desert":   return AnsiCodes.YELLOW;
            case "swamp":    return AnsiCodes.BLUE;
            case "mountain": return AnsiCodes.WHITE;
            default:         return AnsiCodes.WHITE;
        }
    }

    /**
     * Returns the symbol used for a terrain type.
     *
     * @param terrain lowercase terrain name
     * @return single-character symbol
     */
    private static String terrainSymbol(String terrain) {
        switch (terrain) {
            case "plains":   return ".";
            case "forest":   return "F";
            case "desert":   return "~";
            case "swamp":    return "%";
            case "mountain": return "^";
            default:         return "?";
        }
    }

    /**
     * Prints the map legend.
     */
    public static void printLegend() {
        System.out.println(
            "  " + AnsiCodes.BOLD + "LEGEND:" + AnsiCodes.RESET + "  "
            + legendEntry("plains")
            + legendEntry("forest")
            + legendEntry("desert")
            + legendEntry("swamp")
            + legendEntry("mountain")
            + "* item  T trader  "
            + AnsiCodes.BOLD + AnsiCodes.CYAN + "@" + AnsiCodes.RESET + " you"
        );
        System.out.println();
    }

    /**
     * Builds one colored legend entry, e.g. "F forest  ".
     *
     * @param terrain lowercase terrain name
     * @return legend text
     */
    private static String legendEntry(String terrain) {
        return terrainColor(terrain) + terrainSymbol(terrain)
             + AnsiCodes.RESET + " " + terrain + "  ";
    }

    // ---------- Stats ----------

    /**
     * Draws the player's current resource stats as bars.
     *
     * @param player the player whose stats to display
     * @param turn   the current turn number
     */
    public static void drawStats(Player player, int turn) {
        System.out.println(
            AnsiCodes.BOLD + "  -- Turn " + turn
            + "  |  Goal: reach col " + (player.getMap().getWidth() - 1)
            + "  |  You are at col " + player.getCol()
            + " --" + AnsiCodes.RESET
        );
        System.out.println();

        printStatBar("STR ", player.getCurrentStrength(),
                     player.getMaxStrength(), AnsiCodes.GREEN);
        printStatBar("H2O ", player.getCurrentWater(),
                     player.getMaxWater(), AnsiCodes.CYAN);
        printStatBar("FOOD", player.getCurrentFood(),
                     player.getMaxFood(), AnsiCodes.YELLOW);

        System.out.println(
            "  " + AnsiCodes.BOLD + "GOLD" + AnsiCodes.RESET
            + " " + AnsiCodes.YELLOW + "* " + player.getCurrentGold()
            + AnsiCodes.RESET
        );
        System.out.println();
    }

    /**
     * Prints one labeled stat bar, e.g. "STR [#####.....]  5/10".
     *
     * @param label     4-character label
     * @param current   current value
     * @param max       maximum value
     * @param fullColor color when not critically low
     */
    private static void printStatBar(
            String label, int current, int max, String fullColor) {
        System.out.println(
            "  " + AnsiCodes.BOLD + label + AnsiCodes.RESET
            + buildBar(current, max, fullColor, AnsiCodes.RED)
            + "  " + current + "/" + max
        );
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
        int filled = (int) Math.round((double) current / max * BAR_LENGTH);
        filled = Math.max(0, Math.min(BAR_LENGTH, filled));
        int empty = BAR_LENGTH - filled;

        boolean critical = (double) current / max < 0.3;
        String barColor  = critical ? lowColor : fullColor;

        return "[" + barColor + "#".repeat(filled) + AnsiCodes.RESET
             + ".".repeat(empty) + "]";
    }

    // ---------- Events ----------

    /**
     * Buffers a game event to be shown at end of turn.
     * Call flushEvents() to print them all at once.
     *
     * @param message the event message
     */
    public static void printEvent(String message) {
        events.add(AnsiCodes.CYAN + "> " + AnsiCodes.RESET + message);
    }

    /**
     * Buffers a successful event (such as a completed trade)
     * in dark green.
     *
     * @param message the event message
     */
    public static void printSuccess(String message) {
        events.add(AnsiCodes.DARK_GREEN + "> " + message + AnsiCodes.RESET);
    }

    /**
     * Buffers a warning to be shown with this turn's events.
     *
     * @param message the warning
     */
    public static void printWarning(String message) {
        events.add(AnsiCodes.BOLD + AnsiCodes.RED + "! " + message
                   + AnsiCodes.RESET);
    }

    /**
     * Prints all buffered events from this turn, then clears them.
     */
    public static void flushEvents() {
        if (events.isEmpty()) {
            return;
        }
        System.out.println(
            AnsiCodes.BOLD + "  -- What happened --" + AnsiCodes.RESET
        );
        for (String e : events) {
            System.out.println("  " + e);
        }
        events.clear();
        System.out.println();
    }

    // ---------- End screens ----------

    /**
     * Prints the win screen.
     *
     * @param turns number of turns taken
     */
    public static void printWin(int turns) {
        printBanner(AnsiCodes.GREEN, "YOU ESCAPED THE WILDERNESS!");
        System.out.println("  Turns taken: " + turns);
        System.out.println();
    }

    /**
     * Prints the game over screen.
     *
     * @param turns number of turns taken
     */
    public static void printGameOver(int turns) {
        printBanner(AnsiCodes.RED, "GAME  OVER");
        System.out.println("  You survived " + turns + " turns.");
        System.out.println();
    }

    /**
     * Prints a boxed, centered message in the given color.
     *
     * @param color   ANSI color code
     * @param message text inside the box
     */
    private static void printBanner(String color, String message) {
        final int innerWidth = 32;
        int left  = (innerWidth - message.length()) / 2;
        int right = innerWidth - message.length() - left;
        String line = "  +" + "-".repeat(innerWidth) + "+";

        System.out.println();
        System.out.println(AnsiCodes.BOLD + color + line);
        System.out.println("  |" + " ".repeat(left) + message
                           + " ".repeat(right) + "|");
        System.out.println(line + AnsiCodes.RESET);
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