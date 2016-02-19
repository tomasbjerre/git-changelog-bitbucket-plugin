package se.bjurr.changelog.bitbucket.application;

import static se.bjurr.changelog.bitbucket.settings.SettingsStorage.STORAGE_KEY;
import static se.bjurr.changelog.bitbucket.settings.SettingsStorage.fromJson;
import static se.bjurr.changelog.bitbucket.settings.SettingsStorage.getValidatedSettings;
import static se.bjurr.gitchangelog.api.GitChangelogApi.gitChangelogApiBuilder;
import static se.bjurr.gitchangelog.api.GitChangelogApiConstants.DEFAULT_JIRA_ISSUE_PATTEN;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import se.bjurr.changelog.bitbucket.settings.ChangelogSettings;
import se.bjurr.changelog.bitbucket.settings.ValidationException;
import se.bjurr.gitchangelog.api.GitChangelogApi;
import se.bjurr.gitchangelog.internal.integrations.jira.JiraClientFactory;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationLinkService;
import com.atlassian.applinks.api.application.bitbucket.BitbucketApplicationType;
import com.atlassian.applinks.api.application.jira.JiraApplicationType;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.server.ApplicationPropertiesService;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class ChangelogLibService {
 private static final String JIRA_URL = "jiraUrl";
 private final PluginSettingsFactory pluginSettingsFactory;
 private final ApplicationLinkService applicationLinkService;
 private final ApplicationPropertiesService applicationPropertiesService;
 private final JiraClientService jiraClientService;

 public ChangelogLibService(PluginSettingsFactory pluginSettingsFactory, ApplicationLinkService applicationLinkService,
   ApplicationPropertiesService applicationPropertiesService, JiraClientService jiraClientService) {
  this.pluginSettingsFactory = pluginSettingsFactory;
  this.applicationLinkService = applicationLinkService;
  this.applicationPropertiesService = applicationPropertiesService;
  this.jiraClientService = jiraClientService;
 }

 public GitChangelogApi getGitChangelogApiBuilder(Repository repo) throws IOException, ValidationException {
  Builder<String, Object> extendedVariablesBuilder = ImmutableMap.<String, Object> builder() //
    .put("repositoryName", repo.getName()) //
    .put("repositorySlug", repo.getSlug()) //
    .put("projectName", repo.getProject().getName()) //
    .put("projectKey", repo.getProject().getKey());
  ApplicationLink jiraApplicationLink = applicationLinkService.getPrimaryApplicationLink(JiraApplicationType.class);
  String jiraUrlString = null;
  if (jiraApplicationLink != null) {
   jiraUrlString = jiraApplicationLink.getDisplayUrl().toString();
   extendedVariablesBuilder //
     .put(JIRA_URL, jiraUrlString);
  }
  if (applicationLinkService.getPrimaryApplicationLink(BitbucketApplicationType.class) != null) {
   extendedVariablesBuilder //
     .put("bitbucketUrl", applicationLinkService.getPrimaryApplicationLink(BitbucketApplicationType.class));
  }
  ChangelogSettings settings = getValidatedSettings(fromJson(pluginSettingsFactory.createGlobalSettings().get(
    STORAGE_KEY)));
  File repositoryDir = applicationPropertiesService.getRepositoryDir(repo);
  Map<String, Object> extendedVariables = extendedVariablesBuilder.build();
  JiraClientFactory.setJiraClient(jiraClientService.getJiraClient(settings.getLookupJiraTitles()));
  return gitChangelogApiBuilder() //
    .withFromRepo(repositoryDir.getAbsolutePath()) //
    .withExtendedVariables(extendedVariables) //
    .withDateFormat(settings.getDateFormat()) //
    .withJiraIssuePattern(DEFAULT_JIRA_ISSUE_PATTEN) //
    .withJiraServer(jiraUrlString) //
    .withIgnoreCommitsWithMesssage(settings.getIgnoreCommitsIfMessageMatches()) //
    .withNoIssueName(settings.getNoIssueName()) //
    .withTemplateContent(settings.getTemplate()) //
    .withTimeZone(settings.getTimeZone()) //
    .withUntaggedName(settings.getUntaggedName());
 }
}
