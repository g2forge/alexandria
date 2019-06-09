# Alexandria Memory FileSystem

This module contains an implementation of an in-memory NIO filesystem.
This has two uses:

1. Because this module (and only this module) is released under the [unlicense](LICENSE), you may copy it to implement your own file system. We suggest you [read our guide](../ax-filesystem#creating-a-filesystemprovider).
2. You may use this file system as shown in the [runnable examples](src/test/java/com/g2forge/alexandria/filesystem/memory/TestExample.java). It is particularly helpful for unit testing code which invokes file operations.
