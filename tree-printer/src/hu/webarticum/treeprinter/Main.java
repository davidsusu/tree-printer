package hu.webarticum.treeprinter;

import java.util.ArrayList;
import java.util.List;

public class Main {
	
	public static void main(String[] args) {
		TestNode rootNode = new TestNode("root");
		TestNode subNode1 = new TestNode("SUB asdf\nSSS fdsa\nxxx yyy");
		TestNode subNode2 = new TestNode("lorem ipsum");
		TestNode subNode3 = new TestNode("ggggg");
		TestNode subSubNode11 = new TestNode("AAA");
		TestNode subSubNode12 = new TestNode("BBB");
		TestNode subSubNode21 = new TestNode("CCC");
		TestNode subSubNode22 = new TestNode("DDD");
		TestNode subSubNode23 = new TestNode("EEE");
		TestNode subSubNode24 = new TestNode("FFF");
		TestNode subSubNode31 = new TestNode("GGG");
		TestNode subSubSubNode231 = new TestNode("(eee)");
		TestNode subSubSubNode232 = new TestNode("(eee2)");
		TestNode subSubSubNode311 = new TestNode("(ggg)");

		rootNode.addChild(subNode1);
		rootNode.addChild(subNode2);
		rootNode.addChild(subNode3);
		subNode1.addChild(subSubNode11);
		subNode1.addChild(subSubNode12);
		subNode2.addChild(subSubNode21);
		subNode2.addChild(subSubNode22);
		subNode2.addChild(subSubNode23);
		subNode2.addChild(subSubNode24);
		subNode3.addChild(subSubNode31);
		subSubNode23.addChild(subSubSubNode231);
		subSubNode23.addChild(subSubSubNode232);
		subSubNode31.addChild(subSubSubNode311);

		(new SimpleTreePrinter()).print(rootNode);

		System.out.println();
		System.out.println("=====================");
		System.out.println();
		
		(new SimpleTreePrinter()).print(new BorderTreeNodeDecorator(new PadTreeNodeDecorator(rootNode, 1, 2, 1, 2)));
	}
	
	static private class TestNode implements TreeNode {
		
		final String content;
		
		List<TestNode> children = new ArrayList<TestNode>();
		
		TestNode(String content) {
			this.content = content;
		}
		
		void addChild(TestNode childNode) {
			children.add(childNode);
		}

		@Override
		public String getContent() {
			return content;
		}

		@Override
		public int getOffsetX() {
			return 0;
		}

		@Override
		public int getOffsetY() {
			return 0;
		}

		@Override
		public List<TreeNode> getChildren() {
			return new ArrayList<TreeNode>(children);
		}

		@Override
		public boolean isDecorable() {
			return (content.isEmpty() || content.charAt(0) != '(');
		}
		
		@Override
		public String toString() {
			return getContent();
		}

	}
	
}
