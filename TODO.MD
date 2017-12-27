# TODO list


## Write a useful README


## Use builders instead of intricate constructors

    ListingTreePrinter.createBuilder().useUnicode(true).topConnectionChar('¤').topHeight(3).build();


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


## TraditionalTreePrinter: bundled alterantive liners

            A
          //|\\
         // | \\
        //  |  \\
       //   |   \\
      //    |   | \
     / |    |   | |
    /  |    |   | |
    B  C    D   E F
       |   / \
      xxx yy zz

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
