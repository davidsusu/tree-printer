package hu.webarticum.treeprinter.printer.listing;

import java.util.ArrayList;
import java.util.List;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.printer.TreePrinter;
import hu.webarticum.treeprinter.printer.UnicodeMode;
import hu.webarticum.treeprinter.util.Util;

public class ListingTreePrinter implements TreePrinter {

    private enum NodeDisposition { ROOT, GENERAL, LAST }
    
    private static final String[] DEFAULT_ASCII_LINE_STRINGS = new String[] {
        "   ", " | ", " |-", " '-", "---"
    };

    private static final String[] DEFAULT_UNICODE_LINE_STRINGS = new String[] {
        "   ", " │ ", " ├─", " └─", "───"
    };

    private final String liningSpace;
    private final String liningGeneral;
    private final String liningNode;
    private final String liningLastNode;
    private final String liningInset;
    private final boolean displayRoot;
    private final boolean displayPlaceholders;
    private final boolean align;

    public ListingTreePrinter() {
        this(createBuilder());
    }

    private ListingTreePrinter(Builder builder) {
        this.liningSpace = builder.lines[0];
        this.liningGeneral = builder.lines[1];
        this.liningNode = builder.lines[2];
        this.liningLastNode = builder.lines[3];
        this.liningInset = builder.lines[4];
        this.displayRoot = builder.displayRoot;
        this.displayPlaceholders = builder.displayPlaceholders;
        this.align = builder.align;
    }

    @Override
    public void print(TreeNode rootNode, Appendable out) {
        printSub(rootNode, out, "", NodeDisposition.ROOT, align ? Util.getDepth(rootNode) : 0);
    }
    
    private void printSub(TreeNode node, Appendable out, String prefix, NodeDisposition disposition, int inset) {
        String content = node.getContent();
        int connectOffset = node.getInsets()[0];
        
        String[] lines = Util.splitToLines(content);
        for (int i = 0; i < lines.length; i++) {
            printContentLine(out, prefix, disposition, inset, connectOffset, i, lines[i]);
        }
        
        List<TreeNode> childNodes = new ArrayList<>(node.getChildren());
        if (!displayPlaceholders) {
            childNodes.removeIf(TreeNode::isPlaceholder);
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
    
    private void printContentLine(Appendable out, String prefix, NodeDisposition disposition, int inset, int connectOffset, int i, String line) {
        if (disposition == NodeDisposition.ROOT) {
            if (displayRoot) {
                Util.writeln(out, prefix + line);
            }
            return;
        }
        
        String itemPrefix = buildItemPrefix(disposition, inset, connectOffset, i);
        Util.writeln(out, prefix + itemPrefix + line);
    }
    
    private String buildItemPrefix(NodeDisposition disposition, int inset, int connectOffset, int i) {
        StringBuilder resultBuilder = new StringBuilder();
        
        boolean isLast = disposition == NodeDisposition.LAST;
        if (i < connectOffset) {
            resultBuilder.append(liningGeneral);
        } else if (i == connectOffset) {
            resultBuilder.append(isLast ? liningLastNode : liningNode);
        } else {
            resultBuilder.append(isLast ? liningSpace : liningGeneral);
        }
        
        if (inset > 0) {
            String insetString = (i == connectOffset) ? liningInset : liningSpace;
            StringBuilder insetBuilder = new StringBuilder();
            for (int j = 0; j < inset; j++) {
                insetBuilder.append(insetString);
            }
            resultBuilder.append(insetBuilder.toString());
        }
        
        return resultBuilder.toString();
    }
    
    public static Builder createBuilder() {
        return new Builder();
    }
    
    public static class Builder {

        private boolean displayRoot = true;
        private boolean displayPlaceholders = false;
        private boolean align = false;
        
        private String[] lines = (
            UnicodeMode.isUnicodeDefault() ?
            DEFAULT_UNICODE_LINE_STRINGS :
            DEFAULT_ASCII_LINE_STRINGS
        ).clone();

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

        public ListingTreePrinter build() {
            return new ListingTreePrinter(this);
        }
        
    }
    
}
