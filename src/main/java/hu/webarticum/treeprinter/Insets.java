package hu.webarticum.treeprinter;

/**
 * Value class for storing inset values at top, right, bottom and left
 */
public class Insets {
    
    public static final Insets EMPTY = new Insets(0);
    
    
    private final int top;
    
    private final int right;
    
    private final int bottom;
    
    private final int left;
    
    
    public Insets(int inset) {
        this(inset, inset, inset, inset);
    }

    public Insets(int vertical, int horizontal) {
        this.top = vertical;
        this.right = horizontal;
        this.bottom = vertical;
        this.left = horizontal;
    }

    public Insets(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    private Insets(Builder builder) {
        this.top = builder.top;
        this.right = builder.right;
        this.bottom = builder.bottom;
        this.left = builder.left;
    }

    public static Builder builder() {
        return new Builder();
    }
    

    public int top() {
        return top;
    }

    public int right() {
        return right;
    }

    public int bottom() {
        return bottom;
    }

    public int left() {
        return left;
    }

    public Insets extendedWith(int inset) {
        return new Insets(
                this.top + inset,
                this.right + inset,
                this.bottom + inset,
                this.left + inset);
    }
    
    public Insets extendedWith(Insets other) {
        return new Insets(
                this.top + other.top,
                this.right + other.right,
                this.bottom + other.bottom,
                this.left + other.left);
    }
    
    
    public static class Builder {

        private int top = 0;
        
        private int right = 0;
        
        private int bottom = 0;
        
        private int left = 0;
        

        public void top(int top) {
            this.top = top;
        }
        
        public void right(int right) {
            this.right = right;
        }
        
        public void bottom(int bottom) {
            this.bottom = bottom;
        }
        
        public void left(int left) {
            this.left = left;
        }
        
        public void all(int all) {
            this.top = all;
            this.right = all;
            this.bottom = all;
            this.left = all;
        }

        public void vertical(int vertical) {
            this.top = vertical;
            this.bottom = vertical;
        }

        public void horizontal(int horizontal) {
            this.right = horizontal;
            this.left = horizontal;
        }

        public Insets build() {
            if (top == 0 && right == 0 && bottom == 0 && left == 0) {
                return Insets.EMPTY;
            }
            
            return new Insets(this);
        }

    }
    
}
