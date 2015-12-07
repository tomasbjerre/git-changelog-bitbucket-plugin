package se.bjurr.changelog.bitbucket.application;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.bitbucket.server.ApplicationPropertiesService;

public class ChangelogRepositoryServiceTest {

 private ChangelogRepositoryService changelogRepositoryService;
 private ApplicationPropertiesService applicationPropertiesService;
 private RepositoryService repositoryService;

 @Before
 public void before() {
  this.applicationPropertiesService = mock(ApplicationPropertiesService.class);
  this.repositoryService = mock(RepositoryService.class);
  this.changelogRepositoryService = new ChangelogRepositoryService(applicationPropertiesService, repositoryService);
 }

 @Test
 public void testThatReferencesAreFiltered() {
  assertEquals(//
    newArrayList(//
      "a", //
      "b", //
      "refs/c"), //
    changelogRepositoryService.transformReferencesToSimplerFormat(newHashSet(//
      "refs/heads/a",//
      "refs/tags/b",//
      "refs/c")));
 }

}
