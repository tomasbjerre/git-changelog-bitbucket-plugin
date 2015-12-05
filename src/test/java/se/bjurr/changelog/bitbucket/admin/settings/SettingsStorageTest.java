package se.bjurr.changelog.bitbucket.admin.settings;

import static org.junit.Assert.assertNotNull;
import static se.bjurr.changelog.bitbucket.admin.AdminFormValues.FIELDS.timeZone;
import static se.bjurr.changelog.bitbucket.admin.settings.SettingsStorage.fromJson;
import static se.bjurr.changelog.bitbucket.admin.settings.SettingsStorage.getValidatedSettings;

import java.io.IOException;

import org.junit.Test;

import se.bjurr.changelog.bitbucket.admin.AdminFormValues;

public class SettingsStorageTest {

 @Test
 public void testThatDefaultSettingsAreUsedWhenNoPreviousConfig() throws IOException, ValidationException {
  AdminFormValues adminFormValues = fromJson(null);
  assertNotNull("timezone", SettingsStorage.getValue(adminFormValues, timeZone));
 }

 @Test
 public void testThatDefaultSettingsAreValid() throws IOException, ValidationException {
  AdminFormValues adminFormValues = fromJson(null);
  assertNotNull("template", getValidatedSettings(adminFormValues).getTemplate());
 }

}
