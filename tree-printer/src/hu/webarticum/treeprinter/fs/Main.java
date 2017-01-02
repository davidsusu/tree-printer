package hu.webarticum.treeprinter.fs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import hu.webarticum.treeprinter.SimpleTreePrinter;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.print("Root directory: ");
        String path = args.length > 0 ? args[0] : new BufferedReader(new InputStreamReader(System.in)).readLine();
        new SimpleTreePrinter().print(new DefaultFsTreeNodeDecorator(new FsTreeNode(new File(path))));
    }

}
