package hu.webarticum.treeprinter.demo;

import hu.webarticum.treeprinter.SimpleTreeNode;
import hu.webarticum.treeprinter.decorator.BorderTreeNodeDecorator;
import hu.webarticum.treeprinter.printer.boxing.BoxingTreePrinter;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;
import hu.webarticum.treeprinter.printer.traditional.TraditionalTreePrinter;
import hu.webarticum.treeprinter.text.AnsiConsoleText;
import hu.webarticum.treeprinter.text.AnsiFormat;

public class AnsiExamplesMain {

    public static void main(String[] args) {
        SimpleTreeNode rootNode = new SimpleTreeNode("Root");
        
        SimpleTreeNode childNode1 = new SimpleTreeNode(
                new AnsiConsoleText("\u001B[1;32mChild 1\u001B[0m"));
        rootNode.addChild(new BorderTreeNodeDecorator(childNode1));
        
        SimpleTreeNode childNode2 = new SimpleTreeNode("Child 2");
        rootNode.addChild(childNode2);

        SimpleTreeNode grandChildNode22 = new SimpleTreeNode("Grandchild 2-2");
        childNode2.addChild(grandChildNode22);
        
        new ListingTreePrinter().print(rootNode);
        
        System.out.println();
        System.out.println();
        
        new TraditionalTreePrinter().print(rootNode);
        
        System.out.println();
        System.out.println();
        
        BoxingTreePrinter.builder()
                .defaultFormat(AnsiFormat.CYAN)
                .levelFormat(1, AnsiFormat.YELLOW)
                .levelFormat(2, AnsiFormat.RED)
                .build()
                .print(rootNode);
    }
    
}
