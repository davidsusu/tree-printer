# TODO list

## Improve code quality

Remove 'get' from simple getters.
Improve builders, rename createBuilder() to builder().

## Change formatting

Use the 8-space standard, etc.

## Improve examples and tests

Move examples to its own place (separate source set? separate project?).

Add some unit tests.

##  Improve TraditionalTreePrinter

Make it parallel.
This can be achieved with fork-join strategy and a concurrent line buffer.

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
