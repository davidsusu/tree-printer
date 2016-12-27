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
	
	@Override
	public String getContent() {
		
		// TODO
		
		return null;
	}

	@Override
	public int getOffsetX() {
		return decoratedNode.getOffsetX();
	}

	@Override
	public int getOffsetY() {
		return decoratedNode.getOffsetY();
	}

	@Override
	public boolean isDecorable() {
		return decorable;
	}

	@Override
	public List<TreeNode> getChildren() {
		List<TreeNode> decoratedChildren = new ArrayList<TreeNode>();
		for (TreeNode childNode: decoratedNode.getChildren()) {
			if (inherit && (forceInherit || childNode.isDecorable())) {
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
