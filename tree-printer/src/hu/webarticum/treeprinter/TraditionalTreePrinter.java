package hu.webarticum.treeprinter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraditionalTreePrinter extends AbstractTreePrinter {

    static public final Aligner DEFAULT_ALIGNER = new DefaultAligner();

    static public final Liner DEFAULT_LINER = new DefaultLiner();

    private final Aligner aligner;

    private final Liner liner;
    
    public TraditionalTreePrinter() {
        this(DEFAULT_ALIGNER, DEFAULT_LINER);
    }
    
    public TraditionalTreePrinter(Aligner aligner, Liner liner) {
        this.aligner = aligner;
        this.liner = liner;
    }
    
    @Override
    public void print(TreeNode rootNode, Appendable out) {
        Map<TreeNode, Integer> widthMap = new HashMap<TreeNode, Integer>();
        int rootWidth = aligner.collectWidths(widthMap, rootNode);
        
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
                int[] childrenAlign = aligner.alignChildren(node, children, position.col, widthMap);
                
                if (!children.isEmpty()) {
                    int childCount = children.size();
                    List<Integer> childConnections = new ArrayList<Integer>(childCount);
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
    
    public interface Aligner {
        
        public Align alignNode(TreeNode node, int position, int width, int contentWidth);
        
        public int[] alignChildren(TreeNode parentNode, List<TreeNode> children, int position, Map<TreeNode, Integer> widthMap);
        
        public int collectWidths(Map<TreeNode, Integer> widthMap, TreeNode node);
        
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
        private final int gap;

        public DefaultAligner() {
            this(CENTER);
        }

        public DefaultAligner(int align) {
            this(align, 1);
        }
        
        public DefaultAligner(int align, int gap) {
            this(align, 0, CONNECT_TO_CONTENT, align, 0, CONNECT_TO_CONTENT, align, 0, align, gap);
        }
        
        public DefaultAligner(
            int contentAlign, int contentOffset,
            int topConnectionConnect, int topConnectionAlign, int topConnectionOffset,
            int bottomConnectionConnect, int bottomConnectionAlign, int bottomConnectionOffset,
            int childrenAlign,
            int gap
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
            this.gap = gap;
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
        public int[] alignChildren(TreeNode parentNode, List<TreeNode> children, int position, Map<TreeNode, Integer> widthMap) {
            int[] result = new int[children.size()];
            int childrenCount = children.size();
            int childrenWidth = 0;
            boolean first = true;
            for (int i = 0; i < childrenCount; i++) {
                TreeNode childNode = children.get(i);
                if (first) {
                    first = false;
                } else {
                    childrenWidth += gap;
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
        
        @Override
        public int collectWidths(Map<TreeNode, Integer> widthMap, TreeNode node) {
            int contentWidth = Util.getContentDimension(node.getContent())[0];
            int childrenWidth = 0;
            boolean first = true;
            List<TreeNode> children = node.getChildren();
            children.removeAll(Collections.singleton(null));
            for (TreeNode childNode: children) {
                if (first) {
                    first = false;
                } else {
                    childrenWidth += gap;
                }
                childrenWidth += collectWidths(widthMap, childNode);
            }
            int nodeWidth = Math.max(contentWidth, childrenWidth);
            widthMap.put(node, nodeWidth);
            return nodeWidth;
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
    
    public interface Liner {
        
        public int printConnections(LineBuffer buffer, int row, int topConnection, List<Integer> bottomConnections);
        
    }
    
    public static class DefaultLiner implements Liner {

        private final char topConnectionChar;
        private final char bracketLeftChar;
        private final char bracketChar;
        private final char bracketTopChar;
        private final char bracketTopLeftChar;
        private final char bracketTopRightChar;
        private final char bracketBottomChar;
        private final char bracketTopAndBottomChar;
        private final char bracketTopAndBottomLeftChar;
        private final char bracketTopAndBottomRightChar;
        private final char bracketRightChar;
        private final char bracketOnlyChar;
        private final char bottomConnectionChar;

        private final int topHeight;
        private final int bottomHeight;

        private final boolean displayBracket;

        public DefaultLiner() {
            this(UnicodeMode.isUnicodeDefault());
        }

        public DefaultLiner(boolean useUnicode) {
            this(
                useUnicode ? '│' : '|',
                useUnicode ? '┌' : ' ',
                useUnicode ? '─' : '_',
                useUnicode ? '┴' : '|',
                useUnicode ? '└' : '|',
                useUnicode ? '┘' : '|',
                useUnicode ? '┬' : '_',
                useUnicode ? '┼' : '|',
                useUnicode ? '├' : '|',
                useUnicode ? '┤' : '|',
                useUnicode ? '┐' : ' ',
                useUnicode ? '│' : '|',
                useUnicode ? '│' : '|',
                0, 1, true
            );
        }
        
        public DefaultLiner(
            char topConnectionChar, char bracketLeftChar, char bracketChar,
            char bracketTopChar, char bracketTopLeftChar, char bracketTopRightChar, char bracketBottomChar,
            char bracketTopAndBottomChar, char bracketTopAndBottomLeftChar, char bracketTopAndBottomRightChar,
            char bracketRightChar, char bracketOnlyChar, char bottomConnectionChar,
            int topHeight, int bottomHeight, boolean displayBracket
        ) {
            this.topConnectionChar = topConnectionChar;
            this.bracketLeftChar = bracketLeftChar;
            this.bracketChar = bracketChar;
            this.bracketTopChar = bracketTopChar;
            this.bracketTopLeftChar = bracketTopLeftChar;
            this.bracketTopRightChar = bracketTopRightChar;
            this.bracketBottomChar = bracketBottomChar;
            this.bracketTopAndBottomChar = bracketTopAndBottomChar;
            this.bracketTopAndBottomLeftChar = bracketTopAndBottomLeftChar;
            this.bracketTopAndBottomRightChar = bracketTopAndBottomRightChar;
            this.bracketRightChar = bracketRightChar;
            this.bracketOnlyChar = bracketOnlyChar;
            this.bottomConnectionChar = bottomConnectionChar;
            this.topHeight = topHeight;
            this.bottomHeight = bottomHeight;
            this.displayBracket = displayBracket;
        }
        
        @Override
        public int printConnections(LineBuffer buffer, int row, int topConnection, List<Integer> bottomConnections) {
            int start = Math.min(topConnection, bottomConnections.get(0));
            int end = Math.max(topConnection, bottomConnections.get(bottomConnections.size() - 1));
            int topHeightWithBracket = topHeight + (displayBracket ? 1 : 0);
            int fullHeight = topHeightWithBracket + bottomHeight;
            
            {
                StringBuilder topConnectionLineBuilder = new StringBuilder();
                Util.repeat(topConnectionLineBuilder, ' ', topConnection - start);
                topConnectionLineBuilder.append(topConnectionChar);
                String topConnectionLine = topConnectionLineBuilder.toString();
                for (int i = 0; i < topHeight; i++) {
                    buffer.write(row + i, start, topConnectionLine);
                }
            }
            
            {
                StringBuilder bracketLineBuilder = new StringBuilder();
                for (int i = start; i <= end; i++) {
                    char character;
                    if (start == end) {
                        character = bracketOnlyChar;
                    } else if (i == topConnection) {
                        if (bottomConnections.contains(i)) {
                            if (i == start) {
                                character = bracketTopAndBottomLeftChar;
                            } else if (i == end) {
                                character = bracketTopAndBottomRightChar;
                            } else {
                                character = bracketTopAndBottomChar;
                            }
                        } else {
                            if (i == start) {
                                character = bracketTopLeftChar;
                            } else if (i == end) {
                                character = bracketTopRightChar;
                            } else {
                                character = bracketTopChar;
                            }
                        }
                    } else if (i == start) {
                        character = bracketLeftChar;
                    } else if (i == end) {
                        character = bracketRightChar;
                    } else {
                        if (bottomConnections.contains(i)) {
                            character = bracketBottomChar;
                        } else {
                            character = bracketChar;
                        }
                    }
                    bracketLineBuilder.append(character);
                }
                buffer.write(row + topHeight, start, bracketLineBuilder.toString());
            }
            
            {
                StringBuilder bottomConnectionLineBuilder = new StringBuilder();
                int position = start;
                for (int bottomConnection: bottomConnections) {
                    for (int i = position; i < bottomConnection; i++) {
                        bottomConnectionLineBuilder.append(' ');
                    }
                    bottomConnectionLineBuilder.append(bottomConnectionChar);
                    position = bottomConnection + 1;
                }
                String bottomConnectionLine = bottomConnectionLineBuilder.toString();
                for (int i = topHeightWithBracket; i < fullHeight; i++) {
                    buffer.write(row + i, start, bottomConnectionLine);
                }
            }
            
            return fullHeight;
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





