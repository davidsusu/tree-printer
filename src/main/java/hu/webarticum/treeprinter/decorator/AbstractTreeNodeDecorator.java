package hu.webarticum.treeprinter.decorator;

import java.util.ArrayList;
import java.util.List;

import hu.webarticum.treeprinter.AbstractTreeNode;
import hu.webarticum.treeprinter.TreeNode;

public abstract class AbstractTreeNodeDecorator extends AbstractTreeNode {
    
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
        if (decoratedNode == null) {
            throw new IllegalArgumentException("Decorated node must not be null");
        }
        
        this.decoratedNode = decoratedNode;
        this.decorable = decorable;
        this.inherit = inherit;
        this.forceInherit = forceInherit;
    }
    
    public TreeNode getDecoratedNode() {
        return decoratedNode;
    }

    @Override
    public TreeNode getOriginalNode() {
        return decoratedNode.getOriginalNode();
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
            if (inherit && (forceInherit || childNode.isDecorable())) {
                decoratedChildren.add(decorateChild(childNode));
            } else {
                decoratedChildren.add(childNode);
            }
        }
        return decoratedChildren;
    }

    protected abstract TreeNode decorateChild(TreeNode childNode);

}
