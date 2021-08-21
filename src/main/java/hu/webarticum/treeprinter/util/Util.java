package hu.webarticum.treeprinter.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import hu.webarticum.treeprinter.TreeNode;

public final class Util {
    
    // FIXME: use Bee?
    private static final Pattern LINE_SEPARATOR_PATTERN = Pattern.compile("\\R");
    
    
    private Util() {
        // utility class
    }
    
    // TODO: create a general Dimension class
    public static int[] getContentDimension(String content) {
        String[] lines = splitToLines(content);
        int longestLineLength = getMaxLength(lines);
        return new int[] { longestLineLength, lines.length };
    }
    
    public static int getMaxLength(String[] lines) {
        int maxLength = 0;
        for (String line: lines) {
            int lineLength = line.length();
            if (lineLength > maxLength) {
                maxLength = lineLength;
            }
        }
        return maxLength;
    }

    public static String[] splitToLines(String content) {
        return LINE_SEPARATOR_PATTERN.split(content);
    }
    
    public static int getDepth(TreeNode treeNode) {
        List<TreeNode> levelNodes = new ArrayList<>();
        levelNodes.add(treeNode);
        int depth = 0;
        while (true) {
            List<TreeNode> newLevelNodes = new ArrayList<>();
            for (TreeNode levelNode: levelNodes) {
                for (TreeNode childNode: levelNode.getChildren()) {
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

    public static String repeat(char character, int repeats) {
        StringBuilder resultBuilder = new StringBuilder();
        repeat(resultBuilder, character, repeats);
        return resultBuilder.toString();
    }
    
    public static void repeat(StringBuilder stringBuilder, char character, int repeats) {
        for (int i = 0; i < repeats; i ++) {
            stringBuilder.append(character);
        }
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
