package hu.webarticum.treeprinter;

abstract public class AbstractTreeNode implements TreeNode {

	@Override
	public TreeNode getOriginalNode() {
		return this;
	}

	@Override
	public int[] getInsets() {
		return new int[] {0, 0, 0, 0};
	}

	@Override
	public boolean isDecorable() {
		return true;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof TreeNode)) {
			return false;
		}
		return (this.getOriginalNode() == ((TreeNode)other).getOriginalNode());
	}
	
	@Override
	public int hashCode() {
		return System.identityHashCode(getOriginalNode());
	}

	@Override
	public String toString() {
		return getContent();
	}

}
