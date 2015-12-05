package se.bjurr.changelog.bitbucket;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Ordering.usingToString;
import static se.bjurr.changelog.bitbucket.admin.settings.SettingsStorage.STORAGE_KEY;
import static se.bjurr.changelog.bitbucket.admin.settings.SettingsStorage.fromJson;
import static se.bjurr.changelog.bitbucket.admin.settings.SettingsStorage.getValidatedSettings;
import static se.bjurr.gitchangelog.api.GitChangelogApi.gitChangelogApiBuilder;
import static se.bjurr.gitchangelog.api.GitChangelogApiConstants.DEFAULT_JIRA_ISSUE_PATTEN;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import se.bjurr.changelog.bitbucket.admin.settings.ChangelogSettings;
import se.bjurr.changelog.bitbucket.admin.settings.ValidationException;
import se.bjurr.changelog.bitbucket.rest.dto.ChangelogDTO;
import se.bjurr.gitchangelog.api.GitChangelogApi;

import com.atlassian.applinks.api.ApplicationLinkService;
import com.atlassian.applinks.api.application.bitbucket.BitbucketApplicationType;
import com.atlassian.applinks.api.application.jira.JiraApplicationType;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.bitbucket.server.ApplicationPropertiesService;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public final class RestUtils {
 public RestUtils() {
 }

 static GitChangelogApi getGitChangelogApiBuilder(PluginSettingsFactory pluginSettingsFactory,
   ApplicationLinkService applicationLinkService, ApplicationPropertiesService applicationPropertiesService,
   Repository repo) throws IOException, ValidationException {
  Builder<String, Object> builder = ImmutableMap.<String, Object> builder() //
    .put("repositoryName", repo.getName()) //
    .put("repositorySlug", repo.getSlug()) //
    .put("projectName", repo.getProject().getName()) //
    .put("projectKey", repo.getProject().getKey());
  if (applicationLinkService.getPrimaryApplicationLink(JiraApplicationType.class) != null) {
   builder //
     .put("jiraUrl", applicationLinkService.getPrimaryApplicationLink(JiraApplicationType.class));
  }
  if (applicationLinkService.getPrimaryApplicationLink(BitbucketApplicationType.class) != null) {
   builder //
     .put("bitbucketUrl", applicationLinkService.getPrimaryApplicationLink(BitbucketApplicationType.class));
  }
  ChangelogSettings settings = getValidatedSettings(fromJson(pluginSettingsFactory.createGlobalSettings().get(
    STORAGE_KEY)));
  File repositoryDir = applicationPropertiesService.getRepositoryDir(repo);
  Map<String, Object> extendedVariables = builder.build();
  return gitChangelogApiBuilder() //
    .withFromRepo(repositoryDir.getAbsolutePath()) //
    .withExtendedVariables(extendedVariables) //
    .withDateFormat(settings.getDateFormat()) //
    .withJiraIssuePattern(DEFAULT_JIRA_ISSUE_PATTEN) //
    .withIgnoreCommitsWithMesssage(settings.getIgnoreCommitsIfMessageMatches()) //
    .withNoIssueName(settings.getNoIssueName()) //
    .withTemplateContent(settings.getTemplate()) //
    .withTimeZone(settings.getTimeZone()) //
    .withUntaggedName(settings.getUntaggedName());
 }

 static ChangelogDTO getChangelog(ApplicationPropertiesService applicationPropertiesService, final Repository repo,
   GitChangelogApi changelogBuilder) throws IOException {
  File repositoryDir = applicationPropertiesService.getRepositoryDir(repo);

  String changelog = changelogBuilder.render();
  List<String> references = getReferences(repositoryDir);
  ChangelogDTO changelogDto = new ChangelogDTO(changelog, references);

  return changelogDto;
 }

 static List<String> getReferences(File repositoryDir) throws IOException {
  org.eclipse.jgit.lib.Repository rep = new FileRepositoryBuilder()//
    .findGitDir(repositoryDir)//
    .readEnvironment().build();
  try (Git jGit = new Git(rep)) {
   Map<String, Ref> refsMap = jGit.getRepository().getAllRefs();
   return filterRefs(refsMap.keySet());
  }
 }

 @VisibleForTesting
 static List<String> filterRefs(Set<String> allRefs) {
  Iterable<String> refs = filter(allRefs, not(equalTo("HEAD")));
  refs = transform(refs, input -> {
   if (input.startsWith("refs/heads/")) {
    input = input.substring("refs/heads/".length());
   }
   if (input.startsWith("refs/tags/")) {
    input = input.substring("refs/tags/".length());
   }
   return input;
  });
  return usingToString().sortedCopy(newArrayList(refs));
 }

 static Repository getRepository(RepositoryService repositoryService, String project, String repository) {
  final Repository repo = checkNotNull(repositoryService.getBySlug(project, repository), //
    "Did not find " + project + " " + repository);
  return repo;
 }
}
