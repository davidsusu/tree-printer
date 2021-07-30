package hu.webarticum.treeprinter.printer.traditional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.decorator.TrackingTreeNodeDecorator;
import hu.webarticum.treeprinter.printer.AbstractTreePrinter;
import hu.webarticum.treeprinter.util.LineBuffer;
import hu.webarticum.treeprinter.util.Util;

public class TraditionalTreePrinter extends AbstractTreePrinter {

    public static final Aligner DEFAULT_ALIGNER = new DefaultAligner();

    public static final Liner DEFAULT_LINER = new DefaultLiner();

    private final Aligner aligner;

    private final Liner liner;
    
    private final boolean displayPlaceholders;
    
    public TraditionalTreePrinter() {
        this(DEFAULT_ALIGNER, DEFAULT_LINER);
    }

    public TraditionalTreePrinter(Aligner aligner, Liner liner) {
        this(aligner, liner, false);
    }

    public TraditionalTreePrinter(boolean displayPlaceholders) {
        this(DEFAULT_ALIGNER, DEFAULT_LINER, displayPlaceholders);
    }
    
    public TraditionalTreePrinter(Aligner aligner, Liner liner, boolean displayPlaceholders) {
        this.aligner = aligner;
        this.liner = liner;
        this.displayPlaceholders = displayPlaceholders;
    }
    
    // TODO: calculate Position taking into account the insets
    @Override
    public void print(TreeNode rootNode, Appendable out) {
        TreeNode wrappedRootNode = new TrackingTreeNodeDecorator(rootNode);
        
        Map<TreeNode, Integer> widthMap = new HashMap<>();
        int rootWidth = aligner.collectWidths(widthMap, wrappedRootNode);
        
        Map<TreeNode, Position> positionMap = new HashMap<>();
        
        String rootContent = wrappedRootNode.getContent();
        int[] rootContentDimension = Util.getContentDimension(rootContent);
        Align rootAlign = aligner.alignNode(wrappedRootNode, 0, rootWidth, rootContentDimension[0]);
        positionMap.put(wrappedRootNode, new Position(0, 0, rootAlign.bottomConnection, rootAlign.left, rootContentDimension[1]));
        
        LineBuffer buffer = new LineBuffer(out);
        
        buffer.write(0, rootAlign.left, rootContent);
        
        buffer.flush();
        
        while (true) {
            Map<TreeNode, Position> newPositionMap = new HashMap<>();
            List<Integer> childBottoms = new ArrayList<>();
            for (Map.Entry<TreeNode, Position> entry: positionMap.entrySet()) {
                TreeNode node = entry.getKey();
                Position position = entry.getValue();
                Map<TreeNode, Position> childrenPositionMap = new HashMap<>();
                List<TreeNode> children = new ArrayList<>(node.getChildren());
                if (!displayPlaceholders) {
                    children.removeIf(TreeNode::isPlaceholder);
                }
                
                int[] childrenAlign = aligner.alignChildren(node, children, position.col, widthMap);
                
                if (!children.isEmpty()) {
                    int childCount = children.size();
                    List<Integer> childConnections = new ArrayList<>(childCount);
                    for (int i = 0; i < childCount; i++) {
                        int childCol = childrenAlign[i];
                        TreeNode childNode = children.get(i);
                        int childWidth = widthMap.get(childNode);
                        String childContent = childNode.getContent();
                        int[] childContentDimension = Util.getContentDimension(childContent);
                        Align childAlign = aligner.alignNode(childNode, childCol, childWidth, childContentDimension[0]);
                        Position childPositioning = new Position(
                            position.row + position.height, childCol,
                            childAlign.bottomConnection, childAlign.left, childContentDimension[1]
                        );
                        childrenPositionMap.put(childNode, childPositioning);
                        childConnections.add(childAlign.topConnection);
                    }
                    
                    int connectionRows = liner.printConnections(
                        buffer, position.row + position.height, position.connection, childConnections
                    );
                    
                    for (Map.Entry<TreeNode, Position> childEntry: childrenPositionMap.entrySet()) {
                        TreeNode childNode = childEntry.getKey();
                        Position childPositionItem = childEntry.getValue();
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
    
    private class Position {
        
        int row;
        
        int col;
        
        int connection;
        
        int left;
        
        int height;

        Position(int row, int col, int connection, int left, int height) {
            this.row = row;
            this.col = col;
            this.connection = connection;
            this.left = left;
            this.height = height;
        }
        
    }
    
}





