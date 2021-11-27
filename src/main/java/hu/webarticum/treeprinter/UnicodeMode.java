package hu.webarticum.treeprinter;

import hu.webarticum.treeprinter.printer.TreePrinter;

/**
 * Shared static class for managing global default unicode mode.
 * 
 * Built-in {@link TreePrinter} implementations use this setting
 * for choosing a default set of characters to draw
 */
public final class UnicodeMode {
    
    private static volatile boolean enabled = true;
    
    
    private UnicodeMode() {
        // static class
    }
    
    
    public static void setUnicodeAsDefault(boolean enabled) {
        UnicodeMode.enabled = enabled;
    }

    public static boolean isUnicodeDefault() {
        return enabled;
    }
    
}
