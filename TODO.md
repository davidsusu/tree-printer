# TODO list

## For v2.0.0

### BackgroundTreeNodeDecorator (?)

From this:

```
Line 1 asdf
Line 2
Line 3 x
```

make this:

```
Line 1 asdf
Line 2~~~~~
Line 3 x~~~
```

### FsTreeNode

Add option for hidden files/directories

## For v2.1.0

### Add CompactBinaryTreePrinter

Something like this (H?!):

```
    A
   / \
  B   C
 / \   \
D   E   F
 \     / \
  G   /   I
     /   / \
    H   L   M
   / \   \
  J   K   O
     /     \
    N       P
```

## For v3.0.0

### Create an enhanced version of TraditionalTreePrinter

- Inherit major features of the previous TraditionalTreePrinter
- Make it recursively composed
- Fit the right side of the left and left side of the right subtree
- Calculate Position taking into account the insets.
- Use middle of the connection line instead of full contents.
