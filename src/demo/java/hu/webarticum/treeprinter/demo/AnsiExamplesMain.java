package hu.webarticum.treeprinter.demo;

import hu.webarticum.treeprinter.SimpleTreeNode;
import hu.webarticum.treeprinter.decorator.BorderTreeNodeDecorator;
import hu.webarticum.treeprinter.printer.boxing.BoxingTreePrinter;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;
import hu.webarticum.treeprinter.printer.traditional.TraditionalTreePrinter;
import hu.webarticum.treeprinter.text.AnsiConsoleText;

public class AnsiExampleMain {

    public static void main(String[] args) {
        SimpleTreeNode rootNode = new SimpleTreeNode("Root");
        
        SimpleTreeNode childNode1 = new SimpleTreeNode(
                new AnsiConsoleText("\u001B[1;32mChild 1\u001B[0m"));
        rootNode.addChild(new BorderTreeNodeDecorator(childNode1));
        
        SimpleTreeNode childNode2 = new SimpleTreeNode("Child 2");
        rootNode.addChild(childNode2);
        
        new ListingTreePrinter().print(rootNode);
        
        System.out.println();
        System.out.println();
        
        new TraditionalTreePrinter().print(rootNode);
        
        System.out.println();
        System.out.println();
        
        new BoxingTreePrinter().print(rootNode);
    }
    
}
