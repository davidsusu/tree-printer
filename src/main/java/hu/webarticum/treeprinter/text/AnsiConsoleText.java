package hu.webarticum.treeprinter.text;

public class AnsiConsoleText implements ConsoleText {
    
    private final String ansiText;
    
    private volatile String plainText = null;
    
    
    public AnsiConsoleText(String ansiText) {
        this(ansiText, true);
    }

    AnsiConsoleText(String ansiText, boolean clean) {
        this.ansiText = clean ? TextUtil.cleanAnsi(ansiText) : ansiText;
    }
    

    @Override
    public String plain() {
        if (plainText == null) {
            synchronized (this) {
                if (plainText == null) {
                    plainText = TextUtil.ansiToPlain(ansiText);
                }
            }
        }
        return plainText;
    }

    @Override
    public String ansi() {
        return ansiText;
    }
    
    @Override
    public ConsoleText concat(ConsoleText consoleText) {
        return new AnsiConsoleText(this.ansiText + consoleText.ansi(), false);
    }

}
