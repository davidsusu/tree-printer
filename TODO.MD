# TODO list


## ListingTreePrinter: connected lines in aligned mode

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


## CompactBinaryTreePrinter

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


## NestedBoxesTreePrinter

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
