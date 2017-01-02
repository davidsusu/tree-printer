package hu.webarticum.treeprinter;

public class PadTreeNodeDecorator extends AbstractTreeNodeDecorator {

    private final int topPad;
    private final int rightPad;
    private final int bottomPad;
    private final int leftPad;
    
    public PadTreeNodeDecorator(TreeNode decoratedNode) {
        this(decoratedNode, 1);
    }

    public PadTreeNodeDecorator(TreeNode decoratedNode, int pad) {
        this(decoratedNode, pad, pad, pad, pad);
    }

    public PadTreeNodeDecorator(TreeNode decoratedNode, int topPad, int rightPad, int bottomPad, int leftPad) {
        super(decoratedNode);
        this.topPad = topPad;
        this.rightPad = rightPad;
        this.bottomPad = bottomPad;
        this.leftPad = leftPad;
    }
    
    public PadTreeNodeDecorator(
        TreeNode decoratedNode, boolean decorable, boolean inherit, boolean forceInherit,
        int topPad, int rightPad, int bottomPad, int leftPad
    ) {
        super(decoratedNode, decorable, inherit, forceInherit);
        this.topPad = topPad;
        this.rightPad = rightPad;
        this.bottomPad = bottomPad;
        this.leftPad = leftPad;
    }
    
    @Override
    public String getContent() {
        String content = decoratedNode.getContent();

        String[] contentLines = content.split("\n");
        int longestLineLength = 0;
        for (String line: contentLines) {
            int lineLength = line.length();
            if (lineLength > longestLineLength) {
                longestLineLength = lineLength;
            }
        }

        StringBuilder padContentBuilder = new StringBuilder();
        for (int i = 0; i < topPad; i++) {
            Util.repeat(padContentBuilder, ' ', leftPad + longestLineLength + rightPad);
            padContentBuilder.append('\n');
        }

        for (String line: contentLines) {
            Util.repeat(padContentBuilder, ' ', leftPad);
            padContentBuilder.append(line);
            Util.repeat(padContentBuilder, ' ', longestLineLength - line.length() + rightPad);
            padContentBuilder.append('\n');
        }
        
        for (int i = 0; i < bottomPad; i++) {
            Util.repeat(padContentBuilder, ' ', leftPad + longestLineLength + rightPad);
            padContentBuilder.append('\n');
        }
        
        String padContent = padContentBuilder.toString();
        return padContent;
    }

    @Override
    public int[] getInsets() {
        int[] innerInsets = decoratedNode.getInsets();
        return new int[] {
            innerInsets[0] + topPad,
            innerInsets[1] + rightPad,
            innerInsets[2] + bottomPad,
            innerInsets[3] + leftPad,
        };
    }
    
    @Override
    protected TreeNode decorateChild(TreeNode childNode) {
        return new PadTreeNodeDecorator(
            childNode, decorable, inherit, forceInherit,
            topPad, rightPad, bottomPad, leftPad
        );
    }
    
}
