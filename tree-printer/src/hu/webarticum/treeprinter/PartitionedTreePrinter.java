package hu.webarticum.treeprinter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartitionedTreePrinter extends AbstractTreePrinter {
	
	private final int margin;
	
	public PartitionedTreePrinter() {
		this(1);
	}
	
	public PartitionedTreePrinter(int margin) {
		this.margin = margin;
	}
	
	@Override
	public void print(TreeNode rootNode, Appendable out) {
		Map<TreeNode, Integer> widthMap = new HashMap<TreeNode, Integer>();
		int rootWidth = collectWidths(widthMap, rootNode);
		Map<TreeNode, int[]> positionMap = new HashMap<TreeNode, int[]>();
		
		String rootContent = rootNode.getContent();
		int[] rootContentDimension = Util.getContentDimension(rootContent);
		int[] rootAlign = alignNode(0, rootWidth, rootContentDimension[0]);
		positionMap.put(rootNode, new int[] {0, 0, rootAlign[1], rootAlign[0], rootContentDimension[1]});
		
		LineBuffer buffer = new LineBuffer(out);
		
		buffer.write(0, rootAlign[0], rootContent);
		
		// TODO: buffer.flush
		
		while (true) {
			Map<TreeNode, int[]> newPositionMap = new HashMap<TreeNode, int[]>();
			for (Map.Entry<TreeNode, int[]> entry: positionMap.entrySet()) {
				TreeNode node = entry.getKey();
				int[] positions = entry.getValue();
				int row = positions[0];
				int col = positions[1];
				int connection = positions[2];
				int height = positions[4];
				Map<TreeNode, int[]> childrenPositionMap = new HashMap<TreeNode, int[]>();
				int childCol = col;
				List<TreeNode> children = node.getChildren();
				children.removeAll(Collections.singleton(null));
				
				if (!children.isEmpty()) {
					int childCount = children.size();
					int[] childConnections = new int[childCount];
					for (int i = 0; i < childCount; i++) {
						TreeNode childNode = children.get(i);
						int childWidth = widthMap.get(childNode);
						String childContent = childNode.getContent();
						int[] childContentDimension = Util.getContentDimension(childContent);
						int[] childAlign = alignNode(childCol, childWidth, childContentDimension[0]);
						int[] childPositionItem = new int[] {row + height, childCol, childAlign[1], childAlign[0], childContentDimension[1]};
						childrenPositionMap.put(childNode, childPositionItem);
						childCol += childWidth + margin;
						childConnections[i] = childAlign[1];
					}
					
					int connectionRows = printConnections(buffer, row + height, connection, childConnections);
					
					for (Map.Entry<TreeNode, int[]> childEntry: childrenPositionMap.entrySet()) {
						TreeNode childNode = childEntry.getKey();
						int[] childPositionItem = childEntry.getValue();
						childPositionItem[0] += connectionRows;
						int childRow = childPositionItem[0];
						int childLeft = childPositionItem[1]; // XXX
						buffer.write(childRow, childLeft, childNode.getContent());
					}
					
					newPositionMap.putAll(childrenPositionMap);
				}
			}

			// TODO: buffer.flush
			
			if (newPositionMap.isEmpty()) {
				break;
			} else {
				positionMap = newPositionMap;
			}
		}
		
		buffer.flush();
	}
	
	// TODO: aligning strategies
	private int[] alignNode(int position, int width, int contentWidth) {
		//return new int[] {position, position};
		
		int halfContentWidth = contentWidth / 2;
		int left = position + (width / 2) - contentWidth + halfContentWidth;
		return new int[] {left, left + halfContentWidth};
	}
	
	// FIXME: references vs ad hoc decorators
	private int collectWidths(Map<TreeNode, Integer> widthMap, TreeNode node) {
		int contentWidth = Util.getContentDimension(node.getContent())[0];
		int childrenWidth = 0;
		boolean first = true;
		List<TreeNode> children = node.getChildren();
		children.removeAll(Collections.singleton(null));
		for (TreeNode childNode: children) {
			if (first) {
				first = false;
			} else {
				childrenWidth += margin;
			}
			childrenWidth += collectWidths(widthMap, childNode);
		}
		int nodeWidth = Math.max(contentWidth, childrenWidth);
		widthMap.put(node, nodeWidth);
		return nodeWidth;
	}
	
	// TODO: lining strategies
	private int printConnections(LineBuffer buffer, int row, int topConnection, int[] bottomConnections) {
		int start = Math.min(topConnection, bottomConnections[0]);
		int end = Math.max(topConnection, bottomConnections[bottomConnections.length - 1]);
		
		StringBuilder aboveLineBuilder = new StringBuilder();
		for (int i = start; i <= end; i++) {
			char character;
			if (i == topConnection) {
				character = '|';
			} else if (i == start || i == end) {
				character = ' ';
			} else {
				character = '_';
			}
			aboveLineBuilder.append(character);
		}
		buffer.write(row, start, aboveLineBuilder.toString());

		StringBuilder belowLineBuilder = new StringBuilder();
		int position = start;
		for (int bottomConnection: bottomConnections) {
			for (int i = position; i < bottomConnection; i++) {
				belowLineBuilder.append(' ');
			}
			belowLineBuilder.append('|');
			position = bottomConnection + 1;
		}
		buffer.write(row + 1, start, belowLineBuilder.toString());
		
		return 2;
	}
	
}





