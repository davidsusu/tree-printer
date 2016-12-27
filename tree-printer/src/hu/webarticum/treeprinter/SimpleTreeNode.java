package hu.webarticum.treeprinter;

import java.util.ArrayList;
import java.util.List;

public class SimpleTreeNode implements TreeNode {

	protected final String content;
	
	protected final int offsetX;
	
	protected final int offsetY;
	
	protected List<TreeNode> children = new ArrayList<TreeNode>();

	public SimpleTreeNode(String content) {
		this(content, 0, 0);
	}

	public SimpleTreeNode(String content, int offsetX, int offsetY) {
		this.content = content;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	void addChild(TreeNode childNode) {
		children.add(childNode);
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public int getOffsetX() {
		return offsetX;
	}

	@Override
	public int getOffsetY() {
		return offsetY;
	}

	@Override
	public List<TreeNode> getChildren() {
		return new ArrayList<TreeNode>(children);
	}

	@Override
	public boolean isDecorable() {
		return true;
	}
	
	@Override
	public String toString() {
		return getContent();
	}

}
