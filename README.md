# Tree Printer

Simple Java library for visualizing tree structures in the command line.

This legacy version is compatibile with old java versions (1.6+).

```
natural foods
 ├─ fruits
 │  ├─ apple
 │  ├─ banana
 │  ├─ mango
 │  ├─ lorem and
 │  │  impsum
 │  │ ┌──────────────────┐
 │  │ │        ?         │
 │  ├─│    ┌───┴───┐     │
 │  │ │ orange mandarine │
 │  │ └──────────────────┘
 │  │ ┌────────────────────────────────┐
 │  │ │        berry-like              │
 │  │ │     ┌──────┴───────┐           │
 │  └─│   grape          berry         │
 │    │  ┌──┴─┐       ┌────┴─────┐     │
 │    │ red white strawberry raspberry │
 │    └────────────────────────────────┘
 ├─ vegetables
 │  ├─ tomato
 │  ├─ carrot
 │  │ A──────────B
 │  └─│ broccoli │
 │    C──────────D
 └─ seeds
    ├─ walnut
    └─ peanut
```

## Constructing trees

A *tree* is a hierarchical structure built from *nodes*. Any tree is given by its root node.

Nodes implement the `TreeNode` interface, and there is an `AbstractTreeNode` as a bootstrap for a custom implementation.

You can use `SimpleTreeNode` out-of-the-box as a default string-based implementation:

```java
SimpleTreeNode rootNode = new SimpleTreeNode("I'm the root!");
rootNode.addChild(new SimpleTreeNode("I'm a child..."));
rootNode.addChild(new SimpleTreeNode("I'm an other child..."));
```

However, you are free to implement your custom nodes, see the `fs` package for an example.

## Print a tree

There are multiple built-in implementations of the `TreePrinter` interface for printing tree structures via the `print()` method. This method accepts a `TreeNode` (the root node of the printed hierarchy), and, optionally, an `Appendable` object, to where the output will be flushed.

Alternatively, you can get the visualization as `String` via `getAsString()`, but in some cases this is inefficient (especially when you print large data with `ListingTreePrinter`).

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

Or use a `TraditinalTreePrinter`:

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

Of course, `TreePrinter` implementations have many options for controlling the output. You can change the lining characters, the aligning, and so on.

Most classes have multiple constructors and `Builder` subclasses for easy change of settings. For example, if we want to align everything to left:

```java
new TraditionalTreePrinter(
    TraditionalTreePrinter.DefaultAligner.createBuilder()
        .align(TraditionalTreePrinter.DefaultAligner.LEFT)
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

You can easily write node decorators by extending `AbstractTreeNodeDecorator`. There are built-in implementations for creating paddings and borders.

In the previous example the child nodes are confused, because only a single space separates them. It will be much cleaner if we added a border:

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

Decorators inherit by default, but you can change this behavior.

## ASCII vs Unicode mode

Built-in objects that prints lines or borders have a built-in set of characters both for ASCII and Unicode mode. Unicode mode is the default, by default. Affected classes have a constructor (and builder) parameter `useUnicode`. You can globally turn off (and on) the unicode mode with `UnicodeMode.setUnicodeAsDefault()`.

The first example with ASCII rendering:

```java
ListingTreePrinter.createBuilder().ascii().build().print(rootNode);
```

Result:

```
I'm the root!
 |-I'm a child...
 '-I'm an other child...
 ```
