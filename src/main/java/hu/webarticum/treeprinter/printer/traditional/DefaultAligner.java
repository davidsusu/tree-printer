package hu.webarticum.treeprinter.printer.traditional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.util.Util;

public class DefaultAligner implements Aligner {

    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;

    public static final int CONNECT_TO_CONTENT = 0;
    public static final int CONNECT_TO_CONTEXT = 1;
    
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
        this(createBuilder().align(align).gap(gap));
    }
    
    private DefaultAligner(Builder builder) {
        this.contentAlign = builder.contentAlign;
        this.contentOffset = builder.contentOffset;
        this.topConnectionConnect = builder.topConnectionConnect;
        this.topConnectionAlign = builder.topConnectionAlign;
        this.topConnectionOffset = builder.topConnectionOffset;
        this.bottomConnectionConnect = builder.bottomConnectionConnect;
        this.bottomConnectionAlign = builder.bottomConnectionAlign;
        this.bottomConnectionOffset = builder.bottomConnectionOffset;
        this.childrenAlign = builder.childrenAlign;
        this.gap = builder.gap;
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
    
    public static DefaultAligner.Builder createBuilder() {
        return new Builder();
    }
    
    public static class Builder {

        private int contentAlign = CENTER;
        private int contentOffset = 0;
        private int topConnectionConnect = CONNECT_TO_CONTENT;
        private int topConnectionAlign = CENTER;
        private int topConnectionOffset = 0;
        private int bottomConnectionConnect = CONNECT_TO_CONTENT;
        private int bottomConnectionAlign = CENTER;
        private int bottomConnectionOffset = 0;
        private int childrenAlign = CENTER;
        private int gap = 1;

        public DefaultAligner.Builder align(int align) {
            this.contentAlign = align;
            this.topConnectionAlign = align;
            this.bottomConnectionAlign = align;
            this.childrenAlign = align;
            return this;
        }

        public DefaultAligner.Builder contentAlign(int contentAlign) {
            this.contentAlign = contentAlign;
            return this;
        }

        public DefaultAligner.Builder contentOffset(int contentOffset) {
            this.contentOffset = contentOffset;
            return this;
        }

        public DefaultAligner.Builder topConnectionConnect(int topConnectionConnect) {
            this.topConnectionConnect = topConnectionConnect;
            return this;
        }

        public DefaultAligner.Builder topConnectionAlign(int topConnectionAlign) {
            this.topConnectionAlign = topConnectionAlign;
            return this;
        }

        public DefaultAligner.Builder topConnectionOffset(int topConnectionOffset) {
            this.topConnectionOffset = topConnectionOffset;
            return this;
        }

        public DefaultAligner.Builder bottomConnectionConnect(int bottomConnectionConnect) {
            this.bottomConnectionConnect = bottomConnectionConnect;
            return this;
        }

        public DefaultAligner.Builder bottomConnectionAlign(int bottomConnectionAlign) {
            this.bottomConnectionAlign = bottomConnectionAlign;
            return this;
        }

        public DefaultAligner.Builder bottomConnectionOffset(int bottomConnectionOffset) {
            this.bottomConnectionOffset = bottomConnectionOffset;
            return this;
        }

        public DefaultAligner.Builder childrenAlign(int childrenAlign) {
            this.childrenAlign = childrenAlign;
            return this;
        }

        public DefaultAligner.Builder gap(int gap) {
            this.gap = gap;
            return this;
        }

        public DefaultAligner build() {
            return new DefaultAligner(this);
        }
        
    }
    
}