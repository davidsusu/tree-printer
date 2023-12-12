package hu.webarticum.treeprinter.demo;

import hu.webarticum.treeprinter.HorizontalAlign;
import hu.webarticum.treeprinter.SimpleTreeNode;
import hu.webarticum.treeprinter.VerticalAlign;
import hu.webarticum.treeprinter.decorator.AnsiFormatTreeNodeDecorator;
import hu.webarticum.treeprinter.decorator.BorderTreeNodeDecorator;
import hu.webarticum.treeprinter.decorator.JustifyTreeNodeDecorator;
import hu.webarticum.treeprinter.decorator.PadTreeNodeDecorator;
import hu.webarticum.treeprinter.decorator.ShadowTreeNodeDecorator;
import hu.webarticum.treeprinter.printer.boxing.BoxingTreePrinter;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;
import hu.webarticum.treeprinter.printer.traditional.TraditionalTreePrinter;
import hu.webarticum.treeprinter.text.AnsiFormat;
import hu.webarticum.treeprinter.text.ConsoleText;

public class AnsiExamplesMain {

    public static void main(String[] args) {
        SimpleTreeNode rootNode = new SimpleTreeNode(ConsoleText.of("Root").format(
                AnsiFormat.UNDERLINE.compose(AnsiFormat.BOLD).compose(AnsiFormat.RED)));
        
        SimpleTreeNode childNode1 = new SimpleTreeNode(
                ConsoleText.of("Child 1").format(AnsiFormat.GREEN.compose(AnsiFormat.BOLD)));
        rootNode.addChild(new BorderTreeNodeDecorator(childNode1, AnsiFormat.RED));
        
        SimpleTreeNode childNode2 = new SimpleTreeNode("Child 2");
        rootNode.addChild(childNode2);

        SimpleTreeNode grandChildNode21 = new SimpleTreeNode(
                ConsoleText.of("Grandchild ").concat(ConsoleText.of("2-1").format(AnsiFormat.MAGENTA)));
        childNode2.addChild(grandChildNode21);

        SimpleTreeNode grandGrandChildNode211 = new SimpleTreeNode("Grand-grandchild 2-1-1");
        grandChildNode21.addChild(PadTreeNodeDecorator.builder()
                .verticalPad(1)
                .horizontalPad(2)
                .format(AnsiFormat.BG_YELLOW)
                .buildFor(grandGrandChildNode211));
        
        SimpleTreeNode grandChildNode22 = new SimpleTreeNode(
                ConsoleText.of("Grandchild ")
                        .concat(ConsoleText.of("2-2").format(AnsiFormat.CYAN))
                        .breakLine()
                        .concat(ConsoleText.of("Line 2").format(AnsiFormat.RED))
                        .breakLine()
                        .concat(ConsoleText.of("Line line 3").format(AnsiFormat.MAGENTA))
                        .format(AnsiFormat.BOLD));
        childNode2.addChild(new ShadowTreeNodeDecorator(
                JustifyTreeNodeDecorator.builder()
                        .minimumHeight(5)
                        .minimumWidth(20)
                        .verticalAlign(VerticalAlign.MIDDLE)
                        .horizontalAlign(HorizontalAlign.CENTER)
                        .background('~')
                        .backgroundFormat(AnsiFormat.CYAN)
                        .buildFor(grandChildNode22), AnsiFormat.BLUE));
        
        new ListingTreePrinter(AnsiFormat.CYAN).print(new AnsiFormatTreeNodeDecorator(rootNode, AnsiFormat.BOLD));
        
        System.out.println();
        System.out.println();
        
        new TraditionalTreePrinter(AnsiFormat.BLUE).print(rootNode);
        
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
