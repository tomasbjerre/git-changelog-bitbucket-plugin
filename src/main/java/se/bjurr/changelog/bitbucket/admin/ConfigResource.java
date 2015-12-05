package se.bjurr.changelog.bitbucket.admin;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.noContent;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static se.bjurr.changelog.bitbucket.admin.settings.SettingsStorage.STORAGE_KEY;
import static se.bjurr.changelog.bitbucket.admin.settings.SettingsStorage.fromJson;
import static se.bjurr.changelog.bitbucket.admin.settings.SettingsStorage.getValidatedSettings;
import static se.bjurr.changelog.bitbucket.admin.settings.SettingsStorage.toJson;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import se.bjurr.changelog.bitbucket.admin.settings.ValidationException;

import com.atlassian.bitbucket.user.SecurityService;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;

@Path("/config")
public class ConfigResource {

 private final PluginSettingsFactory pluginSettingsFactory;
 private final TransactionTemplate transactionTemplate;
 private final UserManager userManager;
 private final SecurityService securityService;

 public ConfigResource(UserManager userManager, PluginSettingsFactory pluginSettingsFactory,
   TransactionTemplate transactionTemplate, SecurityService securityService) {
  this.userManager = userManager;
  this.pluginSettingsFactory = pluginSettingsFactory;
  this.transactionTemplate = transactionTemplate;
  this.securityService = securityService;
 }

 /**
  * Get list of all notifications.
  */
 @GET
 @Produces(APPLICATION_JSON)
 public Response get(@Context HttpServletRequest request) throws Exception {
  if (!isAdminAllowed(userManager, request, securityService, pluginSettingsFactory)) {
   return status(UNAUTHORIZED).build();
  }

  return ok(getSettingsAsFormValues(transactionTemplate.execute(() -> pluginSettingsFactory.createGlobalSettings())))
    .build();
 }

 /**
  * Store a single notification setting.
  */
 @POST
 @Consumes(APPLICATION_JSON)
 @Produces(APPLICATION_JSON)
 public Response post(final AdminFormValues adminFormValues, @Context HttpServletRequest request) throws Exception {
  if (!isAdminAllowed(userManager, request, securityService, pluginSettingsFactory)) {
   return status(UNAUTHORIZED).build();
  }

  try {
   getValidatedSettings(adminFormValues);

   transactionTemplate.execute(() -> {
    pluginSettingsFactory.createGlobalSettings().put(STORAGE_KEY, toJson(adminFormValues));
    return null;
   });
  } catch (final ValidationException e) {
   return status(BAD_REQUEST).entity(new AdminFormError(e.getField(), e.getError())).build();
  }

  return noContent().build();
 }

 private AdminFormValues getSettingsAsFormValues(PluginSettings pluginSettings) throws IOException {
  return fromJson(pluginSettings.get(STORAGE_KEY));
 }

 static boolean isAdminAllowed(UserManager userManager, HttpServletRequest request, SecurityService securityService,
   final PluginSettingsFactory pluginSettingsFactory) throws Exception {
  final UserProfile user = userManager.getRemoteUser(request);
  if (user == null) {
   return false;
  }
  return userManager.isSystemAdmin(user.getUserKey());
 }
}