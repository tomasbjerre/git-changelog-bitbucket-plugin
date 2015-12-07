package se.bjurr.changelog.bitbucket.presentation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.templaterenderer.TemplateRenderer;

public class RepositoryServletTest {

 @Mock
 private TemplateRenderer renderer;
 @Mock
 private RepositoryService repositoryService;
 private RepositoryServlet repositoryServlet;

 @Before
 public void before() {
  initMocks(this);
  this.repositoryServlet = new RepositoryServlet(renderer, repositoryService);
 }

 @Test
 public void testThatProjectAndRepositoryIsRetrievedFromPathInURL() throws Exception {
  HttpServletRequest req = mock(HttpServletRequest.class);
  HttpServletResponse resp = mock(HttpServletResponse.class);
  Repository repository = mock(Repository.class);

  when(req.getPathInfo()) //
    .thenReturn("/some/path/here/project/repo");
  when(repositoryService.getBySlug("project", "repo")) //
    .thenReturn(repository);

  repositoryServlet.doGet(req, resp);
 }

}
