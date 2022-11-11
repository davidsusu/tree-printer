package hu.webarticum.treeprinter.printer.listing;

import java.util.ArrayList;
import java.util.List;

import hu.webarticum.treeprinter.AnsiMode;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.UnicodeMode;
import hu.webarticum.treeprinter.printer.TreePrinter;
import hu.webarticum.treeprinter.text.AnsiFormat;
import hu.webarticum.treeprinter.text.ConsoleText;
import hu.webarticum.treeprinter.text.TextUtil;
import hu.webarticum.treeprinter.util.Util;

/**
 * {@link TreePrinter} implementation that draws nested lists with lines.
 * 
 * <p>Example output with the default settings:</p>
 * 
 * <pre>
 * Root
 *  ├─Child1
 *  ├─Child2
 *  │  ├─Grandchild1
 *  │  └─Grandchild2
 *  └─Child3
 *     └─Grandchild3
 * </pre>
 */
public class ListingTreePrinter implements TreePrinter {

    private enum NodeDisposition { ROOT, GENERAL, LAST }
    
    
    private static final String[] DEFAULT_ASCII_LINE_STRINGS = new String[] {
            "   ", " | ", " |-", " '-", "---", "-+-" };

    private static final String[] DEFAULT_UNICODE_LINE_STRINGS = new String[] {
            "   ", " │ ", " ├─", " └─", "───", "─┬─" };
    

    private final String liningSpace;
    
    private final String liningGeneral;
    
    private final String liningNode;
    
    private final String liningLastNode;
    
    private final String liningInset;
    
    private final String liningSub;
    
    private final boolean displayRoot;
    
    private final boolean displayPlaceholders;
    
    private final boolean align;
    
    private final boolean connectAlignedChildren;
    
    private final AnsiMode ansiMode;

    private final AnsiFormat liningFormat;

    
    public ListingTreePrinter() {
        this(builder());
    }

    public ListingTreePrinter(AnsiFormat liningFormat) {
        this(builder().liningFormat(liningFormat));
    }

    private ListingTreePrinter(Builder builder) {
        this.liningSpace = builder.lines[0];
        this.liningGeneral = builder.lines[1];
        this.liningNode = builder.lines[2];
        this.liningLastNode = builder.lines[3];
        this.liningInset = builder.lines[4];
        this.liningSub = builder.lines[5];
        this.displayRoot = builder.displayRoot;
        this.displayPlaceholders = builder.displayPlaceholders;
        this.align = builder.align;
        this.connectAlignedChildren = builder.connectAlignedChildren;
        this.ansiMode = builder.ansiMode;
        this.liningFormat = builder.liningFormat;
    }

    public static Builder builder() {
        return new Builder();
    }
    

    @Override
    public void print(TreeNode rootNode, Appendable out) {
        printSub(rootNode, out, "", NodeDisposition.ROOT, align ? Util.getDepth(rootNode) : 0);
    }
    
    private void printSub(
            TreeNode node, Appendable out, String prefix, NodeDisposition disposition, int inset) {
        String content = Util.getStringContent(node.content(), ansiMode);
        
        int connectOffset = node.insets().top();

        List<TreeNode> childNodes = new ArrayList<>(node.children());
        if (!displayPlaceholders) {
            childNodes.removeIf(TreeNode::isPlaceholder);
        }
        
        String[] lines = TextUtil.linesOf(content);
        for (int i = 0; i < lines.length; i++) {
            printContentLine(out, prefix, disposition, !childNodes.isEmpty(), inset, connectOffset, i, lines[i]);
        }
        
        int childNodeCount = childNodes.size();
        for (int i = 0; i < childNodeCount; i++) {
            TreeNode childNode = childNodes.get(i);
            boolean childIsLast = (i == childNodeCount - 1);
            String lining = (disposition == NodeDisposition.LAST) ? liningSpace : liningGeneral;
            String subPrefix = (disposition == NodeDisposition.ROOT) ? prefix : prefix + lining;
            int subInset = Math.max(0, inset - 1);
            NodeDisposition subDisposition = childIsLast ? NodeDisposition.LAST : NodeDisposition.GENERAL;
            printSub(childNode, out, subPrefix, subDisposition, subInset);
        }
    }

