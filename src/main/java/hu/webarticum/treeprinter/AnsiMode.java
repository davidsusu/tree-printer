package hu.webarticum.treeprinter;

import hu.webarticum.treeprinter.printer.TreePrinter;

/**
 * Shared static class for managing global ANSI output mode.
 * 
 * Built-in {@link TreePrinter} implementations use this setting.
 */
public enum AnsiMode {
    
    AUTO {
        
        public boolean isEnabled() {
            return enabled;
        }
        
    },
    
    ENABLED {
        
        public boolean isEnabled() {
            return true;
        }
        
    },
    
    DISABLED {
        
        public boolean isEnabled() {
            return false;
        }
        
    },
    
    ;
    
    
    private static volatile boolean enabled = true;
    
    
    public static void setAnsiAsDefault(boolean enabled) {
        AnsiMode.enabled = enabled;
    }

    public static boolean isAnsiAsDefault() {
        return enabled;
    }

    
    public abstract boolean isEnabled();
    
}
