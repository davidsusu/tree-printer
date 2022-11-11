package hu.webarticum.treeprinter.printer.boxing;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hu.webarticum.treeprinter.AnsiMode;
import hu.webarticum.treeprinter.Insets;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.UnicodeMode;
import hu.webarticum.treeprinter.printer.TreePrinter;
import hu.webarticum.treeprinter.text.AnsiFormat;
import hu.webarticum.treeprinter.text.ConsoleText;
import hu.webarticum.treeprinter.text.Dimensions;
import hu.webarticum.treeprinter.text.LineBuffer;
import hu.webarticum.treeprinter.text.TextUtil;
import hu.webarticum.treeprinter.util.Util;

/**
 * {@link TreePrinter} implementation that draws nested boxes.
 * 
 * <p>Example output with the default settings:</p>
 * 
 * <pre>
 *   ┌────┐
 * ┌─┤Root├───────────────────────────────────────┐
 * │ └────┘                                       │
 * │ ┌──────┐   ┌──────┐          ┌──────┐        │
 * │ │Child1│ ┌─┤Child2├──────┐ ┌─┤Child3├──────┐ │
 * │ └──────┘ │ └──────┘      │ │ └──────┘      │ │
 * │          │ ┌───────────┐ │ │ ┌───────────┐ │ │
 * │          │ │Grandchild1│ │ │ │Grandchild3│ │ │
 * │          │ └───────────┘ │ │ └───────────┘ │ │
 * │          │ ┌───────────┐ │ └───────────────┘ │
 * │          │ │Grandchild2│ │                   │
 * │          │ └───────────┘ │                   │
 * │          └───────────────┘                   │
 * └──────────────────────────────────────────────┘
 * </pre>
 */
public class BoxingTreePrinter implements TreePrinter {

    private static final char[] BOX_CHARS_ASCII = new char[] {
            '.', '-', '.', '|', '\'', '-', '`', '|', '+', '+' };
    
    private static final char[] BOX_CHARS_UNICODE = new char[] {
            '┌', '─', '┐', '│', '┘', '─', '└', '│', '┤', '├' };


    private final boolean displayPlaceholders;
    
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
    
    private final Set<Integer> horizontalLevels;

    private final boolean boxLeafs;

    private final Insets insets;

    private final int verticalGap;

    private final int horizontalGap;
    
    private final AnsiMode ansiMode;
    
    private final AnsiFormat defaultFormat;
    
    private final Map<Integer, AnsiFormat> levelFormats;
    

    public BoxingTreePrinter() {
        this(builder());
    }

    public BoxingTreePrinter(AnsiFormat borderFormat) {
        this(builder().defaultFormat(borderFormat));
    }

    private BoxingTreePrinter(Builder builder) {
        this.displayPlaceholders = builder.displayPlaceholders;
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
        this.horizontalLevels = new HashSet<>(builder.horizontalLevels);
        this.boxLeafs = builder.boxLeafs;
        this.insets = builder.insets;
        this.verticalGap = builder.verticalGap;
        this.horizontalGap = builder.horizontalGap;
        this.ansiMode = builder.ansiMode;
        this.defaultFormat = builder.defaultFormat;
        this.levelFormats = new HashMap<>(builder.levelFormats);
    }

    public static Builder builder() {
        return new Builder();
    }
    
    
    @Override
    public void print(TreeNode rootNode, Appendable out) {
        Util.writeln(out, stringify(rootNode));
    }
    
    @Override
    public String stringify(TreeNode rootNode) {
        return getLevelAsString(rootNode, 0);
    }
    
    private String getLevelAsString(TreeNode rootNode, int level) {
        List<TreeNode> children = rootNode.children();
        if (!displayPlaceholders) {
            children.removeIf(TreeNode::isPlaceholder);
        }
        
        ConsoleText rootContent = rootNode.content();
        Dimensions dimensions = rootContent.dimensions();
        if (children.isEmpty()) {
            return boxIfEnabled(rootContent, level);
        }
        
        StringBuilder resultBuilder = new StringBuilder();
        LineBuffer lineBuffer = Util.createLineBuffer(resultBuilder, ansiMode);
        int leftOffset = 1 + insets.left();
        int topHeight = dimensions.height() + 2;
        int topOffset = topHeight + insets.top();
        Dimensions subDimensions;
        if (horizontalLevels.contains(level)) {
            subDimensions = writeItemsHorizontally(lineBuffer, leftOffset, topOffset, children, level);
        } else {
            subDimensions = writeItemsVertically(lineBuffer, leftOffset, topOffset, children, level);
        }
        int innerWidth = Math.max(dimensions.width() + 4, subDimensions.width() + insets.left() + insets.right());
        int topDiff = writeTop(lineBuffer, level, rootContent, innerWidth);
        int verticalLineTop = topHeight - topDiff;
        int verticalLineHeight = subDimensions.height() + topDiff + insets.top() + insets.bottom();
        int bottomOffset = topOffset + subDimensions.height() + insets.bottom();
        writeBottom(lineBuffer, level, bottomOffset, innerWidth);
        writeLeft(lineBuffer, level, verticalLineTop, verticalLineHeight);
        writeRight(lineBuffer, level, innerWidth + 1, verticalLineTop, verticalLineHeight);
        lineBuffer.flush();
        return resultBuilder.toString();
    }
    
