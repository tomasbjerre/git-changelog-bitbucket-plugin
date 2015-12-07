package se.bjurr.changelog.bitbucket.application;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Ordering.usingToString;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.bitbucket.server.ApplicationPropertiesService;
import com.google.common.annotations.VisibleForTesting;

public class ChangelogRepositoryService {
 private final ApplicationPropertiesService applicationPropertiesService;
 private final RepositoryService repositoryService;

 public ChangelogRepositoryService(ApplicationPropertiesService applicationPropertiesService,
   RepositoryService repositoryService) {
  this.applicationPropertiesService = applicationPropertiesService;
  this.repositoryService = repositoryService;
 }

 public File getRepositoryDir(final Repository repo) throws IOException {
  return applicationPropertiesService.getRepositoryDir(repo);
 }

 public List<String> getReferences(File repositoryDir) throws IOException {
  org.eclipse.jgit.lib.Repository rep = new FileRepositoryBuilder()//
    .findGitDir(repositoryDir)//
    .readEnvironment().build();
  try (Git jGit = new Git(rep)) {
   Map<String, Ref> refsMap = jGit.getRepository().getAllRefs();
   return transformReferencesToSimplerFormat(refsMap.keySet());
  }
 }

 @VisibleForTesting
 List<String> transformReferencesToSimplerFormat(Set<String> allRefs) {
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
  return usingToString() //
    .sortedCopy(newArrayList(refs));
 }

 public Repository getRepository(String project, String repository) {
  final Repository repo = checkNotNull(repositoryService.getBySlug(project, repository), //
    "Did not find " + project + " " + repository);
  return repo;
 }
}
