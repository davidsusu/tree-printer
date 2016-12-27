package hu.webarticum.treeprinter;

import java.util.List;

public interface TreeNode {
	
	public String getContent();
	
	// XXX
	public int getOffsetX();
	
	// XXX
	public int getOffsetY();
	
	public List<TreeNode> getChildren();

	public boolean isDecorable();
	
}
