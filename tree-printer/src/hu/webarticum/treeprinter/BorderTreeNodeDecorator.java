package hu.webarticum.treeprinter;

public class BorderTreeNodeDecorator extends AbstractTreeNodeDecorator {

    private final char topLeft;
    private final char top;
    private final char topRight;
    private final char right;
    private final char bottomRight;
    private final char bottom;
    private final char bottomLeft;
    private final char left;
    
    public BorderTreeNodeDecorator(TreeNode decoratedNode) {
        this(decoratedNode, UnicodeMode.isUnicodeDefault());
    }

    public BorderTreeNodeDecorator(TreeNode decoratedNode, boolean useUnicode) {
        this(
            decoratedNode,
            useUnicode ? '┌' : '.',
            useUnicode ? '─' : '-',
            useUnicode ? '┐' : '.',
            useUnicode ? '│' : '|',
            useUnicode ? '┘' : '\'',
            useUnicode ? '─' : '-',
            useUnicode ? '└' : '`',
            useUnicode ? '│' : '|'
        );
    }

    public BorderTreeNodeDecorator(TreeNode decoratedNode, char character) {
        this(
            decoratedNode,
            character, character, character, character,
            character, character, character, character
        );
    }

    public BorderTreeNodeDecorator(
        TreeNode decoratedNode,
        char topLeft, char top, char topRight, char right,
        char bottomRight, char bottom, char bottomLeft, char left
    ) {
        super(decoratedNode);
        this.topLeft = topLeft;
        this.top = top;
        this.topRight = topRight;
        this.right = right;
        this.bottomRight = bottomRight;
        this.bottom = bottom;
        this.bottomLeft = bottomLeft;
        this.left = left;
    }
    
    public BorderTreeNodeDecorator(
        TreeNode decoratedNode,
        boolean decorable, boolean inherit, boolean forceInherit,
        char topLeft, char top, char topRight, char right,
        char bottomRight, char bottom, char bottomLeft, char left
    ) {
        super(decoratedNode, decorable, inherit, forceInherit);
        this.topLeft = topLeft;
        this.top = top;
        this.topRight = topRight;
        this.right = right;
        this.bottomRight = bottomRight;
        this.bottom = bottom;
        this.bottomLeft = bottomLeft;
        this.left = left;
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
        StringBuilder borderedContentBuilder = new StringBuilder();
        borderedContentBuilder.append(topLeft);
        Util.repeat(borderedContentBuilder, top, longestLineLength);
        borderedContentBuilder.append(topRight);
        borderedContentBuilder.append("\n");
        for (String contentLine: contentLines) {
            borderedContentBuilder.append(left);
            borderedContentBuilder.append(contentLine);
            Util.repeat(borderedContentBuilder, ' ', longestLineLength - contentLine.length());
            borderedContentBuilder.append(right);
            borderedContentBuilder.append("\n");
        }
        borderedContentBuilder.append(bottomLeft);
        Util.repeat(borderedContentBuilder, bottom, longestLineLength);
        borderedContentBuilder.append(bottomRight);
        String borderedContent = borderedContentBuilder.toString();
        
        return borderedContent;
    }

    @Override
    public int[] getInsets() {
        int[] innerInsets = decoratedNode.getInsets();
        return new int[] {
            innerInsets[0] + 1,
            innerInsets[1] + 1,
            innerInsets[2] + 1,
            innerInsets[3] + 1,
        };
    }
    
    @Override
    protected TreeNode decorateChild(TreeNode childNode) {
        return new BorderTreeNodeDecorator(
            childNode,
            decorable, inherit, forceInherit,
            topLeft, top, topRight, right,
            bottomRight, bottom, bottomLeft, left
        );
    }
    
}
