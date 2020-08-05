package hu.webarticum.treeprinter;

public class UnicodeMode {
    
	private static boolean enabled = true;
    
	public static void setUnicodeAsDefault(boolean enabled) {
        UnicodeMode.enabled = enabled;
    }

	public static boolean isUnicodeDefault() {
        return enabled;
    }
    
}
