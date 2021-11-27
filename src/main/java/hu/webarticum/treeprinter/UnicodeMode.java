package hu.webarticum.treeprinter;

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
