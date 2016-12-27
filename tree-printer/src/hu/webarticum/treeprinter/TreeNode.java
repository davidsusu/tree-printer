package hu.webarticum.treeprinter;

import java.util.List;

public interface TreeNode {
	
	public String getContent();
	
	// TODO: width/height contentWidth/~Height contentOffsetX/~Y ?
	public int getOffsetX();
	public int getOffsetY();
	
	public List<TreeNode> getChildren();

	public boolean isDecorable();
	
}
