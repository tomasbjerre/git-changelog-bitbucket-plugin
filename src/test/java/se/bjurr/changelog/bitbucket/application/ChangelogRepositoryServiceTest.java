package se.bjurr.changelog.bitbucket.application;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.bitbucket.server.ApplicationPropertiesService;

public class ChangelogRepositoryServiceTest {

 @Mock
 private ApplicationPropertiesService applicationPropertiesService;
 @Mock
 private RepositoryService repositoryService;
 private ChangelogRepositoryService changelogRepositoryService;

 @Before
 public void before() {
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
