package se.bjurr.changelog.bitbucket;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Ordering.usingToString;
import static com.google.common.io.Resources.getResource;
import static java.net.URLDecoder.decode;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;
import static se.bjurr.gitchangelog.api.GitChangelogApi.gitChangelogApiBuilder;
import static se.bjurr.gitchangelog.api.GitChangelogApiConstants.REF_MASTER;
import static se.bjurr.gitchangelog.api.GitChangelogApiConstants.ZERO_COMMIT;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import se.bjurr.changelog.bitbucket.rest.dto.ChangelogDTO;
import se.bjurr.gitchangelog.api.GitChangelogApi;

import com.atlassian.applinks.api.ApplicationLinkService;
import com.atlassian.applinks.api.application.bitbucket.BitbucketApplicationType;
import com.atlassian.applinks.api.application.jira.JiraApplicationType;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.bitbucket.server.ApplicationPropertiesService;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("/")
public class RestServlet {
 private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

 private final RepositoryService repositoryService;
 private final ApplicationPropertiesService applicationPropertiesService;
 private final TransactionTemplate transactionTemplate;
 private final ApplicationLinkService applicationLinkService;

 public RestServlet(RepositoryService repositoryService, ApplicationPropertiesService applicationPropertiesService,
   TransactionTemplate transactionTemplate, ApplicationLinkService applicationLinkService) {
  this.repositoryService = repositoryService;
  this.applicationPropertiesService = applicationPropertiesService;
  this.transactionTemplate = transactionTemplate;
  this.applicationLinkService = applicationLinkService;
 }

 @GET
 @Produces(TEXT_PLAIN)
 @Path("/")
 public Response doGet() {
  return status(404) //
    .entity("" //
      + "/{project}/{repository} - First commit to master\n" //
      + "/{project}/{repository}/fromref/{fromRef}/toref/{toRef}\n" //
      + "/{project}/{repository}/fromref/{fromRef}/tocommit/{toCommit}\n" //
      + "/{project}/{repository}/fromcommit/{fromCommit}/toref/{toRef}\n" //
      + "/{project}/{repository}/fromcommit/{fromCommit}/tocommit/{toCommit}\n") //
    .build();
 }

 @GET
 @Produces(APPLICATION_JSON)
 @Path("/{project}/{repository}")
 public Response getFromZeroToMaster(//
   @PathParam("project") String project, //
   @PathParam("repository") String repository) //
   throws ServletException, IOException {

  final Repository repo = getRepository(project, repository);

  GitChangelogApi changelogBuilder = getGitChangelogApiBuilder(repo) //
    .withFromCommit(ZERO_COMMIT) //
    .withToRef(REF_MASTER);

  ChangelogDTO changelogDto = getChangelog(repo, changelogBuilder);

  return ok(transactionTemplate.execute(() -> gson.toJson(changelogDto))).build();
 }

 @GET
 @Produces(APPLICATION_JSON)
 @Path("/{project}/{repository}/fromref/{fromRef}/toref/{toRef}")
 public Response getFromRefToRef(//
   @PathParam("project") String project, //
   @PathParam("repository") String repository,//
   @PathParam("fromRef") String fromRef,//
   @PathParam("toRef") String toRef) //
   throws ServletException, IOException {

  final Repository repo = getRepository(project, repository);

  GitChangelogApi changelogBuilder = getGitChangelogApiBuilder(repo) //
    .withFromRef(decode(fromRef, UTF_8.name())) //
    .withToRef(decode(toRef, UTF_8.name()));

  ChangelogDTO changelogDto = getChangelog(repo, changelogBuilder);

  return ok(transactionTemplate.execute(() -> gson.toJson(changelogDto))).build();
 }

