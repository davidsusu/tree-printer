package hu.webarticum.treeprinter.printer.boxing;

import java.util.List;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.printer.TreePrinter;
import hu.webarticum.treeprinter.printer.UnicodeMode;
import hu.webarticum.treeprinter.util.Util;

public class BoxingTreePrinter implements TreePrinter {

    private static final char[] BOX_CHARS_ASCII = new char[] {
        '.', '-', '.', '|', '\'', '-', '`', '|', '+', '+'
    };
    
    private static final char[] BOX_CHARS_UNICODE = new char[] {
        '┌', '─', '┐', '│', '┘', '─', '└', '│', '├', '┤'
    };

    private final char topLeft;
    private final char top;
    private final char topRight;
    private final char right;
    private final char bottomRight;
    private final char bottom;
    private final char bottomLeft;
    private final char left;
    private final char leftConnection;
    private final char rightConnection;
    

    public BoxingTreePrinter() {
        this(createBuilder());
    }

    private BoxingTreePrinter(Builder builder) {
        this.topLeft = builder.characters[0];
        this.top = builder.characters[1];
        this.topRight = builder.characters[2];
        this.right = builder.characters[3];
        this.bottomRight = builder.characters[4];
        this.bottom = builder.characters[5];
        this.bottomLeft = builder.characters[6];
        this.left = builder.characters[7];
        this.leftConnection = builder.characters[8];
        this.rightConnection = builder.characters[9];
    }
    
    
    @Override
    public void print(TreeNode rootNode, Appendable out) {
        Util.writeln(out, getAsString(rootNode));
    }
    
    @Override
    public String getAsString(TreeNode rootNode) {
        List<TreeNode> children = rootNode.getChildren();
        String content = rootNode.getContent();
        if (children.isEmpty()) {
            return boxContent(content);
        }
        
        // TODO
        return "";
        
    }
    
    private String boxContent(String content) {
        int[] dimension = Util.getContentDimension(content);

        // TODO
        return "";
        
    }

    public static Builder createBuilder() {
        return new Builder();
    }
    
    public static class Builder {

        private boolean decorable = true;
        private boolean inherit = true;
        private boolean forceInherit = false;

        private char[] characters = (
            UnicodeMode.isUnicodeDefault() ?
            BOX_CHARS_UNICODE :
            BOX_CHARS_ASCII
        ).clone();

        public Builder ascii() {
            this.characters = BOX_CHARS_ASCII.clone();
            return this;
        }
        
        public Builder unicode() {
            this.characters = BOX_CHARS_UNICODE.clone();
            return this;
        }

        public Builder topLeft(char topLeft) {
            this.characters[0] = topLeft;
            return this;
        }

        public Builder top(char top) {
            this.characters[1] = top;
            return this;
        }

        public Builder topRight(char topRight) {
            this.characters[2] = topRight;
            return this;
        }

        public Builder right(char right) {
            this.characters[3] = right;
            return this;
        }

        public Builder bottomRight(char bottomRight) {
            this.characters[4] = bottomRight;
            return this;
        }

        public Builder bottom(char bottom) {
            this.characters[5] = bottom;
            return this;
        }

        public Builder bottomLeft(char bottomLeft) {
            this.characters[6] = bottomLeft;
            return this;
        }

        public Builder left(char left) {
            this.characters[7] = left;
            return this;
        }

        public Builder leftConnection(char leftConnection) {
            this.characters[8] = leftConnection;
            return this;
        }

        public Builder rightConnection(char rightConnection) {
            this.characters[9] = rightConnection;
            return this;
        }
        
    }

}
