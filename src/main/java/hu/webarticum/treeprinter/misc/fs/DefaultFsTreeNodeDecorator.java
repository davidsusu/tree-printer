package hu.webarticum.treeprinter.misc.fs;

import java.io.File;
import java.text.DecimalFormat;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.decorator.AbstractTreeNodeDecorator;

/**
 * Decorator for {@link FsTreeNode} that displays more information
 */
public class DefaultFsTreeNodeDecorator extends AbstractTreeNodeDecorator {

    public DefaultFsTreeNodeDecorator(TreeNode decoratedNode) {
        super(decoratedNode);
    }

    public DefaultFsTreeNodeDecorator(TreeNode decoratedNode, boolean inherit) {
        super(decoratedNode, inherit);
    }

    public DefaultFsTreeNodeDecorator(TreeNode decoratedNode, boolean inherit, boolean decorable) {
        super(decoratedNode, inherit, decorable);
    }
    
    
    @Override
    public String decoratedContent() {
        if (decoratedNode instanceof FsTreeNode) {
            FsTreeNode fsNode = (FsTreeNode) decoratedNode;
            File file = fsNode.getFile();
            if (file.isDirectory()) {
                return " " + file.getName() + "/";
            } else {
                return " " + file.getName() + " (" + formatFileSize(file.length()) + ")";
            }
        } else {
            return decoratedNode.content();
        }
    }
    
    @Override
    protected TreeNode wrapChild(TreeNode childNode, int index) {
        return new DefaultFsTreeNodeDecorator(childNode, decorable, inherit);
    }
    
    protected String formatFileSize(long fileSize) {
        String[] suffixes = new String[]{" KB", " MB", " GB", " TB"};
        double floatingSize = fileSize;
        String suffix = " b";
        for (String _suffix: suffixes) {
            if (floatingSize > 850) {
                floatingSize /= 1024;
                suffix = _suffix;
            }
        }
        return new DecimalFormat("#.##").format(floatingSize) + suffix;
    }

}