    private Dimensions writeItemsVertically(
            LineBuffer lineBuffer, int leftOffset, int topOffset, List<TreeNode> nodes, int level) {
        int width = 0;
        int height = 0;
        boolean first = true;
        for (TreeNode node : nodes) {
            if (first) {
                first = false;
            } else {
                height += verticalGap;
            }
            String itemContentString = getLevelAsString(node, level + 1);
            ConsoleText itemContent = Util.toConsoleText(itemContentString, ansiMode);
            Dimensions childDimensions = itemContent.dimensions();
            lineBuffer.write(topOffset + height, leftOffset, itemContent);
            height += childDimensions.height();
            int childWidth = childDimensions.width();
            if (childWidth > width) {
                width = childWidth;
            }
        }
        return new Dimensions(width, height);
    }

    private Dimensions writeItemsHorizontally(
            LineBuffer lineBuffer, int leftOffset, int topOffset, List<TreeNode> nodes, int level) {
        int width = 0;
        int height = 0;
        boolean first = true;
        for (TreeNode node : nodes) {
            if (first) {
                first = false;
            } else {
                width += horizontalGap;
            }
            String itemContentString = getLevelAsString(node, level + 1);
            ConsoleText itemContent = Util.toConsoleText(itemContentString, ansiMode);
            Dimensions childDimensions = itemContent.dimensions();
            lineBuffer.write(topOffset, leftOffset + width, itemContent);
            width += childDimensions.width();
            int childHeight = childDimensions.height();
            if (childHeight > height) {
                height = childHeight;
            }
        }
        return new Dimensions(width, height);
    }
    
    private int writeTop(LineBuffer lineBuffer, int level, ConsoleText content, int innerWidth) {
        lineBuffer.write(1, 3, content);
        return writeTopLines(lineBuffer, level, content.dimensions(), innerWidth);
    }

    private int writeTopLines(LineBuffer lineBuffer, int level, Dimensions contentDimensions, int innerWidth) {
        int width = contentDimensions.width();
        int height = contentDimensions.height();
        int lineTop = height - (height / 2);
        int labelRight = width + 3;
        int labelBottom = height + 1;
        
        lineBuffer.write(0, 2, formatLining(composeRoofString(width), level));
        lineBuffer.write(lineTop, 0, formatLining(composeLeftAsideRoofString(), level));
        lineBuffer.write(lineTop, labelRight, formatLining(composeRightAsideRoofString(innerWidth - labelRight), level));
        lineBuffer.write(labelBottom, 2, formatLining(composeBeddingString(width), level));
        for (int i = 1; i < lineTop; i++) {
            lineBuffer.write(i, 2, formatLining(left, level));
            lineBuffer.write(i, labelRight, formatLining(right, level));
        }
        for (int i = lineTop + 1; i < labelBottom; i++) {
            lineBuffer.write(i, 2, formatLining(left, level));
            lineBuffer.write(i, labelRight, formatLining(right, level));
        }
        
        return height - lineTop + 1;
    }
    
    private void writeBottom(LineBuffer lineBuffer, int level, int topOffset, int innerWidth) {
        lineBuffer.write(topOffset, 0, formatLining(composeBeddingString(innerWidth), level));
    }

    private void writeLeft(LineBuffer lineBuffer, int level, int topOffset, int height) {
        int until = topOffset + height;
        for (int i = topOffset; i < until; i++) {
            lineBuffer.write(i, 0, formatLining(left, level));
        }
    }

    private void writeRight(LineBuffer lineBuffer, int level, int leftOffset, int topOffset, int height) {
        int until = topOffset + height;
        for (int i = topOffset; i < until; i++) {
            lineBuffer.write(i, leftOffset, formatLining(right, level));
        }
    }
    
    private String boxIfEnabled(ConsoleText content, int level) {
        return boxLeafs ? boxContent(content, level) : Util.getStringContent(content, ansiMode);
    }

