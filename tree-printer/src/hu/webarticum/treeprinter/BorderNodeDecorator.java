package hu.webarticum.treeprinter;

public class BorderNodeDecorator implements NodeDecorator {

	int borders;

	public BorderNodeDecorator() {
		this(1);
	}

	public BorderNodeDecorator(int borders) {
		this.borders = borders;
	}
	
	@Override
	public String getDecoratedContent(TreeNode node) {
		String content = node.getContent();
		for (int i = 0; i < borders; i++) {
			content = addBorder(content);
		}
		return content;
	}
	
	private String addBorder(String content) {
		String[] contentLines = content.split("\n");
		int longestLineLength = 0;
		for (String line: contentLines) {
			int lineLength = line.length();
			if (lineLength > longestLineLength) {
				longestLineLength = lineLength;
			}
		}
		StringBuilder decoratedContentBuilder = new StringBuilder();
		decoratedContentBuilder.append(".");
		repeat(decoratedContentBuilder, "-", longestLineLength);
		decoratedContentBuilder.append(".");
		decoratedContentBuilder.append("\n");
		for (String contentLine: contentLines) {
			decoratedContentBuilder.append("|");
			decoratedContentBuilder.append(contentLine);
			repeat(decoratedContentBuilder, " ", longestLineLength - contentLine.length());
			decoratedContentBuilder.append("|");
			decoratedContentBuilder.append("\n");
		}
		decoratedContentBuilder.append("`");
		repeat(decoratedContentBuilder, "-", longestLineLength);
		decoratedContentBuilder.append("'");
		return decoratedContentBuilder.toString();
	}
	
	@Override
	public int getOffset() {
		return borders;
	}
	
	private void repeat(StringBuilder stringBuilder, String string, int repeats) {
		for (int i = 0; i < repeats; i ++) {
			stringBuilder.append(string);
		}
	}

}
