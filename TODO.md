# TODO list

## 2.x migration

- Changed package
- Changed conventions (removed get- and create- prefix)
- etc.

##  Improve TraditionalTreePrinter

Calculate Position taking into account the insets.

## Add DecoratorTreePrinter[?]

Example:

```java
new DecoratorTreePrinter(
    baseTreePrinter,
    t -> new BorderTreeNodeDecorator(
        new PadTreeNodeDecorator(t, new Insets(1, 2))
    )
);
```

## Improve ListingTreePrinter
### (connected lines in aligned mode)

Optionally?

    root
     |--+----sub1-line1
     |  |    sub1-line2
     |  |    sub1-line2
     |  |
     |  '--+-sub1.1
     |     |
     |     |-sub1.1.1
     |     |
     |     '-sub1.1.2
     |
     '-------sub2


## Add CompactBinaryTreePrinter

Can be buffered

        A
       / \
      B   C
     / \   \
    D   E   F
     \     / \
      G   /   \
         /     \
        H       I
       / \     / \
      J   K   L   M
         /     \
        N       O
                 \
                  P
