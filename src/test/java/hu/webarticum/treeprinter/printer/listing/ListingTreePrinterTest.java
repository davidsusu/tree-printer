package hu.webarticum.treeprinter.printer.listing;

import hu.webarticum.treeprinter.SimpleTreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ListingTreePrinterTest {

    private ListingTreePrinter treePrinter;

    @BeforeEach
    void before() {
        treePrinter = new ListingTreePrinter();
    }

    @Test
    void print_oneLevel() {
        SimpleTreeNode root = new SimpleTreeNode("ROOT");
        StringBuilder out = new StringBuilder();
        treePrinter.print(root, out);
        assertThat(out.toString()).isEqualTo(
                "ROOT\n");
    }

    @Test
    void collectLineEntries_oneLevel() {
        SimpleTreeNode root = new SimpleTreeNode("ROOT");
        List<ListingLineEntry> treeNodes = treePrinter.collectLineEntries(root);
        assertThat(treeNodes.size()).isEqualTo(1);
        assertListingLineEntry(treeNodes.get(0), root, "", "ROOT");
    }

    @Test
    void print_twoLevels() {
        SimpleTreeNode root = new SimpleTreeNode("ROOT");
        SimpleTreeNode child1 = new SimpleTreeNode("CHILD1");
        SimpleTreeNode child2 = new SimpleTreeNode("CHILD2");
        root.addChild(child1);
        root.addChild(child2);
        StringBuilder out = new StringBuilder();
        treePrinter.print(root, out);
        assertThat(out.toString()).isEqualTo(
                "ROOT\n"
                        + " ├─CHILD1\n"
                        + " └─CHILD2\n");
    }


    @Test
    void collectLineEntries_twoLevels() {
        SimpleTreeNode root = new SimpleTreeNode("ROOT");
        SimpleTreeNode child1 = new SimpleTreeNode("CHILD1");
        SimpleTreeNode child2 = new SimpleTreeNode("CHILD2");
        root.addChild(child1);
        root.addChild(child2);
        List<ListingLineEntry> treeNodes = treePrinter.collectLineEntries(root);
        assertThat(treeNodes.size()).isEqualTo(3);
        assertListingLineEntry(treeNodes.get(0), root, "", "ROOT");
        assertListingLineEntry(treeNodes.get(1), child1, " ├─", "CHILD1");
        assertListingLineEntry(treeNodes.get(2), child2, " └─", "CHILD2");
    }

    @Test
    void print_fourLevels() {
        SimpleTreeNode root = new SimpleTreeNode("ROOT");
        SimpleTreeNode n1 = new SimpleTreeNode("N1");
        SimpleTreeNode n1_1 = new SimpleTreeNode("N1.1");
        n1.addChild(n1_1);
        SimpleTreeNode n1_2 = new SimpleTreeNode("N1.2");
        SimpleTreeNode n1_2_1 = new SimpleTreeNode("N1.2.1");
        n1_2.addChild(n1_2_1);
        SimpleTreeNode n1_2_2 = new SimpleTreeNode("N1.2.2");
        n1_2.addChild(n1_2_2);
        n1.addChild(n1_2);
        SimpleTreeNode n1_3 = new SimpleTreeNode("N1.3");
        n1.addChild(n1_3);
        SimpleTreeNode n1_4 = new SimpleTreeNode("N1.4");
        n1.addChild(n1_4);
        root.addChild(n1);
        SimpleTreeNode n2 = new SimpleTreeNode("N2");
        root.addChild(n2);
        SimpleTreeNode n3 = new SimpleTreeNode("N3");
        root.addChild(n3);

        StringBuilder out = new StringBuilder();
        treePrinter.print(root, out);
        assertThat(out.toString()).isEqualTo(
                "ROOT\n"
                        + " ├─N1\n"
                        + " │  ├─N1.1\n"
                        + " │  ├─N1.2\n"
                        + " │  │  ├─N1.2.1\n"
                        + " │  │  └─N1.2.2\n"
                        + " │  ├─N1.3\n"
                        + " │  └─N1.4\n"
                        + " ├─N2\n"
                        + " └─N3\n");
    }

    @Test
    void collectLineEntries_fourLevels() {
        SimpleTreeNode root = new SimpleTreeNode("ROOT");
        SimpleTreeNode n1 = new SimpleTreeNode("N1");
        SimpleTreeNode n1_1 = new SimpleTreeNode("N1.1");
        n1.addChild(n1_1);
        SimpleTreeNode n1_2 = new SimpleTreeNode("N1.2");
        SimpleTreeNode n1_2_1 = new SimpleTreeNode("N1.2.1");
        n1_2.addChild(n1_2_1);
        SimpleTreeNode n1_2_2 = new SimpleTreeNode("N1.2.2");
        n1_2.addChild(n1_2_2);
        n1.addChild(n1_2);
        SimpleTreeNode n1_3 = new SimpleTreeNode("N1.3");
        n1.addChild(n1_3);
        SimpleTreeNode n1_4 = new SimpleTreeNode("N1.4");
        n1.addChild(n1_4);
        root.addChild(n1);
        SimpleTreeNode n2 = new SimpleTreeNode("N2");
        root.addChild(n2);
        SimpleTreeNode n3 = new SimpleTreeNode("N3");
        root.addChild(n3);

        List<ListingLineEntry> treeNodes = treePrinter.collectLineEntries(root);
        assertThat(treeNodes.size()).isEqualTo(10);
        assertListingLineEntry(treeNodes.get(0), root, "", "ROOT");
        assertListingLineEntry(treeNodes.get(1), n1, " ├─", "N1");
        assertListingLineEntry(treeNodes.get(2), n1_1, " │  ├─", "N1.1");
        assertListingLineEntry(treeNodes.get(3), n1_2, " │  ├─", "N1.2");
        assertListingLineEntry(treeNodes.get(4), n1_2_1, " │  │  ├─", "N1.2.1");
        assertListingLineEntry(treeNodes.get(5), n1_2_2, " │  │  └─", "N1.2.2");
        assertListingLineEntry(treeNodes.get(6), n1_3, " │  ├─", "N1.3");
        assertListingLineEntry(treeNodes.get(7), n1_4, " │  └─", "N1.4");
        assertListingLineEntry(treeNodes.get(8), n2, " ├─", "N2");
        assertListingLineEntry(treeNodes.get(9), n3, " └─", "N3");
    }

    @Test
    void print_twoLevelsWithLineBreaks() {
        SimpleTreeNode root = new SimpleTreeNode("ROOTa\nROOTb");
        SimpleTreeNode child1 = new SimpleTreeNode("CHILD1a\nCHILD1b");
        SimpleTreeNode child2 = new SimpleTreeNode("CHILD2");
        root.addChild(child1);
        root.addChild(child2);
        StringBuilder out = new StringBuilder();
        treePrinter.print(root, out);
        assertThat(out.toString()).isEqualTo(
                "ROOTa\n"
                        + "ROOTb\n"
                        + " ├─CHILD1a\n"
                        + " │ CHILD1b\n"
                        + " └─CHILD2\n");
    }

    @Test
    void collectLineEntries_twoLevelsWithLineBreaks() {
        SimpleTreeNode root = new SimpleTreeNode("ROOTa\nROOTb");
        SimpleTreeNode child1 = new SimpleTreeNode("CHILD1a\nCHILD1b");
        SimpleTreeNode child2 = new SimpleTreeNode("CHILD2");
        root.addChild(child1);
        root.addChild(child2);
        List<ListingLineEntry> treeNodes = treePrinter.collectLineEntries(root);
        assertThat(treeNodes.size()).isEqualTo(5);
        assertListingLineEntry(treeNodes.get(0), root, "", "ROOTa");
        assertListingLineEntry(treeNodes.get(1), root, "", "ROOTb");
        assertListingLineEntry(treeNodes.get(2), child1, " ├─", "CHILD1a");
        assertListingLineEntry(treeNodes.get(3), child1, " │ ", "CHILD1b");
        assertListingLineEntry(treeNodes.get(4), child2, " └─", "CHILD2");
    }

    private void assertListingLineEntry(ListingLineEntry actual, SimpleTreeNode expectedNode, String expectedPrefix,
            String expectedLine) {
        assertThat(actual.node()).isEqualTo(expectedNode);
        assertThat(actual.liningPrefix().plain()).isEqualTo(expectedPrefix);
        assertThat(actual.contentLine().plain()).isEqualTo(expectedLine);
    }

}