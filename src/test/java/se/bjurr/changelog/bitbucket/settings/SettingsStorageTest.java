package se.bjurr.changelog.bitbucket.settings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static se.bjurr.changelog.bitbucket.settings.AdminFormValues.FIELDS.timeZone;
import static se.bjurr.changelog.bitbucket.settings.SettingsStorage.fromJson;
import static se.bjurr.changelog.bitbucket.settings.SettingsStorage.getValidatedSettings;

import java.io.IOException;

import org.junit.Test;

public class SettingsStorageTest {

 @Test
 public void testThatDefaultSettingsAreUsedWhenNoPreviousConfig() throws IOException, ValidationException {
  AdminFormValues adminFormValues = fromJson(null);
  assertNotNull("timezone", SettingsStorage.getValue(adminFormValues, timeZone));
 }

 @Test
 public void testThatDefaultSettingsAreValid() throws IOException, ValidationException {
  AdminFormValues adminFormValues = fromJson(null);
  assertThat(getValidatedSettings(adminFormValues).getTemplate())//
    .isNotNull();
  assertThat(getValidatedSettings(adminFormValues).getLookupJiraTitles())//
    .as("This may be very time consuming!")//
    .isFalse();
 }

}
