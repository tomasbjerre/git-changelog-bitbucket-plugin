package se.bjurr.changelog.bitbucket;

import static com.google.common.base.Charsets.UTF_8;
import static java.net.URLDecoder.decode;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;
import static se.bjurr.changelog.bitbucket.RestUtils.getChangelog;
import static se.bjurr.changelog.bitbucket.RestUtils.getGitChangelogApiBuilder;
import static se.bjurr.changelog.bitbucket.RestUtils.getRepository;
import static se.bjurr.gitchangelog.api.GitChangelogApiConstants.REF_MASTER;
import static se.bjurr.gitchangelog.api.GitChangelogApiConstants.ZERO_COMMIT;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import se.bjurr.changelog.bitbucket.rest.dto.ChangelogDTO;
import se.bjurr.gitchangelog.api.GitChangelogApi;

import com.atlassian.applinks.api.ApplicationLinkService;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.bitbucket.server.ApplicationPropertiesService;
import com.atlassian.sal.api.transaction.TransactionTemplate;
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

  final Repository repo = getRepository(repositoryService, project, repository);

  GitChangelogApi changelogBuilder = getGitChangelogApiBuilder(applicationLinkService, applicationPropertiesService,
    repo) //
    .withFromCommit(ZERO_COMMIT) //
    .withToRef(REF_MASTER);

  ChangelogDTO changelogDto = getChangelog(applicationPropertiesService, repo, changelogBuilder);

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

  final Repository repo = getRepository(repositoryService, project, repository);

  GitChangelogApi changelogBuilder = getGitChangelogApiBuilder(applicationLinkService, applicationPropertiesService,
    repo) //
    .withFromRef(decode(fromRef, UTF_8.name())) //
    .withToRef(decode(toRef, UTF_8.name()));

  ChangelogDTO changelogDto = getChangelog(applicationPropertiesService, repo, changelogBuilder);

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

  final Repository repo = getRepository(repositoryService, project, repository);

  GitChangelogApi changelogBuilder = getGitChangelogApiBuilder(applicationLinkService, applicationPropertiesService,
    repo) //
    .withFromCommit(fromCommit) //
    .withToRef(decode(toRef, UTF_8.name()));

  ChangelogDTO changelogDto = getChangelog(applicationPropertiesService, repo, changelogBuilder);

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

  final Repository repo = getRepository(repositoryService, project, repository);

  GitChangelogApi changelogBuilder = getGitChangelogApiBuilder(applicationLinkService, applicationPropertiesService,
    repo) //
    .withFromCommit(decode(fromCommit, UTF_8.name())) //
    .withToCommit(decode(toCommit, UTF_8.name()));

  ChangelogDTO changelogDto = getChangelog(applicationPropertiesService, repo, changelogBuilder);

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

  final Repository repo = getRepository(repositoryService, project, repository);

  GitChangelogApi changelogBuilder = getGitChangelogApiBuilder(applicationLinkService, applicationPropertiesService,
    repo) //
    .withFromRef(fromRef) //
    .withToCommit(decode(toCommit, UTF_8.name()));

  ChangelogDTO changelogDto = RestUtils.getChangelog(applicationPropertiesService, repo, changelogBuilder);

  return ok(transactionTemplate.execute(() -> gson.toJson(changelogDto))).build();
 }

}
