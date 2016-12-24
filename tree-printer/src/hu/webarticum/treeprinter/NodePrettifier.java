package hu.webarticum.treeprinter;

public interface NodePrettifier {
	
	public PrettifierResult prettify(TreeNode node);
	
	static public class PrettifierResult {
		
		public final String content;
		
		public final int offsetX;
		
		public final int offsetY;
		
		public PrettifierResult(String content, int offsetX, int offsetY) {
			this.content = content;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
		}
		
	}
	
}
