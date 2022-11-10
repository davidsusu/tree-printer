package hu.webarticum.treeprinter;

import hu.webarticum.treeprinter.printer.TreePrinter;

/**
 * Shared static class for managing global ANSI output mode.
 * 
 * Built-in {@link TreePrinter} implementations use this setting.
 */
public final class AnsiMode {
    
    private static volatile boolean enabled = true;
    
    
    private AnsiMode() {
        // static class
    }
    
    
    public static void setAnsiEnabled(boolean enabled) {
        AnsiMode.enabled = enabled;
    }

    public static boolean isAnsiEnabled() {
        return enabled;
    }
    
}
