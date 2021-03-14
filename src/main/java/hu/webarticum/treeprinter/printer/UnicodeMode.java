package hu.webarticum.treeprinter.printer;

public final class UnicodeMode {
    
    private static boolean enabled = true;
    
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
