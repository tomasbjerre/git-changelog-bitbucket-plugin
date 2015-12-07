package se.bjurr.changelog.bitbucket.presentation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import se.bjurr.changelog.bitbucket.settings.AdminFormValues;

import com.atlassian.bitbucket.user.SecurityService;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;

public class ConfigResourceTest {

 @Mock
 private UserManager userManager;
 @Mock
 private PluginSettingsFactory pluginSettingsFactory;
 @Mock
 private TransactionTemplate transactionTemplate;
 @Mock
 private SecurityService securityService;
 private ConfigResource configResource;

 @Before
 public void before() {
  initMocks(this);
  configResource = new ConfigResource(userManager, pluginSettingsFactory, transactionTemplate, securityService);
 }

 @Test
 public void testThatUserMustBeAdministratorToAdministrate() throws Exception {
  UserProfile profile = mock(UserProfile.class);
  when(profile.getUserKey()) //
    .thenReturn(new UserKey("iserKey"));
  when(userManager.getRemoteUser()) //
    .thenReturn(profile);
  when(userManager.isAdmin(profile.getUserKey())) //
    .thenReturn(false);
  AdminFormValues adminFormValues = new AdminFormValues();
  HttpServletRequest request = mock(HttpServletRequest.class);
  Response response = configResource.post(adminFormValues, request);
  assertEquals(401, response.getStatus());
 }

}
