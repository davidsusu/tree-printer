package hu.webarticum.treeprinter.printer.traditional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.util.Util;

public class DefaultAligner implements Aligner {

    public enum Alignment { LEFT, CENTER, RIGHT }

    public enum ConnectMode { CONTENT, CONTEXT }
    
    private final Alignment contentAlign;
    private final int contentOffset;
    private final ConnectMode topConnectionConnect;
    private final Alignment topConnectionAlign;
    private final int topConnectionOffset;
    private final ConnectMode bottomConnectionConnect;
    private final Alignment bottomConnectionAlign;
    private final int bottomConnectionOffset;
    private final Alignment childrenAlign;
    private final int gap;

    public DefaultAligner() {
        this(Alignment.CENTER);
    }

    public DefaultAligner(Alignment align) {
        this(align, 1);
    }
    
    public DefaultAligner(Alignment align, int gap) {
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
        int left = calculateLeft(position, width, contentWidth, contentMaxLeft);
        int topConnection = calculateTopConnection(left, position, width, contentWidth, connectionMaxLeft);
        int bottomConnection = calculateBottomConnection(left, position, width, contentWidth, connectionMaxLeft);
        return new Align(left, topConnection, bottomConnection);
    }
    
    private int calculateLeft(int position, int width, int contentWidth, int contentMaxLeft) {
        int relativeLeft;
        if (contentAlign == Alignment.LEFT) {
            relativeLeft = position;
        } else if (contentAlign == Alignment.RIGHT) {
            relativeLeft = contentMaxLeft;
        } else {
            relativeLeft = position + (width - contentWidth) / 2;
        }
        
        return restrictInt(relativeLeft + contentOffset, 0, contentMaxLeft);
    }
    
    private int calculateTopConnection(int left, int position, int width, int contentWidth, int connectionMaxLeft) {
        int relativeTopConnection;
        if (topConnectionConnect == ConnectMode.CONTENT) {
            if (topConnectionAlign == Alignment.LEFT) {
                relativeTopConnection = left;
            } else if (topConnectionAlign == Alignment.RIGHT) {
                relativeTopConnection = left + contentWidth - 1;
            } else {
                relativeTopConnection = left + (contentWidth / 2);
            }
        } else {
            if (topConnectionAlign == Alignment.LEFT) {
                relativeTopConnection = position;
            } else if (topConnectionAlign == Alignment.RIGHT) {
                relativeTopConnection = connectionMaxLeft;
            } else {
                relativeTopConnection = position + ((width - contentWidth) / 2);
            }
        }

        return restrictInt(relativeTopConnection + topConnectionOffset, 0, connectionMaxLeft);
    }
    
    private int calculateBottomConnection(int left, int position, int width, int contentWidth, int connectionMaxLeft) {
        int bottomConnection;
        if (bottomConnectionConnect == ConnectMode.CONTENT) {
            if (bottomConnectionAlign == Alignment.LEFT) {
                bottomConnection = left;
            } else if (bottomConnectionAlign == Alignment.RIGHT) {
                bottomConnection = left + contentWidth - 1;
            } else {
                bottomConnection = left + (contentWidth / 2);
            }
        } else {
            if (bottomConnectionAlign == Alignment.LEFT) {
                bottomConnection = position;
            } else if (bottomConnectionAlign == Alignment.RIGHT) {
                bottomConnection = connectionMaxLeft;
            } else {
                bottomConnection = position + ((width - contentWidth) / 2);
            }
        }

        return restrictInt(bottomConnection + bottomConnectionOffset, 0, connectionMaxLeft);
    }

    private int restrictInt(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
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
        if (childrenAlign == Alignment.RIGHT) {
            offset = parentWidth - childrenWidth;
        } else if (childrenAlign == Alignment.CENTER) {
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

        private Alignment contentAlign = Alignment.CENTER;
        private int contentOffset = 0;
        private ConnectMode topConnectionConnect = ConnectMode.CONTENT;
        private Alignment topConnectionAlign = Alignment.CENTER;
        private int topConnectionOffset = 0;
        private ConnectMode bottomConnectionConnect = ConnectMode.CONTENT;
        private Alignment bottomConnectionAlign = Alignment.CENTER;
        private int bottomConnectionOffset = 0;
        private Alignment childrenAlign = Alignment.CENTER;
        private int gap = 1;

        public DefaultAligner.Builder align(Alignment align) {
            this.contentAlign = align;
            this.topConnectionAlign = align;
            this.bottomConnectionAlign = align;
            this.childrenAlign = align;
            return this;
        }

        public DefaultAligner.Builder contentAlign(Alignment contentAlign) {
            this.contentAlign = contentAlign;
            return this;
        }

        public DefaultAligner.Builder contentOffset(int contentOffset) {
            this.contentOffset = contentOffset;
            return this;
        }

        public DefaultAligner.Builder topConnectionConnect(ConnectMode topConnectionConnect) {
            this.topConnectionConnect = topConnectionConnect;
            return this;
        }

        public DefaultAligner.Builder topConnectionAlign(Alignment topConnectionAlign) {
            this.topConnectionAlign = topConnectionAlign;
            return this;
        }

        public DefaultAligner.Builder topConnectionOffset(int topConnectionOffset) {
            this.topConnectionOffset = topConnectionOffset;
            return this;
        }

        public DefaultAligner.Builder bottomConnectionConnect(ConnectMode bottomConnectionConnect) {
            this.bottomConnectionConnect = bottomConnectionConnect;
            return this;
        }

        public DefaultAligner.Builder bottomConnectionAlign(Alignment bottomConnectionAlign) {
            this.bottomConnectionAlign = bottomConnectionAlign;
            return this;
        }

        public DefaultAligner.Builder bottomConnectionOffset(int bottomConnectionOffset) {
            this.bottomConnectionOffset = bottomConnectionOffset;
            return this;
        }

        public DefaultAligner.Builder childrenAlign(Alignment childrenAlign) {
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