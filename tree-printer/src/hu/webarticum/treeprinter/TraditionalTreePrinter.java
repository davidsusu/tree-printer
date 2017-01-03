package hu.webarticum.treeprinter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraditionalTreePrinter extends AbstractTreePrinter {

    static public final Aligner DEFAULT_ALIGNER = new DefaultAligner();
    
    private final int margin;
    
    private final Aligner aligner;
    
    public TraditionalTreePrinter() {
        this(1, DEFAULT_ALIGNER);
    }
    
    public TraditionalTreePrinter(int margin) {
        this(margin, DEFAULT_ALIGNER);
    }

    public TraditionalTreePrinter(Aligner aligner) {
        this(1, aligner);
    }
    
    public TraditionalTreePrinter(int margin, Aligner aligner) {
        this.margin = margin;
        this.aligner = aligner;
    }
    
    @Override
    public void print(TreeNode rootNode, Appendable out) {
        Map<TreeNode, Integer> widthMap = new HashMap<TreeNode, Integer>();
        int rootWidth = collectWidths(widthMap, rootNode);
        
        Map<TreeNode, Position> positionMap = new HashMap<TreeNode, Position>();
        
        String rootContent = rootNode.getContent();
        int[] rootContentDimension = Util.getContentDimension(rootContent);
        Align rootAlign = aligner.alignNode(rootNode, 0, rootWidth, rootContentDimension[0]);
        positionMap.put(rootNode, new Position(0, 0, rootAlign.bottomConnection, rootAlign.left, rootContentDimension[1]));
        
        LineBuffer buffer = new LineBuffer(out);
        
        buffer.write(0, rootAlign.left, rootContent);
        
        buffer.flush();
        
        while (true) {
            Map<TreeNode, Position> newPositionMap = new HashMap<TreeNode, Position>();
            List<Integer> childBottoms = new ArrayList<Integer>();
            for (Map.Entry<TreeNode, Position> entry: positionMap.entrySet()) {
                TreeNode node = entry.getKey();
                Position position = entry.getValue();
                Map<TreeNode, Position> childrenPositionMap = new HashMap<TreeNode, Position>();
                List<TreeNode> children = node.getChildren();
                children.removeAll(Collections.singleton(null));
                int[] childrenAlign = aligner.alignChildren(node, children, position.col, margin, widthMap);
                
                if (!children.isEmpty()) {
                    int childCount = children.size();
                    int[] childConnections = new int[childCount];
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
                        childConnections[i] = childAlign.topConnection;
                    }
                    
                    int connectionRows = printConnections(
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
    
    // TODO: align children areas...
    public interface Aligner {
        
        public Align alignNode(TreeNode node, int position, int width, int contentWidth);
        
        public int[] alignChildren(TreeNode parentNode, List<TreeNode> children, int position, int margin, Map<TreeNode, Integer> widthMap);
        
    }
    
    public static class DefaultAligner implements Aligner {

        public static int LEFT = 0;
        public static int CENTER = 1;
        public static int RIGHT = 2;

        public static int CONNECT_TO_CONTENT = 0;
        public static int CONNECT_TO_CONTEXT = 1;
        
        private final int contentAlign;
        private final int contentOffset;
        private final int topConnectionConnect;
        private final int topConnectionAlign;
        private final int topConnectionOffset;
        private final int bottomConnectionConnect;
        private final int bottomConnectionAlign;
        private final int bottomConnectionOffset;
        private final int childrenAlign;

        public DefaultAligner() {
            this(CENTER);
        }

        public DefaultAligner(int align) {
            this(align, 0, CONNECT_TO_CONTENT, align, 0, CONNECT_TO_CONTENT, align, 0, align);
        }
        
        public DefaultAligner(
            int contentAlign, int contentOffset,
            int topConnectionConnect, int topConnectionAlign, int topConnectionOffset,
            int bottomConnectionConnect, int bottomConnectionAlign, int bottomConnectionOffset,
            int childrenAlign
        ) {
            this.contentAlign = contentAlign;
            this.contentOffset = contentOffset;
            this.topConnectionConnect = topConnectionConnect;
            this.topConnectionAlign = topConnectionAlign;
            this.topConnectionOffset = topConnectionOffset;
            this.bottomConnectionConnect = bottomConnectionConnect;
            this.bottomConnectionAlign = bottomConnectionAlign;
            this.bottomConnectionOffset = bottomConnectionOffset;
            this.childrenAlign = childrenAlign;
        }

        @Override
        public Align alignNode(TreeNode node, int position, int width, int contentWidth) {
            int contentMaxLeft = position + width - contentWidth;
            int connectionMaxLeft = position + width - 1;
            
            int left;
            if (contentAlign == LEFT) {
                left = position;
            } else if (contentAlign == RIGHT) {
                left = contentMaxLeft;
            } else {
                left = position + (width - contentWidth) / 2;
            }
            
            left = Math.max(0, Math.min(contentMaxLeft, left + contentOffset));
            
            
            int topConnection;
            if (topConnectionConnect == CONNECT_TO_CONTENT) {
                if (topConnectionAlign == LEFT) {
                    topConnection = left;
                } else if (topConnectionAlign == RIGHT) {
                    topConnection = left + contentWidth - 1;
                } else {
                    topConnection = left + (contentWidth / 2);
                }
            } else {
                if (topConnectionAlign == LEFT) {
                    topConnection = position;
                } else if (topConnectionAlign == RIGHT) {
                    topConnection = connectionMaxLeft;
                } else {
                    topConnection = position + ((width - contentWidth) / 2);
                }
            }

            topConnection = Math.max(0, Math.min(connectionMaxLeft, topConnection + topConnectionOffset));
            
            
            int bottomConnection;
            if (bottomConnectionConnect == CONNECT_TO_CONTENT) {
                if (bottomConnectionAlign == LEFT) {
                    bottomConnection = left;
                } else if (bottomConnectionAlign == RIGHT) {
                    bottomConnection = left + contentWidth - 1;
                } else {
                    bottomConnection = left + (contentWidth / 2);
                }
            } else {
                if (bottomConnectionAlign == LEFT) {
                    bottomConnection = position;
                } else if (bottomConnectionAlign == RIGHT) {
                    bottomConnection = connectionMaxLeft;
                } else {
                    bottomConnection = position + ((width - contentWidth) / 2);
                }
            }

            bottomConnection = Math.max(0, Math.min(connectionMaxLeft, bottomConnection + bottomConnectionOffset));
            
            
            return new Align(left, topConnection, bottomConnection);
        }

        @Override
        public int[] alignChildren(TreeNode parentNode, List<TreeNode> children, int position, int margin, Map<TreeNode, Integer> widthMap) {
            int[] result = new int[children.size()];
            int childrenCount = children.size();
            int childrenWidth = 0;
            boolean first = true;
            for (int i = 0; i < childrenCount; i++) {
                TreeNode childNode = children.get(i);
                if (first) {
                    first = false;
                } else {
                    childrenWidth += margin;
                }
                int childWidth = widthMap.get(childNode);
                result[i] = position + childrenWidth;
                childrenWidth += childWidth;
            }
            int parentWidth = widthMap.get(parentNode);
            int offset = 0;
            if (childrenAlign == RIGHT) {
                offset = parentWidth - childrenWidth;
            } else if (childrenAlign == CENTER) {
                offset = (parentWidth - childrenWidth) / 2;
            }
            if (offset > 0) {
                for (int i = 0; i < childrenCount; i++) {
                    result[i] += offset;
                }
            }
            return result;
        }
        
    }

    public static class Align {

        final int left;

        final int topConnection;

        final int bottomConnection;

        public Align(int left, int topConnection, int bottomConnection) {
            this.left = left;
            this.topConnection = topConnection;
            this.bottomConnection = bottomConnection;
        }
        
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