    private String boxContent(ConsoleText content, int level) {
        StringBuilder resultBuilder = new StringBuilder();
        
        Dimensions dimensions = content.dimensions();
        ConsoleText[] consoleTextLines = TextUtil.linesOf(content);
        int width = dimensions.width();
        
        resultBuilder.append(Util.getStringContent(formatLining(composeRoofString(width), level), ansiMode));
        resultBuilder.append('\n');
        
        for (ConsoleText consoleTextLine : consoleTextLines) {
            resultBuilder.append(Util.getStringContent(formatLining(left, level), ansiMode));
            resultBuilder.append(Util.getStringContent(consoleTextLine, ansiMode));
            TextUtil.repeat(resultBuilder, ' ', width - consoleTextLine.dimensions().width());
            resultBuilder.append(Util.getStringContent(formatLining(right, level), ansiMode));
            resultBuilder.append('\n');
        }

        resultBuilder.append(Util.getStringContent(formatLining(composeBeddingString(width), level), ansiMode));
        resultBuilder.append('\n');
        
        return resultBuilder.toString();
    }

    private ConsoleText formatLining(char liningChar, int level) {
        return formatLining("" + liningChar, level);
    }
    
    private ConsoleText formatLining(String liningText, int level) {
        AnsiFormat levelFormat = levelFormats.getOrDefault(level, defaultFormat);
        return ConsoleText.of(liningText).format(levelFormat);
    }
    
    private String composeLeftAsideRoofString() {
        return "" + topLeft + top + leftConnection;
    }

    private String composeRightAsideRoofString(int rightWidth) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(rightConnection);
        TextUtil.repeat(resultBuilder, top, rightWidth);
        resultBuilder.append(topRight);
        return resultBuilder.toString();
    }

    private String composeRoofString(int innerWidth) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(topLeft);
        TextUtil.repeat(resultBuilder, top, innerWidth);
        resultBuilder.append(topRight);
        return resultBuilder.toString();
    }

    private String composeBeddingString(int innerWidth) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(bottomLeft);
        TextUtil.repeat(resultBuilder, bottom, innerWidth);
        resultBuilder.append(bottomRight);
        return resultBuilder.toString();
    }
    
    
    public static class Builder {

        private boolean displayPlaceholders = false;

        private char[] characters =
                UnicodeMode.isUnicodeDefault() ?
                BOX_CHARS_UNICODE.clone() :
                BOX_CHARS_ASCII.clone();
        
        private Set<Integer> horizontalLevels = new HashSet<>(Arrays.asList(0));
        
        private boolean boxLeafs = true;

        private Insets insets = new Insets(0, 1);

        private int verticalGap = 0;

        private int horizontalGap = 1;
        
        private AnsiMode ansiMode = AnsiMode.AUTO;
        
        private AnsiFormat defaultFormat = AnsiFormat.NONE;
        
        private Map<Integer, AnsiFormat> levelFormats = new HashMap<>();
        
        
        public Builder displayPlaceholders(boolean displayPlaceholders) {
            this.displayPlaceholders = displayPlaceholders;
            return this;
        }
        
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

        public Builder horizontalLevels(Integer... horizontalLevels) {
            return horizontalLevels(Arrays.asList(horizontalLevels));
        }

        public Builder horizontalLevels(Collection<Integer> horizontalLevels) {
            this.horizontalLevels.clear();
            this.horizontalLevels.addAll(horizontalLevels);
            return this;
        }

        public Builder boxLeafs(boolean boxLeafs) {
            this.boxLeafs = boxLeafs;
            return this;
        }

        public Builder insets(Insets insets) {
            this.insets = insets;
            return this;
        }

        public Builder verticalGap(int verticalGap) {
            this.verticalGap = verticalGap;
            return this;
        }

        public Builder horizontalGap(int horizontalGap) {
            this.horizontalGap = horizontalGap;
            return this;
        }
        
        public Builder ansiMode(AnsiMode ansiMode) {
            this.ansiMode = ansiMode;
            return this;
        }
        
        public Builder defaultFormat(AnsiFormat defaultFormat) {
            this.defaultFormat = defaultFormat;
            return this;
        }
        
        public Builder levelFormats(Map<Integer, AnsiFormat> levelFormats) {
            this.levelFormats = new HashMap<>(levelFormats);
            return this;
        }

        public Builder levelFormat(int level, AnsiFormat formats) {
            this.levelFormats.put(level, formats);
            return this;
        }
        
        public BoxingTreePrinter build() {
            return new BoxingTreePrinter(this);
        }

    }

}
