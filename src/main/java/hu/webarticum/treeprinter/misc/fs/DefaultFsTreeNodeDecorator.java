package hu.webarticum.treeprinter.misc.fs;

import java.io.File;
import java.text.DecimalFormat;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.decorator.AbstractTreeNodeDecorator;
import hu.webarticum.treeprinter.text.ConsoleText;

/**
 * Decorator for {@link FsTreeNode} that displays more information
 */
public class DefaultFsTreeNodeDecorator extends AbstractTreeNodeDecorator {

    public DefaultFsTreeNodeDecorator(TreeNode baseNode) {
        super(baseNode);
    }

    public DefaultFsTreeNodeDecorator(TreeNode baseNode, boolean inherit) {
        super(baseNode, inherit);
    }

    public DefaultFsTreeNodeDecorator(TreeNode baseNode, boolean inherit, boolean decorable) {
        super(baseNode, inherit, decorable);
    }
    
    
    @Override
    public ConsoleText decoratedContent() {
        if (baseNode instanceof FsTreeNode) {
            FsTreeNode fsNode = (FsTreeNode) baseNode;
            File file = fsNode.getFile();
            if (file.isDirectory()) {
                return ConsoleText.of(" " + file.getName() + "/");
            } else {
                return ConsoleText.of(" " + file.getName() + " (" + formatFileSize(file.length()) + ")");
            }
        } else {
            return baseNode.content();
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
