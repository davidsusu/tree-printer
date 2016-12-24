package hu.webarticum.treeprinter;

public class BorderNodePrettifier implements NodePrettifier {

	private char topLeft;
	private char top;
	private char topRight;
	private char right;
	private char bottomRight;
	private char bottom;
	private char bottomLeft;
	private char left;
	
	private NodePrettifier innerPrettifier;

	public BorderNodePrettifier() {
		this(null);
	}

	public BorderNodePrettifier(NodePrettifier innerPrettifier) {
		this('.', '-', '.', '|', '\'', '-', '`', '|', innerPrettifier);
	}

	public BorderNodePrettifier(char character) {
		this(
			character, character, character, character,
			character, character, character, character,
			null
		);
	}

	public BorderNodePrettifier(char character, NodePrettifier innerPrettifier) {
		this(
			character, character, character, character,
			character, character, character, character,
			innerPrettifier
		);
	}

	public BorderNodePrettifier(
		char topLeft, char top, char topRight, char right,
		char bottomRight, char bottom, char bottomLeft, char left
	) {
		this(topLeft, top, topRight, right, bottomRight, bottom, bottomLeft, left, null);
	}

	public BorderNodePrettifier(
		char topLeft, char top, char topRight, char right,
		char bottomRight, char bottom, char bottomLeft, char left,
		NodePrettifier innerPrettifier
	) {
		this.topLeft = topLeft;
		this.top = top;
		this.topRight = topRight;
		this.right = right;
		this.bottomRight = bottomRight;
		this.bottom = bottom;
		this.bottomLeft = bottomLeft;
		this.left = left;
		this.innerPrettifier = innerPrettifier;
	}
	
	@Override
	public PrettifierResult prettify(TreeNode node) {
		String content;
		int offsetX = 1;
		int offsetY = 1;
		if (innerPrettifier != null) {
			PrettifierResult result = innerPrettifier.prettify(node);
			content = result.content;
			offsetX += result.offsetX;
			offsetY += result.offsetY;
		} else {
			content = node.getContent();
		}
		
		String[] contentLines = content.split("\n");
		int longestLineLength = 0;
		for (String line: contentLines) {
			int lineLength = line.length();
			if (lineLength > longestLineLength) {
				longestLineLength = lineLength;
			}
		}
		StringBuilder prettifiedContentBuilder = new StringBuilder();
		prettifiedContentBuilder.append(topLeft);
		repeat(prettifiedContentBuilder, top, longestLineLength);
		prettifiedContentBuilder.append(topRight);
		prettifiedContentBuilder.append("\n");
		for (String contentLine: contentLines) {
			prettifiedContentBuilder.append(left);
			prettifiedContentBuilder.append(contentLine);
			repeat(prettifiedContentBuilder, ' ', longestLineLength - contentLine.length());
			prettifiedContentBuilder.append(right);
			prettifiedContentBuilder.append("\n");
		}
		prettifiedContentBuilder.append(bottomLeft);
		repeat(prettifiedContentBuilder, bottom, longestLineLength);
		prettifiedContentBuilder.append(bottomRight);
		String prettifiedContent = prettifiedContentBuilder.toString();
		
		return new PrettifierResult(prettifiedContent, offsetX, offsetY);
	}
	
	private void repeat(StringBuilder stringBuilder, char character, int repeats) {
		for (int i = 0; i < repeats; i ++) {
			stringBuilder.append(character);
		}
	}

}
