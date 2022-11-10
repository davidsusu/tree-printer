package hu.webarticum.treeprinter;

import java.util.ArrayList;
import java.util.List;

import hu.webarticum.treeprinter.text.ConsoleText;

/**
 * Very simple {@link TreeNode} implementation which is placeholder and non-decorable
 */
public class PlaceholderNode implements TreeNode {

    @Override
    public ConsoleText content() {
        return ConsoleText.empty();
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
