# Alexandria

The alexandria project is designed to provide a general-purpose library of Java code for a wide range of applications.
Things specific to Java but which should be shared widely among projects belong in this library.

# Developers

## Annotations & Build

The [`ax-annotations`](ax-annotations) project provides some general purpose annotations for code documentation, along with an annotation process for reporting one them in compiler logs.
Because of the complex dependencies inherent in build annotation processors `ax-annotations` requires some care in building.
Below are the endorsed, though certainly not the only, steps to build alexandria in Eclipse.

1. Obtain the source code either by download or `git clone`.
2. Import [`ax-annotations` `pom.xml`](ax-alexandria/pom.xml) into Eclipse.
3. Run a maven install (`mvn install`) using the Eclipse UI for the `ax-annotations` project.
4. Remove `ax-annotations` from your Eclipse workspace.
5. Import the room [`alexandria` `pom.xml`](pom.xml) into Eclipse, which will bring in all other subprojects except `ax-annotations`.

## Release

1. `mvn release:prepare -Prelease`
2. `mvn release:perform -Prelease`
3. Release or drop [the staging repository](https://oss.sonatype.org/#stagingRepositories)
4. Open a [pull request](https://github.com/g2forge/alexandria/pulls) with the results of Step #2, tagging it with the milestone you've just released.
5. Create the new [github milestone](https://github.com/g2forge/alexandria/milestones) and close the prior one
