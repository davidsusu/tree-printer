package hu.webarticum.treeprinter;

import java.util.Collections;
import java.util.List;

public class SimpleTreePrinter extends AbstractTreePrinter {

    private final int NODE_ROOT = 0;
    private final int NODE_GENERAL = 1;
    private final int NODE_LAST = 2;

    private final String liningSpace;
    private final String liningGeneral;
    private final String liningNode;
    private final String liningLastNode;
    private final String liningInset;
    private final boolean displayRoot;
    private final boolean align;

    public SimpleTreePrinter() {
        this(true, false);
    }

    public SimpleTreePrinter(boolean displayRoot, boolean align) {
        this("   " , " | ", " |-", " '-", "---", displayRoot, align);
    }
    
    public SimpleTreePrinter(
        String liningSpace, String liningGeneral, String liningNode, String liningLastNode, String liningInset,
        boolean displayRoot, boolean align
    ) {
        this.liningSpace = liningSpace;
        this.liningGeneral = liningGeneral;
        this.liningNode = liningNode;
        this.liningLastNode = liningLastNode;
        this.liningInset = liningInset;
        this.displayRoot = displayRoot;
        this.align = align;
    }

    @Override
    public void print(TreeNode rootNode, Appendable out) {
        printSub(rootNode, out, "", NODE_ROOT, align ? Util.getDepth(rootNode) : 0);
    }
    
    private void printSub(TreeNode node, Appendable out, String prefix, int type, int inset) {
        String content = node.getContent();
        int connectOffset = node.getInsets()[0];
        
        String[] lines = content.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (type == NODE_ROOT) {
                if (displayRoot) {
                    writeln(out, prefix + line);
                }
            } else {
                String itemPrefix;
                if (i < connectOffset) {
                    itemPrefix = liningGeneral;
                } else if (i == connectOffset) {
                    itemPrefix = (type == NODE_LAST) ? liningLastNode : liningNode;
                } else {
                    itemPrefix = (type == NODE_LAST) ? liningSpace : liningGeneral;
                }
                if (inset > 0) {
                    String insetString = (i == connectOffset) ? liningInset : liningSpace;
                    StringBuilder insetBuilder = new StringBuilder();
                    for (int j = 0; j < inset; j++) {
                        insetBuilder.append(insetString);
                    }
                    itemPrefix += insetBuilder.toString();
                }
                writeln(out, prefix + itemPrefix + line);
            }
        }
        
        List<TreeNode> childNodes = node.getChildren();
        childNodes.removeAll(Collections.singleton(null));
        int childNodeCount = childNodes.size();
        for (int i = 0; i < childNodeCount; i++) {
            TreeNode childNode = childNodes.get(i);
            boolean childIsLast = (i == childNodeCount - 1);
            String subPrefix = (type == NODE_ROOT) ? prefix : prefix + ((type == NODE_LAST) ? liningSpace : liningGeneral);
            int subInset = Math.max(0, inset - 1);
            printSub(childNode, out, subPrefix, childIsLast ? NODE_LAST : NODE_GENERAL, subInset);
        }
    }
    
}
