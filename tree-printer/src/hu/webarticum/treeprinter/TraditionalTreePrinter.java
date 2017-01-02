package hu.webarticum.treeprinter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraditionalTreePrinter extends AbstractTreePrinter {
    
    private final int margin;
    
    public TraditionalTreePrinter() {
        this(1);
    }
    
    public TraditionalTreePrinter(int margin) {
        this.margin = margin;
    }
    
    @Override
    public void print(TreeNode rootNode, Appendable out) {
        Map<TreeNode, Integer> widthMap = new HashMap<TreeNode, Integer>();
        int rootWidth = collectWidths(widthMap, rootNode);
        
        Map<TreeNode, PositionItem> positionMap = new HashMap<TreeNode, PositionItem>();
        
        String rootContent = rootNode.getContent();
        int[] rootContentDimension = Util.getContentDimension(rootContent);
        int[] rootAlign = alignNode(0, rootWidth, rootContentDimension[0]);
        positionMap.put(rootNode, new PositionItem(0, 0, rootAlign[1], rootAlign[0], rootContentDimension[1]));
        
        LineBuffer buffer = new LineBuffer(out);
        
        buffer.write(0, rootAlign[0], rootContent);
        
        buffer.flush();
        
        while (true) {
            Map<TreeNode, PositionItem> newPositionMap = new HashMap<TreeNode, PositionItem>();
            List<Integer> childBottoms = new ArrayList<Integer>();
            for (Map.Entry<TreeNode, PositionItem> entry: positionMap.entrySet()) {
                TreeNode node = entry.getKey();
                PositionItem positionItem = entry.getValue();
                Map<TreeNode, PositionItem> childrenPositionMap = new HashMap<TreeNode, PositionItem>();
                int childCol = positionItem.col;
                List<TreeNode> children = node.getChildren();
                children.removeAll(Collections.singleton(null));
                
                if (!children.isEmpty()) {
                    int childCount = children.size();
                    int[] childConnections = new int[childCount];
                    for (int i = 0; i < childCount; i++) {
                        TreeNode childNode = children.get(i);
                        int childWidth = widthMap.get(childNode);
                        String childContent = childNode.getContent();
                        int[] childContentDimension = Util.getContentDimension(childContent);
                        int[] childAlign = alignNode(childCol, childWidth, childContentDimension[0]);
                        PositionItem childPositionItem = new PositionItem(
                            positionItem.row + positionItem.height, childCol,
                            childAlign[1], childAlign[0], childContentDimension[1]
                        );
                        childrenPositionMap.put(childNode, childPositionItem);
                        childCol += childWidth + margin;
                        childConnections[i] = childAlign[1];
                    }
                    
                    int connectionRows = printConnections(
                        buffer, positionItem.row + positionItem.height, positionItem.connection, childConnections
                    );
                    
                    for (Map.Entry<TreeNode, PositionItem> childEntry: childrenPositionMap.entrySet()) {
                        TreeNode childNode = childEntry.getKey();
                        PositionItem childPositionItem = childEntry.getValue();
                        childPositionItem.row += connectionRows;
                        buffer.write(childPositionItem.row, childPositionItem.left, childNode.getContent());
                        childBottoms.add(childPositionItem.row + childPositionItem.height);
                    }
                    
                    newPositionMap.putAll(childrenPositionMap);
                }
            }

            if (newPositionMap.isEmpty()) {
                break;
            } else {
                int minimumChildBottom = Integer.MAX_VALUE;
                for (int bottomValue: childBottoms) {
                    if (bottomValue < minimumChildBottom) {
                        minimumChildBottom = bottomValue;
                    }
                }
                buffer.flush(minimumChildBottom);
                
                positionMap = newPositionMap;
            }
        }
        
        buffer.flush();
    }
    
    // TODO: aligning strategies
    private int[] alignNode(int position, int width, int contentWidth) {
        //return new int[] {position, position};
        
        int left = position + (width - contentWidth) / 2;
        return new int[] {left, left + (contentWidth / 2)};
    }
    
    // FIXME: references vs ad hoc decorators
    private int collectWidths(Map<TreeNode, Integer> widthMap, TreeNode node) {
        int contentWidth = Util.getContentDimension(node.getContent())[0];
        int childrenWidth = 0;
        boolean first = true;
        List<TreeNode> children = node.getChildren();
        children.removeAll(Collections.singleton(null));
        for (TreeNode childNode: children) {
            if (first) {
                first = false;
            } else {
                childrenWidth += margin;
            }
            childrenWidth += collectWidths(widthMap, childNode);
        }
        int nodeWidth = Math.max(contentWidth, childrenWidth);
        widthMap.put(node, nodeWidth);
        return nodeWidth;
    }
    
    // TODO: lining strategies
    private int printConnections(LineBuffer buffer, int row, int topConnection, int[] bottomConnections) {
        int start = Math.min(topConnection, bottomConnections[0]);
        int end = Math.max(topConnection, bottomConnections[bottomConnections.length - 1]);
        
        StringBuilder aboveLineBuilder = new StringBuilder();
        for (int i = start; i <= end; i++) {
            char character;
            if (i == topConnection) {
                character = '|';
            } else if (i == start || i == end) {
                character = ' ';
            } else {
                character = '_';
            }
            aboveLineBuilder.append(character);
        }
        buffer.write(row, start, aboveLineBuilder.toString());

        StringBuilder belowLineBuilder = new StringBuilder();
        int position = start;
        for (int bottomConnection: bottomConnections) {
            for (int i = position; i < bottomConnection; i++) {
                belowLineBuilder.append(' ');
            }
            belowLineBuilder.append('|');
            position = bottomConnection + 1;
        }
        buffer.write(row + 1, start, belowLineBuilder.toString());
        
        return 2;
    }
    
    private class PositionItem {
        
        int row = 0;
        
        int col = 0;
        
        int connection = 0;
        
        int left = 0;
        
        int height = 0;

        PositionItem(int row, int col, int connection, int left, int height) {
            this.row = row;
            this.col = col;
            this.connection = connection;
            this.left = left;
            this.height = height;
        }
        
    }
    
}





