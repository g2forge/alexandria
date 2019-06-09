# Alexandria FileSystem

This module provides a set of generic base classes to make it easier to write your own [FileSystemProvider](https://docs.oracle.com/javase/10/docs/api/java/nio/file/spi/FileSystemProvider.html).
The [ax-fileystem-memory](../ax-fileystem-memory) module is a usable, working example of how to use this module.
While this module is licensed under [Apache 2.0](../LICENSE), [the ax-fileystem-memory license](../ax-fileystem-memory/LICENSE) is less restrictive so that you may copy it.

# Creating a FileSystemProvider

So you want to create a [FileSystemProvider](https://docs.oracle.com/javase/10/docs/api/java/nio/file/spi/FileSystemProvider.html)...
That's one of the main reasons we created [ax-fileystem-memory](../ax-fileystem-memory), and this module, on which that one is built.

You've probably seen the [guide from oracle](https://docs.oracle.com/javase/8/docs/technotes/guides/io/fsp/filesystemprovider.html) or any number of [pieces of example code](https://www.google.com/search?q=create+a+java+filesystemprovider).
The critical thing missing from many of these pages is fully-fledged example code.
The oracle guide refers to the `ZipFileSystemProvider` which you probably wouldn't want to copy, and other examples like [githubfs](https://github.com/gnodet/githubfs) are implementation specific enough that they would be hard to copy.

[ax-fileystem-memory](../ax-fileystem-memory) is simple, and we have tried to create a maximum of re-usable code in a [this module](./).
Here are the rough steps we suggest when you want to create your own filesystem provider:

1. Take a look at the the javadocs for the classes you will need to implement:
  * [FileSystemProvider](https://docs.oracle.com/javase/10/docs/api/java/nio/file/spi/FileSystemProvider.html)
  * [FileSystem](https://docs.oracle.com/javase/10/docs/api/java/nio/file/FileSystem.html)
  * [Path](https://docs.oracle.com/javase/10/docs/api/java/nio/file/Path.html)
2. At least skim the [guide from oracle](https://docs.oracle.com/javase/8/docs/technotes/guides/io/fsp/filesystemprovider.html) so you understand terms.
3. Set up your project
  * Create your maven module
  * Add a dependency on `com.g2forge.alexandria:ax-filesystem` so you can use our generic base classes
  * Add a test scoped dependency on `com.g2forge.alexandria:ax-filesystem-test` so you can use our file system unit tester (test driven development is most fun when someone else writes the tests)
4. Copy the [unit tests](../ax-filesystem-memory/src/test/java/com/g2forge/alexandria/filesystem/memory/TestMemoryFileSystemProvider.java) in your `src/test/java` package
  * Modify the `before()` method to create an instance of your file system
  * Modify the `createPath()` method to create paths associated with the file system created by `before()`
  * Depending on how you decide to format your URIs, you may only need to update `MYFS` to your scheme (replace `memory:...` with `MYFANCYNEWFS:...`).
5. Run JUnit and watch everything fail because you haven't implemented anything. This'll make it more fun later when you implement things.
6. Create your `FileSystemProvider` by extending `AGenericFileSystemProvider`
  * You can copy [MemoryFileSystemProvider](../ax-filesystem-memory/src/main/java/com/g2forge/alexandria/filesystem/memory/MemoryFileSystemProvider.java)
    * At a minimum you'll need to change the `getScheme()` return value to your new scheme. This way you'll have a fully functional memory file system provider, separate from ours.
  * You may find it easier to start from an empty class and fill in implementations, watching your unit test pass rate increase as you go.

`MemoryFileSystemProvider` is implemented using `AGenericFileSystemProvider`.
This means you will need to implement:

* A class for URIs
* All the basic file system operations like `createDirectory()`, `delete()`, etc.
* Some kind of code to represent entries, files and directories in your file system, and `IGenericEntryAccessor` to allow the base classes to interact with this.
* File attributes using `IAttributeViewAccessor`

You will not need to implement:

* `FileSystem`, though you can write a custom one the easiest way will be to use `GenericFileSystem`
* `Path`, though you can write a custom one the easiest way will be to use `GenericPath`
* Code to handle locking and atomicity, or basic file time modifications
