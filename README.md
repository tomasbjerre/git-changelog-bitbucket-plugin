# Git Changelog Bitbucket Plugin [![Build Status](https://travis-ci.org/tomasbjerre/git-changelog-bitbucket-plugin.svg?branch=master)](https://travis-ci.org/tomasbjerre/git-changelog-bitbucket-plugin)

Generate changelog or releasenotes in Atlassian Bitbucket Server using [Git Changelog](https://github.com/tomasbjerre/git-changelog-lib).

## Developer instructions
Prerequisites:

* Atlas SDK [(installation instructions)](https://developer.atlassian.com/docs/getting-started/set-up-the-atlassian-plugin-sdk-and-build-a-project).
* JDK 1.8 or newer

Generate Eclipse project:
```
atlas-compile eclipse:eclipse
```

Package the plugin:
```
atlas-package
```

Run Bitbucket, with the plugin, on localhost:
```
export MAVEN_OPTS=-Dplugin.resource.directories=`pwd`/src/main/resources
mvn bitbucket:run
```

You can also debug with:
```
mvn bitbucket:debug
```

Make a release [(detailed instructions)](https://developer.atlassian.com/docs/common-coding-tasks/development-cycle/packaging-and-releasing-your-plugin):
```
mvn release:prepare
mvn release:perform
```
