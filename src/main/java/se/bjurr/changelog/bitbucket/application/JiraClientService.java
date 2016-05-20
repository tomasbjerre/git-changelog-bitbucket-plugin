package se.bjurr.changelog.bitbucket.application;

import static com.atlassian.sal.api.net.Request.MethodType.GET;
import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static java.util.logging.Level.SEVERE;

import java.util.logging.Logger;

import se.bjurr.gitchangelog.internal.integrations.jira.JiraClient;
import se.bjurr.gitchangelog.internal.integrations.jira.JiraIssue;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationLinkService;
import com.atlassian.applinks.api.application.jira.JiraApplicationType;
import com.google.common.base.Optional;

public class JiraClientService {
 private static Logger LOG = Logger.getLogger(JiraClientService.class.getSimpleName());
 private static JiraClient NULL_JIRA_CLIENT = new JiraClient(null) {
  @Override
  public Optional<JiraIssue> getIssue(String matched) {
   return absent();
  }

  @Override
  public JiraClient withBasicCredentials(String username, String password) {
   return null;
  }
 };
 private final ApplicationLinkService applicationLinkService;

 public JiraClientService(ApplicationLinkService applicationLinkService) {
  this.applicationLinkService = applicationLinkService;
 }

 public JiraClient getJiraClient(boolean lookupJiraTitles) {
  ApplicationLink primaryApplicationLink = this.applicationLinkService. //
    getPrimaryApplicationLink(JiraApplicationType.class);
  if (!lookupJiraTitles || primaryApplicationLink == null) {
   return NULL_JIRA_CLIENT;
  }
  String jiraUrlString = primaryApplicationLink.getDisplayUrl().toString();
  return new JiraClient(jiraUrlString) {

   @Override
   public Optional<JiraIssue> getIssue(String matched) {
    String endpoint = null;
    try {
     endpoint = getEndpoint(matched);
     String json = primaryApplicationLink //
       .createAuthenticatedRequestFactory() //
       .createRequest(GET, endpoint) //
       .execute();
     return of(toJiraIssue(matched, json));
    } catch (Exception e) {
     LOG.log(SEVERE, "Could not read from:\n" + endpoint, e);
    }
    return absent();
   }

   @Override
   public JiraClient withBasicCredentials(String username, String password) {
    return null;
   }
  };
 }
}
