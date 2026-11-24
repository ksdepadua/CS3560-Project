/**
 * ANSI escape codes and special characters for terminal output.
 */
public class AnsiCodes {

    public static final String ESC = "\u001B[";

    // ---------- Styles ----------
    public static final String RESET = ESC + "0m";
    public static final String BOLD  = ESC + "1m";

    // ---------- Text (foreground) colors ----------
    public static final String BLACK      = ESC + "30m";
    public static final String RED        = ESC + "31m";
    public static final String GREEN      = ESC + "32m";
    public static final String DARK_GREEN = ESC + "38;5;22m";
    public static final String YELLOW     = ESC + "33m";
    public static final String BLUE       = ESC + "34m";
    public static final String CYAN       = ESC + "36m";
    public static final String WHITE      = ESC + "37m";

    // ---------- Background colors ----------
    public static final String BG_BLACK      = ESC + "40m";
    public static final String BG_GREEN      = ESC + "42m";
    public static final String BG_DARK_GREEN = ESC + "48;5;22m";
    public static final String BG_YELLOW     = ESC + "43m";
    public static final String BG_BLUE       = ESC + "44m";
    public static final String BG_WHITE      = ESC + "47m";

    // ---------- Screen control ----------
    public static final String CURSOR_HOME      = ESC + "H";
    public static final String CLEAR_SCREEN     = ESC + "2J";
    public static final String CLEAR_SCROLLBACK = ESC + "3J";

    // ---------- Block characters ----------
    public static final String FULL_BLOCK  = "█";   // █
    public static final String LIGHT_SHADE = "░";   // ░
}