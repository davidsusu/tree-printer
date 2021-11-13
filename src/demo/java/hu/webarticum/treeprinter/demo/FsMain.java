package hu.webarticum.treeprinter.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import hu.webarticum.treeprinter.misc.fs.DefaultFsTreeNodeDecorator;
import hu.webarticum.treeprinter.misc.fs.FsTreeNode;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;

public class FsMain {

    public static void main(String[] args) throws IOException {
        System.out.print("Root directory: ");
        String path = new BufferedReader(new InputStreamReader(System.in)).readLine();
        new ListingTreePrinter().print(new DefaultFsTreeNodeDecorator(new FsTreeNode(new File(path))));
    }

}
