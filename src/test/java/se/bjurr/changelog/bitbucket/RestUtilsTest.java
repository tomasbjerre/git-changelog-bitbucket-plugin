package se.bjurr.changelog.bitbucket;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static se.bjurr.changelog.bitbucket.RestUtils.filterRefs;

import org.junit.Test;

public class RestUtilsTest {

 @Test
 public void testThatReferencesAreFiltered() {
  assertEquals(//
    newArrayList(//
      "a", //
      "b", //
      "refs/c"), //
    filterRefs(newHashSet(//
      "refs/heads/a",//
      "refs/tags/b",//
      "refs/c")));
 }

}
