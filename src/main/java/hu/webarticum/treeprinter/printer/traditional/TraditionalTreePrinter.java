package hu.webarticum.treeprinter.printer.traditional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.decorator.TrackingTreeNodeDecorator;
import hu.webarticum.treeprinter.printer.TreePrinter;
import hu.webarticum.treeprinter.util.LineBuffer;
import hu.webarticum.treeprinter.util.Util;

public class TraditionalTreePrinter implements TreePrinter {

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
    
    
    @Override
    public void print(TreeNode rootNode, Appendable out) {
        TreeNode wrappedRootNode = new TrackingTreeNodeDecorator(rootNode);
        
        Map<TreeNode, Integer> widthMap = new HashMap<>();
        int rootWidth = aligner.collectWidths(widthMap, wrappedRootNode);
        
        Map<TreeNode, Position> positionMap = new HashMap<>();
        
        String rootContent = wrappedRootNode.content();
        int[] rootContentDimension = Util.getContentDimension(rootContent);
        Align rootAlign = aligner.alignNode(wrappedRootNode, 0, rootWidth, rootContentDimension[0]);
        Position rootPosition = new Position(0, 0, rootAlign.bottomConnection, rootAlign.left, rootContentDimension[1]);
        positionMap.put(wrappedRootNode, rootPosition);
        
        LineBuffer buffer = new LineBuffer(out);
        
        buffer.write(0, rootAlign.left, rootContent);
        
        buffer.flush();
        
        while (!positionMap.isEmpty()) {
            positionMap = printNextGeneration(buffer, positionMap, widthMap);
        }
        
        buffer.flush();
    }
    
    private Map<TreeNode, Position> printNextGeneration(
            LineBuffer buffer, Map<TreeNode, Position> positionMap, Map<TreeNode, Integer> widthMap) {
        Map<TreeNode, Position> newPositionMap = new HashMap<>();
        List<Integer> childBottoms = new ArrayList<>();
        for (Map.Entry<TreeNode, Position> entry: positionMap.entrySet()) {
            TreeNode node = entry.getKey();
            Position position = entry.getValue();
            handleNodeChildren(buffer, node, position, newPositionMap, widthMap, childBottoms);
        }

        if (!newPositionMap.isEmpty()) {
            int minimumChildBottom = Integer.MAX_VALUE;
            for (int bottomValue: childBottoms) {
                if (bottomValue < minimumChildBottom) {
                    minimumChildBottom = bottomValue;
                }
            }
            buffer.flush(minimumChildBottom);
        }
        
        return newPositionMap;
    }
    
    private void handleNodeChildren(
            LineBuffer buffer,
            TreeNode node,
            Position position,
            Map<TreeNode, Position> newPositionMap,
            Map<TreeNode, Integer> widthMap,
            List<Integer> childBottoms) {
        Map<TreeNode, Position> childrenPositionMap = new HashMap<>();
        List<TreeNode> children = new ArrayList<>(node.children());
        if (!displayPlaceholders) {
            children.removeIf(TreeNode::isPlaceholder);
        }
        if (children.isEmpty()) {
            return;
        }
        
        int[] childrenAlign = aligner.alignChildren(node, children, position.col, widthMap);
        
        int childCount = children.size();
        List<Integer> childConnections = new ArrayList<>(childCount);
        for (int i = 0; i < childCount; i++) {
            int childCol = childrenAlign[i];
            TreeNode childNode = children.get(i);
            int childWidth = widthMap.get(childNode);
            String childContent = childNode.content();
            int[] childContentDimension = Util.getContentDimension(childContent);
            Align childAlign = aligner.alignNode(childNode, childCol, childWidth, childContentDimension[0]);
            Position childPositioning = new Position(
                    position.row + position.height,
                    childCol,
                    childAlign.bottomConnection,
                    childAlign.left,
                    childContentDimension[1]);
            childrenPositionMap.put(childNode, childPositioning);
            childConnections.add(childAlign.topConnection);
        }
        
        int connectionRows = liner.printConnections(
                buffer, position.row + position.height, position.connection, childConnections);
        
        for (Map.Entry<TreeNode, Position> childEntry: childrenPositionMap.entrySet()) {
            TreeNode childNode = childEntry.getKey();
            Position childPositionItem = childEntry.getValue();
            childPositionItem.row += connectionRows;
            buffer.write(childPositionItem.row, childPositionItem.left, childNode.content());
            childBottoms.add(childPositionItem.row + childPositionItem.height);
        }
        
        newPositionMap.putAll(childrenPositionMap);
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
