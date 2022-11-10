package hu.webarticum.treeprinter.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hu.webarticum.treeprinter.AnsiMode;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.text.AnsiLineMerger;
import hu.webarticum.treeprinter.text.ConsoleText;
import hu.webarticum.treeprinter.text.LineBuffer;
import hu.webarticum.treeprinter.text.LineMerger;
import hu.webarticum.treeprinter.text.PlainLineMerger;

public final class Util {
    
    private Util() {
        // utility class
    }


    public static String getStringContent(ConsoleText content) {
        return AnsiMode.isAnsiEnabled() ? content.ansi() : content.plain();
    }

    public static ConsoleText toConsoleText(String stringContent) {
        return AnsiMode.isAnsiEnabled() ? ConsoleText.ofAnsi(stringContent) : ConsoleText.of(stringContent);
    }

    public static LineBuffer createLineBuffer(Appendable out) {
        LineMerger lineMerger = AnsiMode.isAnsiEnabled() ? new AnsiLineMerger() : new PlainLineMerger();
        return new LineBuffer(out, lineMerger);
    }
    
    public static int getDepth(TreeNode treeNode) {
        List<TreeNode> levelNodes = new ArrayList<>();
        levelNodes.add(treeNode);
        int depth = 0;
        while (true) {
            List<TreeNode> newLevelNodes = new ArrayList<>();
            for (TreeNode levelNode: levelNodes) {
                for (TreeNode childNode: levelNode.children()) {
                    if (childNode != null) {
                        newLevelNodes.add(childNode);
                    }
                }
            }
            if (newLevelNodes.isEmpty()) {
                break;
            }
            levelNodes = newLevelNodes;
            depth++;
        }
        return depth;
    }

    public static void write(Appendable out, String content) {
        try {
            out.append(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeln(Appendable out, String content) {
        write(out, content + "\n");
    }
    
}
