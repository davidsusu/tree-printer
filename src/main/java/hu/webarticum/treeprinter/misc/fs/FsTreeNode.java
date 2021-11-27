package hu.webarticum.treeprinter.misc.fs;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hu.webarticum.treeprinter.TreeNode;

/**
 * {@link TreeNode} implementation that represents a file system tree.
 * 
 * It is recommended to use with the {@link DefaultFsTreeNodeDecorator}.
 */
public class FsTreeNode implements TreeNode {

    public static final FileFilter DEFAULT_FILE_FILTER = fileItem -> !fileItem.isHidden();
    
    public static final Comparator<File> DEFAULT_COMPARATOR = FsTreeNode::compareFiles;
    
    
    private final File file;
    
    private final FileFilter filter;
    
    private final Comparator<File> comparator;
    
    private final boolean decorable;
    
    
    public FsTreeNode() {
        this(new File("."));
    }

    public FsTreeNode(File file) {
        this(file, DEFAULT_FILE_FILTER);
    }

    public FsTreeNode(File file, FileFilter filter) {
        this(file, filter, DEFAULT_COMPARATOR);
    }

    public FsTreeNode(File file, FileFilter filter, Comparator<File> comparator) {
        this(file, filter, comparator, true);
    }
    
    public FsTreeNode(File file, FileFilter filter, Comparator<File> comparator, boolean decorable) {
        this.file = file;
        this.filter = filter;
        this.comparator = comparator;
        this.decorable = decorable;
    }
    
    
    private static int compareFiles(File file1, File file2) {
        if (file1.isDirectory()) {
            if (!file2.isDirectory()) {
                return -1;
            }
        } else if (file2.isDirectory()) {
            return 1;
        }
        
        return file1.getName().compareToIgnoreCase(file2.getName());
    }
    
    public File getFile() {
        return file;
    }
    
    @Override
    public String content() {
        return file.getName();
    }

    @Override
    public List<TreeNode> children() {
        List<TreeNode> childNodes = new ArrayList<>();
        File[] subFileArray = file.listFiles(filter);
        if (subFileArray != null && subFileArray.length > 0) {
            List<File> subFiles = new ArrayList<>(Arrays.asList(subFileArray));
            Collections.sort(subFiles, comparator);
            for (File subFile: subFiles) {
                childNodes.add(new FsTreeNode(subFile, filter, comparator, decorable));
            }
        }
        return childNodes;
    }

    @Override
    public boolean isDecorable() {
        return decorable;
    }

}
