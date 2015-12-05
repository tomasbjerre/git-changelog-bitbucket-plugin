# Git Changelog Bitbucket Plugin [![Build Status](https://travis-ci.org/tomasbjerre/git-changelog-bitbucket-plugin.svg?branch=master)](https://travis-ci.org/tomasbjerre/git-changelog-bitbucket-plugin)

Generates a changelog, or releasenotes, in Atlassian Bitbucket Server using [Git Changelog Lib](https://github.com/tomasbjerre/git-changelog-lib).

## Changelog page
The plugin adds a page on repository level where the user can select *from* and *to* branch to generate a changelog.

There are some screenshots [here](https://github.com/tomasbjerre/git-changelog-lib/tree/screenshots/sandbox).

## REST API
The plugin exposes the changelog as a REST API, available at */bitbucket/rest/changelog/1.0/*

```
/{project}/{repository} - First commit to master
/{project}/{repository}/fromref/{fromRef}/toref/{toRef}
/{project}/{repository}/fromref/{fromRef}/tocommit/{toCommit}
/{project}/{repository}/fromcommit/{fromCommit}/toref/{toRef}
/{project}/{repository}/fromcommit/{fromCommit}/tocommit/{toCommit}
```

## Variables
The changelog is available in the context of the template. These variables are documented in [Git Changelog Lib](https://github.com/tomasbjerre/git-changelog-lib).

There are also some extended variables available in this Bitbucket plugin.

 * *repositoryName* Name of repository
 * *repositorySlug* Name of repository used in URL:s of Bitbucket
 * *projectName* Name of project
 * *projectKey* Name of project used in URL:s of Bitbucket
 * *jiraUrl* URL pointing to Jira, if you have one configured in Bitbucket
 * *bitbucketUrl* URL pointing at your Bitbucket server

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

 * `mvn release:prepare release:perform`
 * Browse to GitHub page
  * Upload artifact
  * Write releasenotes
 * Go to Marketplace
  * Create a release
