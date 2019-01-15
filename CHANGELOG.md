# Changelog

Changelog of Git Changelog Bitbucket plugin.

## 1.20
### GitHub [#25](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/issues/25) JiraClientService uses DisplayUrl instead of RpcUrl
  fixes #25 JiraClientService uses DisplayUrl instead of RpcUrl
  
  [75c016e5c72c66f](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/75c016e5c72c66f) Aerni Mario, IT122 *2019-01-15 14:42:02*

### No issue
  Update lib to 1.88
  
  [aac4080681ab88b](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/aac4080681ab88b) Tomas Bjerre *2019-01-15 16:59:07*

  Building for 5.0
  
  [07eebd11b5824c9](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/07eebd11b5824c9) Tomas Bjerre *2017-05-14 07:01:04*

## 1.19
### No issue
  tag time added to tag model
  
  [4c963217c9e4e39](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/4c963217c9e4e39) Tomas Bjerre *2017-04-14 09:12:27*

## 1.18
### No issue
  Fix Jira labels
  
  [646c4f1018f9507](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/646c4f1018f9507) Tomas Bjerre *2017-03-20 18:19:28*

## 1.17
### No issue
  Git Changelog Lib 1.58 -> 1.64

 * Jira issueType and labels
 * GitHub labels
 * 10 seconds timeout to GitHub
  
  [58fddffa3b84725](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/58fddffa3b84725) Tomas Bjerre *2017-03-18 09:23:24*

## 1.16
### No issue
  Adding annotation to context of tag
  
  [c22e11970211d4c](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/c22e11970211d4c) Tomas Bjerre *2016-10-22 09:51:59*

## 1.15
### No issue
  Adding merge boolean to commits
  
  [2d9cdec045b9fa5](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/2d9cdec045b9fa5) Tomas Bjerre *2016-10-05 18:36:39*

## 1.14
### No issue
  Getting Jira issue name with admin priv
  
  [aca6b82b518b786](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/aca6b82b518b786) Tomas Bjerre *2016-08-19 18:52:23*

## 1.13
### No issue
  Lib 1.56 correcting link to Jira
  
  [0c66b38fc31cd7c](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/0c66b38fc31cd7c) Tomas Bjerre *2016-08-11 14:51:40*

  Fixed commit link in changelog_html.mustache

Generated commit link was wrong due to wrong basepath
Generated commit link was wrong due to usage of shortened commitID instead of full commitID
  
  [5aecedb4a3da302](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/5aecedb4a3da302) hoppfrosch *2016-08-11 05:09:49*

  doc
  
  [5e38331e92f1e0a](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/5e38331e92f1e0a) Tomas Bjerre *2016-08-10 14:08:52*

## 1.12
### GitHub [#21](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/issues/21) Template: {{hash}} vs. {{hashFull}}
  Using lib 1.55, adding {{hashFull}} #21
  
  [86f901bbcf40937](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/86f901bbcf40937) Tomas Bjerre *2016-08-10 14:07:39*

  Using lib 1.55, adding {{hashFull}} #21
  
  [8482c11fe3262f0](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/8482c11fe3262f0) Tomas Bjerre *2016-08-10 14:07:07*

## 1.11
### GitHub [#20](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/issues/20) Errors installing in 4.8.1
  Avoiding crash on startup #20
  
  [825a1a0f68ebde3](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/825a1a0f68ebde3) Tomas Bjerre *2016-07-30 09:12:17*

## 1.10
### No issue
  Lib 1.53
  
  [bae2642ddec0771](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/bae2642ddec0771) Tomas Bjerre *2016-06-26 18:36:52*

## 1.9
### No issue
  Ignoring trailing slash in JIRA URL
  
  [18f022c69c17707](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/18f022c69c17707) Tomas Bjerre *2016-05-20 19:34:08*

## 1.8
### No issue
  Using lib 1.45
  
  [f288eae0f98fd11](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/f288eae0f98fd11) Tomas Bjerre *2016-04-14 16:48:40*

  Updating debug info in README.md
  
  [657267ba42bd2df](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/657267ba42bd2df) Tomas Bjerre *2016-02-24 18:17:28*

## 1.7
### No issue
  Supplying commit in each issue mentioned in message
  
  [4d8795c65c72f21](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/4d8795c65c72f21) Tomas Bjerre *2016-02-20 08:36:21*

## 1.6
### GitHub [#12](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/issues/12) Optionally lookup jira titles
  Using Bitbuckets Jira authenticated HTTP Client #12 #14 #15

 * Was not authenticating.
 * Also adding checkbox to optionally fetch titles. This solves #14 where the plugin most likely got stuck on waiting for Jira.
  
  [7daad7759006fc5](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/7daad7759006fc5) Tomas Bjerre *2016-02-19 21:24:34*

