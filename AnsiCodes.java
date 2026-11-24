public class AnsiCodes {
    public static final String RESET  = "\u001B[0m";
    public static final String BOLD   = "\u001B[1m";
    public static final String RED    = "\u001B[31m";
    public static final String GREEN  = "\u001B[32m";
    public static final String DARK_GREEN = "\u001B[38;5;22m"; 
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[34m";
    public static final String CYAN   = "\u001B[36m";
    public static final String WHITE  = "\u001B[37m";
    public static final String BG_BLACK = "\u001B[40m";
    public static final String ESC          = "\u001B[";
    public static final String CURSOR_HOME  = ESC + "H";
    public static final String CLEAR_SCREEN = ESC + "2J";
    public static final String CLEAR_SCROLLBACK = ESC + "3J";  
}
