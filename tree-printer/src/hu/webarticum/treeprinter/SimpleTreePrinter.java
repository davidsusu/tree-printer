package hu.webarticum.treeprinter;

import java.util.List;

public class SimpleTreePrinter extends AbstractTreePrinter {

	private final int NODE_ROOT = 0;
	private final int NODE_GENERAL = 1;
	private final int NODE_LAST = 2;

	private String liningSpace = "   ";
	private String liningGeneral = " | ";
	private String liningNode = " |-";
	private String liningLastNode = " '-";

	private NodeDecorator nodeDecorator;
	
	public SimpleTreePrinter() {
		this(null);
	}

	public SimpleTreePrinter(NodeDecorator nodeDecorator) {
		this("   " , " | ", " |-", " '-", nodeDecorator);
	}

	public SimpleTreePrinter(
		String liningSpace, String liningGeneral, String liningNode, String liningLastNode
	) {
		this(liningSpace, liningGeneral, liningNode, liningLastNode, null);
	}
	
	public SimpleTreePrinter(
		String liningSpace, String liningGeneral, String liningNode, String liningLastNode,
		NodeDecorator nodeDecorator
	) {
		this.liningSpace = liningSpace;
		this.liningGeneral = liningGeneral;
		this.liningNode = liningNode;
		this.liningLastNode = liningLastNode;
		this.nodeDecorator = nodeDecorator;
	}
	
	@Override
	public void print(TreeNode rootNode, Appendable out) {
		printSub(rootNode, out, "", NODE_ROOT);
	}
	
	private void printSub(TreeNode node, Appendable out, String prefix, int type) {
		int connectOffset;
		String content;
		if (nodeDecorator != null) {
			content = nodeDecorator.getDecoratedContent(node);
			connectOffset = nodeDecorator.getOffset();
		} else {
			content = node.getContent();
			connectOffset = 0;
		}
		String[] lines = content.split("\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (type == NODE_ROOT) {
				writeln(out, prefix + line);
			} else {
				String itemPrefix;
				if (i < connectOffset) {
					itemPrefix = liningGeneral;
				} else if (i == connectOffset) {
					itemPrefix = (type == NODE_LAST) ? liningLastNode : liningNode;
				} else {
					itemPrefix = (type == NODE_LAST) ? liningSpace : liningGeneral;
				}
				writeln(out, prefix + itemPrefix + line);
			}
		}
		List<TreeNode> childNodes = node.getChildren();
		int childNodeCount = childNodes.size();
		for (int i = 0; i < childNodeCount; i++) {
			TreeNode childNode = childNodes.get(i);
			boolean childIsLast = (i == childNodeCount - 1);
			String subPrefix = (type == NODE_ROOT) ? prefix : prefix + ((type == NODE_LAST) ? liningSpace : liningGeneral);
			printSub(childNode, out, subPrefix, childIsLast ? NODE_LAST : NODE_GENERAL);
		}
	}
	
}
