package hu.webarticum.treeprinter;

public class UnicodeMode {
    
    static private boolean enabled = true;
    
    static public void setUnicodeAsDefault(boolean enabled) {
        UnicodeMode.enabled = enabled;
    }

    static public boolean isUnicodeDefault() {
        return enabled;
    }
    
}
