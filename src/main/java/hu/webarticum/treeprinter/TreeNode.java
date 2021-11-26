package hu.webarticum.treeprinter;

import java.util.List;

import hu.webarticum.treeprinter.decorator.AbstractTreeNodeDecorator;
import hu.webarticum.treeprinter.printer.TreePrinter;

/**
 * Main interface for trees and tree nodes
 * 
 * Any tree is given by its root node.
 * Nodes provide a content as a string (via {@link #content()}),
 * and list their children (via {@link #children()}).
 * The other methods provide metadata, they have default implementations.
 */
public interface TreeNode {
    
    /**
     * Gets the content of this node.
     */
    public String content();

    /**
     * Gets the list of child nodes of this node.
     */
    public List<TreeNode> children();

    /**
     * Gets the original node object.
     * 
     * By default, returns with <code>this</code>.
     * Decorators should return with the most inner decorated node.
     */
    public default TreeNode originalNode() {
        return this;
    }

    /**
     * Gets the content insets.
     * 
     * Returns with the sizes of the visual decoration around the main content.
     * By default, this is fully zero.
     * Visually extending decorators (border, shadow etc.) should extend
     * the insets with the sizes of the visual extension.
     */
    public default Insets insets() {
        return Insets.EMPTY;
    }

    /**
     * Checks if this node can be decorated.
     * 
     * By default, decorators should keep non-decorable nodes untouched.
     * Most decorator support forcing decoration (see {@link AbstractTreeNodeDecorator}).
     */
    public default boolean isDecorable() {
        return true;
    }

    /**
     * Checks if this node is a placeholder.
     * 
     * By default, generic {@link TreePrinter} implementations hide placeholder nodes.
     * Some types of implementation (e. g. n-ary trees) should take these into account anyway.
     */
    public default boolean isPlaceholder() {
        return false;
    }

}