### GitHub [#14](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/issues/14) Initial changelog
  Using Bitbuckets Jira authenticated HTTP Client #12 #14 #15

 * Was not authenticating.
 * Also adding checkbox to optionally fetch titles. This solves #14 where the plugin most likely got stuck on waiting for Jira.
  
  [7daad7759006fc5](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/7daad7759006fc5) Tomas Bjerre *2016-02-19 21:24:34*

### GitHub [#15](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/issues/15) Bitbucket -&gt; Jira: Http Error 401
  Using Bitbuckets Jira authenticated HTTP Client #12 #14 #15

 * Was not authenticating.
 * Also adding checkbox to optionally fetch titles. This solves #14 where the plugin most likely got stuck on waiting for Jira.
  
  [7daad7759006fc5](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/7daad7759006fc5) Tomas Bjerre *2016-02-19 21:24:34*

### No issue
  Updating CHANGELOG.md
  
  [9409b8336507de7](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/9409b8336507de7) Tomas Bjerre *2016-02-15 19:44:59*

## 1.5
### GitHub [#13](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/issues/13) Error on creating changelog
  Lib 1.30, supporting multiple tags per commit #13

 * Also encoding slashes as \_slash\_ in REST-api.
  
  [528e021138be70f](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/528e021138be70f) Tomas Bjerre *2016-02-15 19:39:54*

### No issue
  Updating CHANGELOG.md
  
  [8681b7f916942f2](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/8681b7f916942f2) Tomas Bjerre *2016-02-14 19:19:01*

## 1.4
### GitHub [#1](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/issues/1) Support Jira integration
  Linking Jira:s #1

 * Also Correcting reference listing on changelog page.
  
  [955f59691e177a6](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/955f59691e177a6) Tomas Bjerre *2016-02-14 19:16:24*

## 1.3
### GitHub [#6](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/issues/6) Hangs for large repos
  Updating changelog only when user selects from, and to, branches #6
  
  [96335908f986c65](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/96335908f986c65) Tomas Bjerre *2016-02-14 06:32:06*

### No issue
  Adding button to open changelog in new window

 * And other GUI improvements.
  
  [4824da64acfcece](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/4824da64acfcece) Tomas Bjerre *2016-02-14 08:30:46*

  Updating CHANGELOG.md
  
  [f10e97e44d4476c](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/f10e97e44d4476c) Tomas Bjerre *2016-02-13 17:23:25*

## 1.2
### GitHub [#6](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/issues/6) Hangs for large repos
  Significant performance improvements #6

 * Using lib 1.27.
  
  [d182238e306a860](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/d182238e306a860) Tomas Bjerre *2016-02-13 09:27:02*

### No issue
  Git changelog lib 1.25
  
  [b0dcca6b807048d](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/b0dcca6b807048d) Tomas Bjerre *2016-02-10 18:46:58*

## 1.1
### GitHub [#4](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/issues/4) Refactor and add test cases
  Adding some test cases #4
  
  [61038babd877d50](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/61038babd877d50) Tomas Bjerre *2015-12-07 20:30:44*

  Creating services and moving stuff around #4
  
  [108f10560ac9028](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/108f10560ac9028) Tomas Bjerre *2015-12-07 19:36:13*

### GitHub [#5](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/issues/5) Wrong date of some commits
  Added variables: messageTitle, messageBody, messageItems #5
  
  [ed6d538323defa7](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/ed6d538323defa7) Tomas Bjerre *2016-02-09 19:46:20*

### No issue
  Using lib 1.23
  
  [5c56b2c9481a7fc](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/5c56b2c9481a7fc) Tomas Bjerre *2016-01-31 11:24:28*

  Correcting link to Marketplace
  
  [f3a7fb575b506c5](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/f3a7fb575b506c5) Tomas Bjerre *2015-12-15 05:37:35*

  Adding link to Atlassian Marketplace in readme
  
  [fdcc554d6532248](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/fdcc554d6532248) Tomas Bjerre *2015-12-06 17:06:54*

  Adding changelog
  
  [20318a3a986f4c5](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/20318a3a986f4c5) Tomas Bjerre *2015-12-06 08:03:26*

## 1.0
### No issue
  Admin GUI
  
  [7e1cbad42b5db5c](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/7e1cbad42b5db5c) Tomas Bjerre *2015-12-05 19:23:50*

  Refactored and added test
  
  [8a6e09c11ee4064](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/8a6e09c11ee4064) Tomas Bjerre *2015-12-05 15:22:01*

  Adding extended variables
  
  [5356c3b3531a221](https://github.com/tomasbjerre/git-changelog-bitbucket-plugin/commit/5356c3b3531a221) Tomas Bjerre *2015-12-05 15:07:45*

