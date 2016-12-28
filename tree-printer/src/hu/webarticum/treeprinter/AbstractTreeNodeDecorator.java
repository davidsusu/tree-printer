package hu.webarticum.treeprinter;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractTreeNodeDecorator implements TreeNode {
	
	protected final TreeNode decoratedNode;

	protected final boolean decorable;
	
	protected final boolean inherit;
	
	protected final boolean forceInherit;

	public AbstractTreeNodeDecorator(TreeNode decoratedNode) {
		this(decoratedNode, decoratedNode.isDecorable(), true, false);
	}

	public AbstractTreeNodeDecorator(TreeNode decoratedNode, boolean decorable) {
		this(decoratedNode, decorable, true, false);
	}

	public AbstractTreeNodeDecorator(TreeNode decoratedNode, boolean decorable, boolean inherit) {
		this(decoratedNode, decorable, inherit, false);
	}

	public AbstractTreeNodeDecorator(TreeNode decoratedNode, boolean decorable, boolean inherit, boolean forceInherit) {
		this.decoratedNode = decoratedNode;
		this.decorable = decorable;
		this.inherit = inherit;
		this.forceInherit = forceInherit;
	}
	
	public TreeNode getDecoratedNode() {
		return decoratedNode;
	}

	public TreeNode getInmostNode() {
		if (decoratedNode instanceof AbstractTreeNodeDecorator) {
			return ((AbstractTreeNodeDecorator)decoratedNode).getInmostNode();
		} else {
			return decoratedNode;
		}
	}
	
	@Override
	public int[] getInsets() {
		return decoratedNode.getInsets();
	}
	
	@Override
	public boolean isDecorable() {
		return decorable;
	}

	@Override
	public List<TreeNode> getChildren() {
		List<TreeNode> decoratedChildren = new ArrayList<TreeNode>();
		for (TreeNode childNode: decoratedNode.getChildren()) {
			if (childNode == null) {
				decoratedChildren.add(null);
			} else if (inherit && (forceInherit || childNode.isDecorable())) {
				decoratedChildren.add(decorateChild(childNode));
			} else {
				decoratedChildren.add(childNode);
			}
		}
		return decoratedChildren;
	}
	
	protected void repeat(StringBuilder stringBuilder, char character, int repeats) {
		for (int i = 0; i < repeats; i ++) {
			stringBuilder.append(character);
		}
	}

	abstract protected TreeNode decorateChild(TreeNode childNode);

}
