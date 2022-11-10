package hu.webarticum.treeprinter.text;

public class AnsiFormat {
    
    public static final AnsiFormat NONE = new AnsiFormat("", false);

    public static final AnsiFormat BOLD = new AnsiFormat("\u001B[1m", false);

    public static final AnsiFormat FAINT = new AnsiFormat("\u001B[2m", false);

    public static final AnsiFormat ITALIC = new AnsiFormat("\u001B[3m", false);

    public static final AnsiFormat UNDERLINE = new AnsiFormat("\u001B[4m", false);

    public static final AnsiFormat BLINKING = new AnsiFormat("\u001B[5m", false);

    public static final AnsiFormat STRIKE = new AnsiFormat("\u001B[9m", false);

    public static final AnsiFormat BLACK = new AnsiFormat("\u001B[30m", false);

    public static final AnsiFormat RED = new AnsiFormat("\u001B[31m", false);

    public static final AnsiFormat GREEN = new AnsiFormat("\u001B[32m", false);

    public static final AnsiFormat YELLOW = new AnsiFormat("\u001B[33m", false);

    public static final AnsiFormat BLUE = new AnsiFormat("\u001B[34m", false);

    public static final AnsiFormat MAGENTA = new AnsiFormat("\u001B[35m", false);

    public static final AnsiFormat CYAN = new AnsiFormat("\u001B[36m", false);

    public static final AnsiFormat WHITE = new AnsiFormat("\u001B[37m", false);

    public static final AnsiFormat BRIGHT_BLACK = new AnsiFormat("\u001B[90m", false);

    public static final AnsiFormat BRIGHT_RED = new AnsiFormat("\u001B[91m", false);

    public static final AnsiFormat BRIGHT_GREEN = new AnsiFormat("\u001B[92m", false);

    public static final AnsiFormat BRIGHT_YELLOW = new AnsiFormat("\u001B[93m", false);

    public static final AnsiFormat BRIGHT_BLUE = new AnsiFormat("\u001B[94m", false);

    public static final AnsiFormat BRIGHT_MAGENTA = new AnsiFormat("\u001B[95m", false);

    public static final AnsiFormat BRIGHT_CYAN = new AnsiFormat("\u001B[96m", false);

    public static final AnsiFormat BRIGHT_WHITE = new AnsiFormat("\u001B[97m", false);

    public static final AnsiFormat BG_BLACK = new AnsiFormat("\u001B[40m", false);

    public static final AnsiFormat BG_RED = new AnsiFormat("\u001B[41m", false);

    public static final AnsiFormat BG_GREEN = new AnsiFormat("\u001B[42m", false);

    public static final AnsiFormat BG_YELLOW = new AnsiFormat("\u001B[43m", false);

    public static final AnsiFormat BG_BLUE = new AnsiFormat("\u001B[44m", false);

    public static final AnsiFormat BG_MAGENTA = new AnsiFormat("\u001B[45m", false);

    public static final AnsiFormat BG_CYAN = new AnsiFormat("\u001B[46m", false);

    public static final AnsiFormat BG_WHITE = new AnsiFormat("\u001B[47m", false);

    public static final AnsiFormat BG_BRIGHT_BLACK = new AnsiFormat("\u001B[100m", false);

    public static final AnsiFormat BG_BRIGHT_RED = new AnsiFormat("\u001B[101m", false);

    public static final AnsiFormat BG_BRIGHT_GREEN = new AnsiFormat("\u001B[102m", false);

    public static final AnsiFormat BG_BRIGHT_YELLOW = new AnsiFormat("\u001B[103m", false);

    public static final AnsiFormat BG_BRIGHT_BLUE = new AnsiFormat("\u001B[104m", false);

    public static final AnsiFormat BG_BRIGHT_MAGENTA = new AnsiFormat("\u001B[105m", false);

    public static final AnsiFormat BG_BRIGHT_CYAN = new AnsiFormat("\u001B[106m", false);

    public static final AnsiFormat BG_BRIGHT_WHITE = new AnsiFormat("\u001B[107m", false);

    
    private final String format;
    
    
    private AnsiFormat(String format, boolean doClean) {
        this.format = doClean ? TextUtil.cleanAnsi(format) : format;
    }
    
    
    public static AnsiFormat of(String format) {
        return new AnsiFormat(format, true);
    }
    

    public AnsiFormat compose(AnsiFormat wrapperFormat) {
        // TODO: compose to single ANSI escape if possible
        return new AnsiFormat(wrapperFormat.format + format, false);
    }
    
    public String toString() {
        return format;
    }
    
}
