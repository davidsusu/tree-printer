package hu.webarticum.treeprinter.printer.boxing;

import java.util.List;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.printer.TreePrinter;
import hu.webarticum.treeprinter.printer.UnicodeMode;
import hu.webarticum.treeprinter.util.LineBuffer;
import hu.webarticum.treeprinter.util.Util;

// TODO: pad/margin
// TODO: horizontal layout (selective)
// TODO: alignment (start, end, justify)
// TODO: clean code
public class BoxingTreePrinter implements TreePrinter {

    private static final char[] BOX_CHARS_ASCII = new char[] {
        '.', '-', '.', '|', '\'', '-', '`', '|', '+', '+'
    };
    
    private static final char[] BOX_CHARS_UNICODE = new char[] {
        '┌', '─', '┐', '│', '┘', '─', '└', '│', '┤', '├'
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
    private final boolean displayPlaceholders;
    

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
        this.displayPlaceholders = builder.displayPlaceholders;
    }
    
    
    @Override
    public void print(TreeNode rootNode, Appendable out) {
        Util.writeln(out, getAsString(rootNode));
    }
    
    @Override
    public String getAsString(TreeNode rootNode) {
        List<TreeNode> children = rootNode.getChildren();
        if (!displayPlaceholders) {
            children.removeIf(TreeNode::isPlaceholder);
        }
        String content = rootNode.getContent();
        if (children.isEmpty()) {
            return boxContent(content);
        }
        
        StringBuilder resultBuilder = new StringBuilder();
        int[] dimensions = Util.getContentDimension(content);
        LineBuffer lineBuffer = new LineBuffer(resultBuilder);
        int leftOffset = 1;
        int topOffset = dimensions[1] + 2;
        int[] subDimensions = writeItemsVertically(lineBuffer, leftOffset, topOffset, children);
        int innerWidth = Math.max(dimensions[0] + 4, subDimensions[0]);
        int topDiff = writeTop(lineBuffer, content, dimensions, innerWidth);
        int verticalLineTop = topOffset - topDiff;
        int verticalLineHeight = subDimensions[1] + topDiff;
        writeBottom(lineBuffer, topOffset + subDimensions[1], innerWidth);
        writeLeft(lineBuffer, verticalLineTop, verticalLineHeight);
        writeRight(lineBuffer, leftOffset + innerWidth, verticalLineTop, verticalLineHeight);
        lineBuffer.flush();
        return resultBuilder.toString();
    }
    
    private int[] writeItemsVertically(
        LineBuffer lineBuffer, int leftOffset, int topOffset, List<TreeNode> nodes
    ) {
        int width = 0;
        int height = 0;
        for (TreeNode node : nodes) {
            String itemContent = getAsString(node);
            int[] childDimensions = Util.getContentDimension(itemContent);
            lineBuffer.write(topOffset + height, leftOffset, itemContent);
            height += childDimensions[1];
            if (childDimensions[0] > width) {
                width = childDimensions[0];
            }
        }
        return new int[] { width, height };
    }
    
    private int writeTop(LineBuffer lineBuffer, String content, int[] dimensions, int innerWidth) {
        int width = dimensions[0];
        int height = dimensions[1];
        int lineTop = height - (height / 2);
        int labelRight = width + 3;
        int labelBottom = height + 1;
        lineBuffer.write(lineTop, 0, topLeft + "");
        lineBuffer.write(lineTop, 1, top + "");
        lineBuffer.write(lineTop, 2, leftConnection + "");
        lineBuffer.write(0, 2, topLeft + "");
        lineBuffer.write(0, labelRight, topRight + "");
        lineBuffer.write(labelBottom, 2, bottomLeft + "");
        lineBuffer.write(labelBottom, labelRight, bottomRight + "");
        for (int i = 3; i < labelRight; i++) {
            lineBuffer.write(0, i, top + "");
            lineBuffer.write(labelBottom, i, top + "");
        }
        for (int i = 1; i < lineTop; i++) {
            lineBuffer.write(i, 2, left + "");
            lineBuffer.write(i, labelRight, right + "");
        }
        for (int i = lineTop + 1; i < labelBottom; i++) {
            lineBuffer.write(i, 2, left + "");
            lineBuffer.write(i, labelRight, right + "");
        }
        lineBuffer.write(lineTop, width + 3, rightConnection + "");
        for (int i = width + 4; i <= innerWidth; i++) {
            lineBuffer.write(lineTop, i, top + "");
        }
        lineBuffer.write(lineTop, innerWidth + 1, topRight + "");
        lineBuffer.write(1, 3, content);
        return height - lineTop + 1;
    }
    
    private void writeBottom(LineBuffer lineBuffer, int topOffset, int innerWidth) {
        lineBuffer.write(topOffset, 0, bottomLeft + "");
        for (int i = 1; i <= innerWidth; i++) {
            lineBuffer.write(topOffset, i, bottom + "");
        }
        lineBuffer.write(topOffset, innerWidth + 1, bottomRight + "");
        
    }

    private void writeLeft(LineBuffer lineBuffer, int topOffset, int height) {
        int until = topOffset + height;
        for (int i = topOffset; i < until; i++) {
            lineBuffer.write(i, 0, left + "");
        }
    }

    private void writeRight(LineBuffer lineBuffer, int leftOffset, int topOffset, int height) {
        int until = topOffset + height;
        for (int i = topOffset; i < until; i++) {
            lineBuffer.write(i, leftOffset, right + "");
        }
    }

    private String boxContent(String content) {
        String[] lines = Util.splitToLines(content);
        int width = Util.getMaxLength(lines);
        
        StringBuilder resultBuilder = new StringBuilder();
        
        resultBuilder.append(topLeft);
        Util.repeat(resultBuilder, top, width);
        resultBuilder.append(topRight);
        resultBuilder.append('\n');
        
        for (String line : lines) {
            resultBuilder.append(left);
            resultBuilder.append(line);
            Util.repeat(resultBuilder, ' ', width - line.length());
            resultBuilder.append(right);
            resultBuilder.append('\n');
        }
        
        resultBuilder.append(bottomLeft);
        Util.repeat(resultBuilder, bottom, width);
        resultBuilder.append(bottomRight);
        resultBuilder.append('\n');
        
        return resultBuilder.toString();
    }

    public static Builder createBuilder() {
        return new Builder();
    }
    
    public static class Builder {

        private char[] characters = (
            UnicodeMode.isUnicodeDefault() ?
            BOX_CHARS_UNICODE :
            BOX_CHARS_ASCII
        ).clone();

        private boolean displayPlaceholders = false;

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

        public Builder displayPlaceholders(boolean displayPlaceholders) {
            this.displayPlaceholders = displayPlaceholders;
            return this;
        }
        
        public BoxingTreePrinter build() {
            return new BoxingTreePrinter(this);
        }

    }

}
