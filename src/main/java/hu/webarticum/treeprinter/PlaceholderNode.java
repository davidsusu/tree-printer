package hu.webarticum.treeprinter;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderNode extends AbstractTreeNode {

    @Override
    public String content() {
        return "";
    }

    @Override
    public List<TreeNode> children() {
        return new ArrayList<>(0);
    }

    @Override
    public boolean isDecorable() {
        return false;
    }

    @Override
    public boolean isPlaceholder() {
        return true;
    }

}
