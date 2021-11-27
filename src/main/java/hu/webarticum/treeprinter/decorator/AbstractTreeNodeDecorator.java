package hu.webarticum.treeprinter.decorator;

import java.util.ArrayList;
import java.util.List;

import hu.webarticum.treeprinter.Insets;
import hu.webarticum.treeprinter.TreeNode;

/**
 * Base class for {@link TreeNode} decorators.
 * 
 * Decorators should only effect if the wrapped node is decorable.
 * Decorators should inherit by default (wrap their children with a similar decorator),
 * and should not inherit when the <code>inherit</code> option is set to <code>false</code>.
 */
public abstract class AbstractTreeNodeDecorator implements TreeNode {
    
    protected final TreeNode decoratedNode;
    
    protected final boolean inherit;

    protected final boolean decorable;
    

    public AbstractTreeNodeDecorator(TreeNode decoratedNode) {
        this(decoratedNode, true);
    }

    public AbstractTreeNodeDecorator(TreeNode decoratedNode, boolean inherit) {
        this(decoratedNode, inherit, decoratedNode.isDecorable());
    }

    public AbstractTreeNodeDecorator(TreeNode decoratedNode, boolean inherit, boolean decorable) {
        if (decoratedNode == null) {
            throw new IllegalArgumentException("Decorated node must not be null");
        }
        
        this.decoratedNode = decoratedNode;
        this.inherit = inherit;
        this.decorable = decorable;
    }
    

    @Override
    public String content() {
        if (decoratedNode.isDecorable()) {
            return decoratedContent();
        } else {
            return decoratedNode.content();
        }
    }
    
    public TreeNode getDecoratedNode() {
        return decoratedNode;
    }

    @Override
    public TreeNode originalNode() {
        return decoratedNode.originalNode();
    }
    
    @Override
    public Insets insets() {
        return decoratedNode.insets();
    }
    
    @Override
    public boolean isDecorable() {
        return decorable;
    }

    @Override
    public boolean isPlaceholder() {
        return decoratedNode.isPlaceholder();
    }

    @Override
    public List<TreeNode> children() {
        List<TreeNode> wrappedChildren = new ArrayList<>();
        List<TreeNode> originalChildren = decoratedNode.children();
        int childCount = originalChildren.size();
        for (int i = 0; i < childCount; i++) {
            TreeNode childNode = originalChildren.get(i);
            TreeNode wrappedChildNode = inherit ? wrapChild(childNode, i) : childNode;
            wrappedChildren.add(wrappedChildNode);
        }
        return wrappedChildren;
    }
    
    protected abstract String decoratedContent();

    protected abstract TreeNode wrapChild(TreeNode childNode, int index);

}
