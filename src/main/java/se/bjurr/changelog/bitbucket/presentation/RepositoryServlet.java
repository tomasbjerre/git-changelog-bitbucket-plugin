package se.bjurr.changelog.bitbucket.presentation;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableMap.of;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.templaterenderer.TemplateRenderer;

public class RepositoryServlet extends HttpServlet {
 private final TemplateRenderer renderer;
 private final RepositoryService repositoryService;

 private static final long serialVersionUID = 8180468320832783844L;

 public RepositoryServlet(TemplateRenderer renderer, RepositoryService repositoryService) {
  this.renderer = renderer;
  this.repositoryService = repositoryService;
 }

 @Override
 protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
  String pathInfo = req.getPathInfo();
  String[] components = pathInfo.split("/");
  String project = components[components.length - 2];
  String repoSlug = components[components.length - 1];
  final Repository repository = checkNotNull(repositoryService.getBySlug(project, repoSlug), //
    "Did not find " + project + " " + repoSlug);

  resp.setContentType("text/html;charset=UTF-8");
  renderer.render( //
    "static/repository/repository.vm", //
    of( //
      "repository", repository //
    ), //
    resp.getWriter());
 }
}
