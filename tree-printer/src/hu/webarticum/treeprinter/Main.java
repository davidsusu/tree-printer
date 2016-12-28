package hu.webarticum.treeprinter;

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
		subNode2.addChild(null);
		subNode3.addChild(subSubNode31);
		subSubNode23.addChild(subSubSubNode231);
		subSubNode23.addChild(subSubSubNode232);
		subSubNode31.addChild(subSubSubNode311);
		subSubNode31.addChild(null);

		(new SimpleTreePrinter()).print(rootNode);

		System.out.println();
		System.out.println("=====================");
		System.out.println();

		(new SimpleTreePrinter(false, true)).print(rootNode);

		System.out.println();
		System.out.println("=====================");
		System.out.println();
		
		(new SimpleTreePrinter()).print(new BorderTreeNodeDecorator(new PadTreeNodeDecorator(rootNode, 1, 2, 1, 2)));
	}
	
	static private class TestNode extends SimpleTreeNode {

		TestNode(String content) {
			super(content);
		}

		@Override
		public boolean isDecorable() {
			return (content.isEmpty() || content.charAt(0) != '(');
		}
		
	}
	
}
