# Alexandria

A standard library of commonly used Java code.

The alexandria project is designed to provide a general-purpose library of Java code for a wide range of applications.
Things specific to Java but which should be shared widely among projects belong in this library.

# Developers

## Building Annotations

The [`ax-annotations`](ax-annotations) project provides some general purpose annotations for code documentation, along with an annotation process for reporting one them in compiler logs.
Because of the complex dependencies inherent in build annotation processors `ax-annotations` requires some care in building if you're a developer.
Below are the endorsed, though certainly not the only, steps to build alexandria in Eclipse.

1. Obtain the source code either by download or `git clone`.
2. Import [`ax-root/pom.xml`](ax-root/pom.xml) and [`ax-annotations/pom.xml`](ax-annotations/pom.xml) into Eclipse.
3. Run a maven install (`mvn install`) using the Eclipse UI for the `ax-root` and then `ax-annotations` projects.
4. Remove `ax-annotations` from your Eclipse workspace.
5. Import the room [`alexandria/pom.xml`](pom.xml) into Eclipse, which will bring in all other subprojects except `ax-annotations`.

## Release

1. Use maven to release the project and update versions
	1. `mvn release:prepare -Prelease`
	2. `mvn release:perform -Prelease`
	3. Release or drop [the staging repository](https://oss.sonatype.org/#stagingRepositories)
2. Publish changes to GitHub
	1. Open a [pull request](https://github.com/g2forge/alexandria/pulls) with the results of step #1.2, marking it with the milestone you've just released.
	2. Push the tag generated by step #1.2 to github
	3. Create the new [github milestone](https://github.com/g2forge/alexandria/milestones) and close the prior one
4. Local & downstream updates
	1. [Install the new SNAPSHOT version of annotations](#building-annotations) `cd .../ax-root && mvn install` then `cd .../ax-annotations && mvn install` 
	2. Update downstream projects (`<alexandria.version>XXX</alexandria.version>`, parent POM versions, etc)
	3. Update issue tracking. For Jira note that you will want to bulk-add the new fix version, and then bulk-remove the old version rather than replace since other versions may be involved.