package hu.webarticum.treeprinter.printer;

import java.io.IOException;

import hu.webarticum.treeprinter.TreeNode;

public abstract class AbstractTreePrinter implements TreePrinter {

    @Override
    public void print(TreeNode rootNode) {
        print(rootNode, System.out);
    }
    
    @Override
    public String getAsString(TreeNode rootNode) {
        StringBuilder builder = new StringBuilder();
        print(rootNode, builder);
        return builder.toString();
    }
    
    protected void write(Appendable out, String content) {
        try {
            out.append(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeln(Appendable out, String content) {
        write(out, content + "\n");
    }
    
}