    private void printContentLine(
            Appendable out,
            String prefix,
            NodeDisposition disposition,
            boolean hasChildren,
            int inset,
            int connectOffset,
            int i,
            String line) {
        if (disposition == NodeDisposition.ROOT) {
            if (displayRoot) {
                Util.writeln(out, prefix + line);
            }
            return;
        }
        
        String itemPrefix = buildItemPrefix(disposition, hasChildren, inset, connectOffset, i);
        String fullPrefix = prefix + itemPrefix;
        String formattedFullPrefix = Util.getStringContent(ConsoleText.of(fullPrefix).format(liningFormat), ansiMode);
        Util.writeln(out, formattedFullPrefix + line);
    }
    
    private String buildItemPrefix(
            NodeDisposition disposition, boolean hasChildren, int inset, int connectOffset, int i) {
        StringBuilder resultBuilder = new StringBuilder();
        
        boolean isLast = disposition == NodeDisposition.LAST;
        if (i < connectOffset) {
            resultBuilder.append(liningGeneral);
        } else if (i == connectOffset) {
            resultBuilder.append(isLast ? liningLastNode : liningNode);
        } else {
            resultBuilder.append(isLast ? liningSpace : liningGeneral);
        }
        
        String insetString = buildInsets(hasChildren, inset, connectOffset, i);
        resultBuilder.append(insetString);

        return resultBuilder.toString();
    }
    
    private String buildInsets(boolean hasChildren, int inset, int connectOffset, int i) {
        if (inset == 0) {
            return "";
        }

        String insetString = (i == connectOffset) ? liningInset : liningSpace;
        String firstInsetString = insetString;
        if (align && connectAlignedChildren && hasChildren) {
            if (i == connectOffset) {
                firstInsetString = liningSub;
            } else if (i > connectOffset) {
                firstInsetString = liningGeneral;
            }
        }
        StringBuilder insetBuilder = new StringBuilder();
        insetBuilder.append(firstInsetString);
        for (int j = 1; j < inset; j++) {
            insetBuilder.append(insetString);
        }
        
        return insetBuilder.toString();
    }
    
    
    public static class Builder {

        private boolean displayRoot = true;
        
        private boolean displayPlaceholders = false;
        
        private boolean align = false;
        
        private boolean connectAlignedChildren = true;
        
        private String[] lines =
                UnicodeMode.isUnicodeDefault() ?
                DEFAULT_UNICODE_LINE_STRINGS.clone() :
                DEFAULT_ASCII_LINE_STRINGS.clone();
        
        private AnsiMode ansiMode = AnsiMode.AUTO;
        
        private AnsiFormat liningFormat = AnsiFormat.NONE;

                
        public Builder displayRoot(boolean displayRoot) {
            this.displayRoot = displayRoot;
            return this;
        }

        public Builder displayPlaceholders(boolean displayPlaceholders) {
            this.displayPlaceholders = displayPlaceholders;
            return this;
        }

        public Builder align(boolean align) {
            this.align = align;
            return this;
        }
        
        public Builder connectAlignedChildren(boolean connectAlignedChildren) {
            this.connectAlignedChildren = connectAlignedChildren;
            return this;
        }
        
        public Builder ascii() {
            this.lines = DEFAULT_ASCII_LINE_STRINGS.clone();
            return this;
        }
        
        public Builder unicode() {
            this.lines = DEFAULT_UNICODE_LINE_STRINGS.clone();
            return this;
        }

        public Builder lining(String space, String general, String node, String lastNode, String inset) {
            this.lines = new String[] {space, general, node, lastNode, inset};
            return this;
        }

        public Builder liningSpace(String liningSpace) {
            this.lines[0] = liningSpace;
            return this;
        }

        public Builder liningGeneral(String liningGeneral) {
            this.lines[1] = liningGeneral;
            return this;
        }

        public Builder liningNode(String liningNode) {
            this.lines[2] = liningNode;
            return this;
        }

        public Builder liningLastNode(String liningLastNode) {
            this.lines[3] = liningLastNode;
            return this;
        }

        public Builder liningInset(String liningInset) {
            this.lines[4] = liningInset;
            return this;
        }

        public Builder ansiMode(AnsiMode ansiMode) {
            this.ansiMode = ansiMode;
            return this;
        }

        public Builder liningFormat(AnsiFormat liningFormat) {
            this.liningFormat = liningFormat;
            return this;
        }

        public ListingTreePrinter build() {
            return new ListingTreePrinter(this);
        }
        
    }
    
}
