package se.bjurr.changelog.bitbucket.presentation;

import static com.google.common.base.Charsets.UTF_8;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

import java.io.File;
import java.net.URLDecoder;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import se.bjurr.changelog.bitbucket.application.ChangelogLibService;
import se.bjurr.changelog.bitbucket.application.ChangelogRepositoryService;
import se.bjurr.changelog.bitbucket.presentation.dto.ChangelogDTO;

import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("/")
public class RestResource {
 private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

 private final TransactionTemplate transactionTemplate;
 private final ChangelogLibService changelogLibService;
 private final ChangelogRepositoryService changelogRepositoryService;

 public RestResource(TransactionTemplate transactionTemplate, ChangelogLibService changelogLibService,
   ChangelogRepositoryService changelogRepositoryService) {
  this.transactionTemplate = transactionTemplate;
  this.changelogLibService = changelogLibService;
  this.changelogRepositoryService = changelogRepositoryService;
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
 public Response getRefs(//
   @PathParam("project") String project, //
   @PathParam("repository") String repository) //
   throws Exception {

  final Repository repo = changelogRepositoryService.getRepository(project, repository);
  File repositoryDir = changelogRepositoryService.getRepositoryDir(repo);
  List<String> references = changelogRepositoryService.getReferences(repositoryDir);
  ChangelogDTO changelogDto = new ChangelogDTO(references);

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
   throws Exception {

  final Repository repo = changelogRepositoryService.getRepository(project, repository);

  String changelog = changelogLibService.getGitChangelogApiBuilder(repo) //
    .withFromRef(decode(fromRef)) //
    .withToRef(decode(toRef)) //
    .render();

  File repositoryDir = changelogRepositoryService.getRepositoryDir(repo);
  List<String> references = changelogRepositoryService.getReferences(repositoryDir);
  ChangelogDTO changelogDto = new ChangelogDTO(changelog, references);

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
   throws Exception {

  final Repository repo = changelogRepositoryService.getRepository(project, repository);

  String changelog = changelogLibService.getGitChangelogApiBuilder(repo) //
    .withFromCommit(fromCommit) //
    .withToRef(decode(toRef)) //
    .render();

  File repositoryDir = changelogRepositoryService.getRepositoryDir(repo);
  List<String> references = changelogRepositoryService.getReferences(repositoryDir);
  ChangelogDTO changelogDto = new ChangelogDTO(changelog, references);

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
   throws Exception {

  final Repository repo = changelogRepositoryService.getRepository(project, repository);

  String changelog = changelogLibService.getGitChangelogApiBuilder(repo) //
    .withFromCommit(fromCommit) //
    .withToCommit(toCommit) //
    .render();

  File repositoryDir = changelogRepositoryService.getRepositoryDir(repo);
  List<String> references = changelogRepositoryService.getReferences(repositoryDir);
  ChangelogDTO changelogDto = new ChangelogDTO(changelog, references);

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
   throws Exception {

  final Repository repo = changelogRepositoryService.getRepository(project, repository);

  String changelog = changelogLibService.getGitChangelogApiBuilder(repo) //
    .withFromRef(decode(fromRef)) //
    .withToCommit(toCommit) //
    .render();

  File repositoryDir = changelogRepositoryService.getRepositoryDir(repo);
  List<String> references = changelogRepositoryService.getReferences(repositoryDir);
  ChangelogDTO changelogDto = new ChangelogDTO(changelog, references);

  return ok(transactionTemplate.execute(() -> gson.toJson(changelogDto))).build();
 }

 private String decode(String ref) throws Exception {
  ref = ref.replaceAll("_slash_", "/");
  return URLDecoder.decode(ref, UTF_8.name());
 }
}
