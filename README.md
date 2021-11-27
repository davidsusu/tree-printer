# Tree Printer

> :warning: &nbsp; **`v2.0.0` is coming soon.**
> Ideas, issues, pull-requests are welcome.
> [TODOs](TODO.md)

Simple Java library for visualizing tree structures in the command line.

```
natural foods
 ├─ fruits
 │  ├─ apple
 │  ├─ banana
 │  ├─ mango
 │  ├─ lorem and
 │  │  ipsum
 │  │ ┌──────────────────────────────────┐
 │  │ │            citroideae            │
 │  ├─│    ┌─────┬─────┴─┬─────────┐     │
 │  │ │ orange lemon grapefruit mandarin │
 │  │ └──────────────────────────────────┘
 │  │ ┌────────────────────────────────┐
 │  │ │         berries                │
 │  │ │     ┌──────┴───────┐           │
 │  └─│   grape          other         │
 │    │  ┌──┴─┐       ┌────┴─────┐     │
 │    │ red white strawberry raspberry │
 │    └────────────────────────────────┘
 ├─ vegetables
 │  ├─ tomato
 │  ├─ carrot
 │  │ A──────────B
 │  └─│ broccoli │
 │    D──────────C
 └─ seeds
    ├─ walnut
    └─ peanut
```

## Migration from 1.x to 2.x

The following breaking changes was made:

- Package/class structure was reorganized
- Method naming convention was changed (e. g. `getChildren()` &rarr; `children()`)
- Most of the telescoping constructors was removed (use builders instead)

Also, the new major version contains many little changes, extensions and fixes.

## Using in projects

This library is open source, and available under the Apache License V2.

The library is compatible with java versions 1.8+.
See the `1.x` branch for using the legacy version (compatible with 1.6+).

Built packages are available from the Maven Central Repository.

In gradle projects:

```groovy

dependencies {
    implementation "hu.webarticum:tree-printer:${treePrinterVersion}"
}

```

In maven projects:

```xml
<dependency>
  <groupId>hu.webarticum</groupId>
  <artifactId>tree-printer</artifactId>
  <version>${treePrinterVersion}</version>
</dependency>
```

## Constructing trees

A *tree* is a hierarchical structure built from *nodes*.
Any tree is given by its root node.

Nodes implement the `TreeNode` interface.
It contains default implementations for the majority of its methods,
you must implement only the `content()` and `children()` methods.

You can use `SimpleTreeNode` out-of-the-box as a default string-based implementation:

```java
SimpleTreeNode rootNode = new SimpleTreeNode("I'm the root!");
rootNode.addChild(new SimpleTreeNode("I'm a child..."));
rootNode.addChild(new SimpleTreeNode("I'm an other child..."));
```

However, you are free to implement your custom nodes.

## Printing a tree

There are multiple built-in implementations of the `TreePrinter` interface
for printing tree structures via the `print()` method.
This method accepts a `TreeNode` (the root node of the printed hierarchy),
and, optionally, an `Appendable` object, to where the output will be flushed.

Alternatively, you can get the visualization as `String` via `stringify()`.
In some cases this is inefficient (especially when you print large data with `ListingTreePrinter`).

It is very easy to visualize the above structure:

```java
new ListingTreePrinter().print(rootNode);
```

And the result:

```
I'm the root!
 ├─I'm a child...
 └─I'm an other child...
```

Or use a `TraditionalTreePrinter`:

```java
new TraditionalTreePrinter().print(rootNode);
```

Which results:

```
           I'm the root!
       ┌─────────┴───────┐
       │                 │
I'm a child... I'm an other child...
```

For more available printers see the `hu.webarticum.treeprinter.printer.*` packages.

Of course, `TreePrinter` implementations have many options for controlling the output.
You can change the lining characters, the aligning, and so on.

Most classes have builders and some basic constructors for easy change of settings.
For example, if we want to align everything to left:

```java
new TraditionalTreePrinter(
        DefaultAligner.builder()
                .align(DefaultAligner.Alignment.LEFT)
        .build(),
        TraditionalTreePrinter.DEFAULT_LINER
).print(rootNode);
```

Result:

```
I'm the root!
├──────────────┐
│              │
I'm a child... I'm an other child...
```

## Using decorators

You can easily write node decorators by extending `AbstractTreeNodeDecorator`.
There are built-in implementations for adding
padding, border, shadow and other basic decorations.

In the previous example the child nodes are confused, because only a single space separates them.
It will be much cleaner if we added a border:

```java
new TraditionalTreePrinter().print(new BorderTreeNodeDecorator(rootNode));
```

Result:

```
            ┌─────────────┐
            │I'm the root!│
            └─────────────┘
        ┌──────────┴────────┐
        │                   │
┌──────────────┐ ┌─────────────────────┐
│I'm a child...│ │I'm an other child...│
└──────────────┘ └─────────────────────┘
```

Of course, you can compose as many decorators as you want:

```java
TreeNode decoratedTreeNode = new ShadowTreeNodeDecorator(
        BorderTreeNodeDecorator.builder()
                .wideUnicode()
                .buildFor(
                        new PadTreeNodeDecorator(rootNode, new Insets(0, 2)))
        );

new TraditionalTreePrinter().print(decoratedTreeNode);
```

Result:

```
               ▛▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▜
               ▏  I'm the root!  ▕▒
               ▙▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▟▒
                ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
          ┌──────────────┴──────────┐
          │                         │
▛▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▜  ▛▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▜
▏  I'm a child...  ▕▒ ▏  I'm an other child...  ▕▒
▙▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▟▒ ▙▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▟▒
 ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
```

Decorators inherit by default, but you can change this behavior.

## ASCII vs Unicode mode

Built-in objects that print lines or borders have a built-in set of characters, both for ASCII and Unicode mode.
Affected classes have a constructor/builder parameter `useUnicode`.
You can globally change the default mode with `UnicodeMode.setUnicodeAsDefault()`.
(Initial global default is Unicode.)

The first example with ASCII rendering:

```java
ListingTreePrinter.builder().ascii().build().print(rootNode);
```

Result:

```
I'm the root!
 |-I'm a child...
 '-I'm an other child...
 ```

## Placeholders

You can put placeholder nodes into the tree.
These nodes are hidden by default in the general printers.
Placeholders are useful in n-ary trees, where missing nodes matter.

```java
boolean displayPlaceholders = true;

SimpleTreeNode alignedTree = new SimpleTreeNode("ROOT");
alignedTree.addChild(new PlaceholderNode());
alignedTree.addChild(new SimpleTreeNode("RIGHT"));

new TraditionalTreePrinter(displayPlaceholders).print(
        PadTreeNodeDecorator.builder()
                .forceInherit(true)
                .horizontalPad(3)
                .buildFor(new BorderTreeNodeDecorator(alignedTree))
        );
```

```
       ┌────┐
       │ROOT│
       └────┘
   ┌──────┴──┐
   │         │
          ┌─────┐
          │RIGHT│
          └─────┘
```

Any node whose `isPlaceholder()` method returns `true` is considered a placeholder.
`hu.webarticum.treeprinter.PlaceholderNode` is a built-in placeholder,
it's empty and undecorable too.
