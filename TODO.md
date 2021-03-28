# TODO list

## Improve code quality

Especially: use enums instead of magic parameters and constants.
Remove 'get' from simple getters.

##  Improve the TreeNode interface

- Remove AbstractTreeNode, use interface default methods instead
- Add getKey() (or just key()) method
- Create SortChildrenByKeyTreeNodeDecorator (or similar)

##  Make TraditionalTreePrinter parallel

This can be achieved with fork-join strategy and a concurrent line buffer.

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


## Add NestedBoxesTreePrinter

Can be buffered

    ,----+----------------------,
    |root|                      |
    +----'                      |
    | ,------, ,------+-------, |
    | |child1| |child2|       | |
    | '------' +------'       | |
    |          | ,----------, | |
    |          | | subchild | | |
    |          | '----------' | |
    |          '--------------' |
    '---------------------------'
