package se.bjurr.changelog.bitbucket.presentation;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.atlassian.bitbucket.user.SecurityService;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import com.atlassian.templaterenderer.TemplateRenderer;

public class AdminServletTest {
 @Mock
 private UserManager userManager;
 @Mock
 private LoginUriProvider loginUriProvider;
 @Mock
 private TemplateRenderer renderer;
 @Mock
 private SecurityService securityService;
 @Mock
 private PluginSettingsFactory pluginSettingsFactory;
 @Mock
 private HttpServletRequest request;
 @Mock
 private HttpServletResponse response;
 private AdminServlet adminServlet;

 @Before
 public void before() {
  initMocks(this);
  this.adminServlet = new AdminServlet(userManager, loginUriProvider, renderer, securityService, pluginSettingsFactory);
 }

 @Test
 public void testThatUserMustBeSignedInToAdministrate() throws Exception {
  mockLoginProvider();

  adminServlet.doGet(request, response);
  verify(response).sendRedirect("redirectoToThis");
 }

 @Test
 public void testThatUserMustBeAdministratorToAdministrate() throws Exception {
  mockLoginProvider();
  UserProfile profile = mock(UserProfile.class);
  when(profile.getUserKey()) //
    .thenReturn(new UserKey("iserKey"));
  when(userManager.getRemoteUser(request)) //
    .thenReturn(profile);
  when(userManager.isAdmin(profile.getUserKey())) //
    .thenReturn(false);
  adminServlet.doGet(request, response);
  verify(response).sendError(anyInt(), anyString());
 }

 private void mockLoginProvider() throws URISyntaxException {
  when(userManager.getRemoteUser(request)) //
    .thenReturn(null);
  when(request.getRequestURL()) //
    .thenReturn(new StringBuffer("http://loginurl/"));
  when(request.getQueryString()) //
    .thenReturn("querystring");
  when(loginUriProvider.getLoginUri(any())).thenReturn(new URI("redirectoToThis"));
 }
}
