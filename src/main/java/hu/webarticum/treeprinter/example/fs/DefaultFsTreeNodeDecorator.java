package hu.webarticum.treeprinter.example.fs;

import java.io.File;
import java.text.DecimalFormat;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.decorator.AbstractTreeNodeDecorator;

public class DefaultFsTreeNodeDecorator extends AbstractTreeNodeDecorator {

    public DefaultFsTreeNodeDecorator(TreeNode decoratedNode) {
        super(decoratedNode);
    }

    public DefaultFsTreeNodeDecorator(TreeNode decoratedNode, boolean decorable) {
        super(decoratedNode, decorable);
    }

    public DefaultFsTreeNodeDecorator(TreeNode decoratedNode, boolean decorable, boolean inherit) {
        super(decoratedNode, decorable, inherit);
    }
    
    public DefaultFsTreeNodeDecorator(TreeNode decoratedNode, boolean decorable, boolean inherit, boolean forceInherit) {
        super(decoratedNode, decorable, inherit, forceInherit);
    }
    
    @Override
    public String content() {
        if (decoratedNode instanceof FsTreeNode) {
            FsTreeNode fsNode = (FsTreeNode)decoratedNode;
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
    protected TreeNode decorateChild(TreeNode childNode, int index) {
        return new DefaultFsTreeNodeDecorator(childNode, decorable, inherit, forceInherit);
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
