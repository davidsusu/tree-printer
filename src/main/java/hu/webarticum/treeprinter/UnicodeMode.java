package hu.webarticum.treeprinter;

/**
 * Shared static class for managing global default unicode mode.
 * 
 * Built-in printers and node decorators use this setting at construction time
 * for choosing a default set of characters
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
