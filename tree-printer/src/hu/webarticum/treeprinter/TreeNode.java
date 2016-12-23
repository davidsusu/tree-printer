package hu.webarticum.treeprinter;

import java.util.List;

public interface TreeNode {
	
	public String getContent();
	
	public List<TreeNode> getChildren();
	
}
