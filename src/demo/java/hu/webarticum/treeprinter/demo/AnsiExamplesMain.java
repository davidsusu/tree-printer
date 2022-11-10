package hu.webarticum.treeprinter.demo;

import hu.webarticum.treeprinter.SimpleTreeNode;
import hu.webarticum.treeprinter.decorator.BorderTreeNodeDecorator;
import hu.webarticum.treeprinter.decorator.PadTreeNodeDecorator;
import hu.webarticum.treeprinter.decorator.ShadowTreeNodeDecorator;
import hu.webarticum.treeprinter.printer.boxing.BoxingTreePrinter;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;
import hu.webarticum.treeprinter.printer.traditional.DefaultLiner;
import hu.webarticum.treeprinter.printer.traditional.Liner;
import hu.webarticum.treeprinter.printer.traditional.TraditionalTreePrinter;
import hu.webarticum.treeprinter.text.AnsiFormat;
import hu.webarticum.treeprinter.text.ConsoleText;

public class AnsiExamplesMain {

    public static void main(String[] args) {
        SimpleTreeNode rootNode = new SimpleTreeNode("Root");
        
        SimpleTreeNode childNode1 = new SimpleTreeNode(
                ConsoleText.of("Child 1").format(AnsiFormat.GREEN.compose(AnsiFormat.BOLD)));
        rootNode.addChild(BorderTreeNodeDecorator.builder().format(AnsiFormat.RED).buildFor(childNode1));
        
        SimpleTreeNode childNode2 = new SimpleTreeNode("Child 2");
        rootNode.addChild(childNode2);

        SimpleTreeNode grandChildNode21 = new SimpleTreeNode(
                ConsoleText.of("Grandchild ").concat(ConsoleText.of("2-1").format(AnsiFormat.MAGENTA)));
        childNode2.addChild(grandChildNode21);

        SimpleTreeNode grandGrandChildNode211 = new SimpleTreeNode("Grand-grandchild 2-1-1");
        grandChildNode21.addChild(PadTreeNodeDecorator.builder().pad(1).format(AnsiFormat.BG_YELLOW).buildFor(grandGrandChildNode211));
        
        SimpleTreeNode grandChildNode22 = new SimpleTreeNode(
                ConsoleText.of("Grandchild ")
                        .concat(ConsoleText.of("2-2").format(AnsiFormat.CYAN))
                        .concat(ConsoleText.of("\n"))
                        .concat(ConsoleText.of("Line 2").format(AnsiFormat.RED))
                        .concat(ConsoleText.of("\n"))
                        .concat(ConsoleText.of("Line line 3").format(AnsiFormat.MAGENTA)));
        childNode2.addChild(ShadowTreeNodeDecorator.builder().format(AnsiFormat.BLUE).buildFor(grandChildNode22));
        
        ListingTreePrinter.builder()
                .liningFormat(AnsiFormat.CYAN)
                .build()
                .print(rootNode);
        
        System.out.println();
        System.out.println();
        
        Liner liner = DefaultLiner.builder()
                .topHeight(1)
                .bottomHeight(1)
                .format(AnsiFormat.BLUE)
                .build();
        new TraditionalTreePrinter(liner).print(rootNode);
        
        System.out.println();
        System.out.println();
        
        BoxingTreePrinter.builder()
                .defaultFormat(AnsiFormat.CYAN)
                .levelFormat(1, AnsiFormat.YELLOW)
                .levelFormat(2, AnsiFormat.MAGENTA)
                .build()
                .print(rootNode);
    }
    
}
