package hu.webarticum.treeprinter.text;

public interface ConsoleText {
    
    public String plain();
    
    public String ansi();
    
    public ConsoleText concat(ConsoleText consoleText);
    
    public default int length() {
        return plain().length();
    }
    
    public default Dimensions dimensions() {
        String plainContent = plain();
        String[] lines = TextUtil.linesOf(plainContent);
        int width = 0;
        int height = 0;
        for (int i = 0; i < lines.length; i++) {
            int length = lines[i].length();
            if (length > width) {
                width = length;
            }
            height++;
        }
        return new Dimensions(width, height);
    }
    

    public static ConsoleText empty() {
        return new PlainConsoleText("");
    }

    public static ConsoleText of(char plainChar) {
        return new PlainConsoleText(plainChar);
    }

    public static ConsoleText of(String plainText) {
        return new PlainConsoleText(plainText);
    }

    public static ConsoleText ofAnsi(String ansiText) {
        return new AnsiConsoleText(ansiText);
    }
    
    
    public default ConsoleText format(AnsiFormat format) {
        String formatText = format.toString();
        if (formatText.isEmpty()) {
            return this;
        }
        
        return ofAnsi(formatText + ansi() + TextUtil.ansiReset());
    }
    
}