 @GET
 @Produces(APPLICATION_JSON)
 @Path("/{project}/{repository}/fromcommit/{fromCommit}/toref/{toRef}")
 public Response getFromCommitToRef(//
   @PathParam("project") String project, //
   @PathParam("repository") String repository,//
   @PathParam("fromCommit") String fromCommit,//
   @PathParam("toRef") String toRef) //
   throws ServletException, IOException {

  final Repository repo = getRepository(project, repository);

  GitChangelogApi changelogBuilder = getGitChangelogApiBuilder(repo) //
    .withFromCommit(fromCommit) //
    .withToRef(decode(toRef, UTF_8.name()));

  ChangelogDTO changelogDto = getChangelog(repo, changelogBuilder);

  return ok(transactionTemplate.execute(() -> gson.toJson(changelogDto))).build();
 }

 @GET
 @Produces(APPLICATION_JSON)
 @Path("/{project}/{repository}/fromcommit/{fromCommit}/tocommit/{toCommit}")
 public Response getFromCommitToCommit(//
   @PathParam("project") String project, //
   @PathParam("repository") String repository,//
   @PathParam("fromCommit") String fromCommit, //
   @PathParam("toCommit") String toCommit) //
   throws ServletException, IOException {

  final Repository repo = getRepository(project, repository);

  GitChangelogApi changelogBuilder = getGitChangelogApiBuilder(repo) //
    .withFromCommit(decode(fromCommit, UTF_8.name())) //
    .withToCommit(decode(toCommit, UTF_8.name()));

  ChangelogDTO changelogDto = getChangelog(repo, changelogBuilder);

  return ok(transactionTemplate.execute(() -> gson.toJson(changelogDto))).build();
 }

 @GET
 @Produces(APPLICATION_JSON)
 @Path("/{project}/{repository}/fromref/{fromRef}/tocommit/{toCommit}")
 public Response getFromRefToCommit(//
   @PathParam("project") String project, //
   @PathParam("repository") String repository,//
   @PathParam("fromRef") String fromRef, //
   @PathParam("toCommit") String toCommit) //
   throws ServletException, IOException {

  final Repository repo = getRepository(project, repository);

  GitChangelogApi changelogBuilder = getGitChangelogApiBuilder(repo) //
    .withFromRef(fromRef) //
    .withToCommit(decode(toCommit, UTF_8.name()));

  ChangelogDTO changelogDto = getChangelog(repo, changelogBuilder);

  return ok(transactionTemplate.execute(() -> gson.toJson(changelogDto))).build();
 }

 private GitChangelogApi getGitChangelogApiBuilder(Repository repo) throws IOException {
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
  File repositoryDir = applicationPropertiesService.getRepositoryDir(repo);
  Map<String, Object> extendedVariables = builder.build();
  return gitChangelogApiBuilder() //
    .withTemplateContent(Resources.toString(getResource("static/changelog_html.mustache"), UTF_8)) //
    .withFromRepo(repositoryDir.getAbsolutePath()) //
    .withExtendedVariables(extendedVariables);
 }

 private ChangelogDTO getChangelog(final Repository repo, GitChangelogApi changelogBuilder) throws IOException {
  File repositoryDir = applicationPropertiesService.getRepositoryDir(repo);

  String changelog = changelogBuilder.render();
  List<String> references = usingToString().sortedCopy(getReferences(repositoryDir));
  ChangelogDTO changelogDto = new ChangelogDTO(changelog, references);

  return changelogDto;
 }

 private List<String> getReferences(File repositoryDir) throws IOException {
  org.eclipse.jgit.lib.Repository rep = new FileRepositoryBuilder()//
    .findGitDir(repositoryDir)//
    .readEnvironment().build();
  try (Git jGit = new Git(rep)) {
   Map<String, Ref> refsMap = jGit.getRepository().getAllRefs();
   Set<String> allRefs = refsMap.keySet();
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
   return newArrayList(refs);
  }
 }

 private Repository getRepository(String project, String repository) {
  final Repository repo = checkNotNull(repositoryService.getBySlug(project, repository), //
    "Did not find " + project + " " + repository);
  return repo;
 }
}
