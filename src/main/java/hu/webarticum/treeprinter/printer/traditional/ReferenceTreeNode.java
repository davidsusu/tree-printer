package hu.webarticum.treeprinter.printer.traditional;

import java.util.ArrayList;
import java.util.List;

import hu.webarticum.treeprinter.TreeNode;

public class ReferenceTreeNode implements TreeNode {
	
	public final ReferenceTreeNode parent;
	
	public final int index;
	
	public final TreeNode baseNode;
	

	public ReferenceTreeNode(TreeNode baseNode) {
		this(null, 0, baseNode);
	}
	
	public ReferenceTreeNode(ReferenceTreeNode parent, int index, TreeNode baseNode) {
		this.parent = parent;
		this.index = index;
		this.baseNode = baseNode;
	}


	@Override
	public String getContent() {
		return baseNode.getContent();
	}


	@Override
	public TreeNode getOriginalNode() {
		return baseNode.getOriginalNode();
	}


	@Override
	public int[] getInsets() {
		return baseNode.getInsets();
	}


	@Override
	public List<TreeNode> getChildren() {
		List<TreeNode> children = baseNode.getChildren();
		int childCount = children.size();
		List<TreeNode> referencedChildren = new ArrayList<TreeNode>(childCount);
		for (int i = 0; i < childCount; i++) {
			TreeNode childNode = children.get(i);
			referencedChildren.add(
				childNode != null ? new ReferenceTreeNode(this, i, childNode) : null
			);
		}
		return referencedChildren;
	}


	@Override
	public boolean isDecorable() {
		return false;
	}
	
	@Override
	public int hashCode() {
		int parentHashCode = parent != null ? parent.hashCode(): 0;
		return (parentHashCode * 37) + index;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ReferenceTreeNode)) {
			return false;
		}

		ReferenceTreeNode otherReferenceTreeNode = (ReferenceTreeNode)other;
		ReferenceTreeNode otherParent = otherReferenceTreeNode.parent;
		
		if (parent == null) {
			if (otherParent != null) {
				return false;
			}
		} else if (otherParent == null || !parent.equals(otherParent)) {
			return false;
		}
		
		return index == otherReferenceTreeNode.index;
	}

}
