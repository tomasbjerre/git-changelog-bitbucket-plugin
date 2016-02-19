package se.bjurr.changelog.bitbucket.settings;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Iterables.tryFind;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.io.Resources.getResource;
import static se.bjurr.changelog.bitbucket.settings.AdminFormValues.NAME;
import static se.bjurr.changelog.bitbucket.settings.AdminFormValues.VALUE;
import static se.bjurr.changelog.bitbucket.settings.AdminFormValues.FIELDS.dateFormat;
import static se.bjurr.changelog.bitbucket.settings.AdminFormValues.FIELDS.ignoreCommitsIfMessageMatches;
import static se.bjurr.changelog.bitbucket.settings.AdminFormValues.FIELDS.lookupJiraTitles;
import static se.bjurr.changelog.bitbucket.settings.AdminFormValues.FIELDS.noIssueName;
import static se.bjurr.changelog.bitbucket.settings.AdminFormValues.FIELDS.template;
import static se.bjurr.changelog.bitbucket.settings.AdminFormValues.FIELDS.timeZone;
import static se.bjurr.changelog.bitbucket.settings.AdminFormValues.FIELDS.untaggedName;
import static se.bjurr.changelog.bitbucket.settings.ChangelogSettingsBuilder.changelogSettingsBuilder;
import static se.bjurr.gitchangelog.api.GitChangelogApiConstants.DEFAULT_DATEFORMAT;
import static se.bjurr.gitchangelog.api.GitChangelogApiConstants.DEFAULT_IGNORE_COMMITS_REGEXP;
import static se.bjurr.gitchangelog.api.GitChangelogApiConstants.DEFAULT_NO_ISSUE_NAME;
import static se.bjurr.gitchangelog.api.GitChangelogApiConstants.DEFAULT_TIMEZONE;
import static se.bjurr.gitchangelog.api.GitChangelogApiConstants.DEFAULT_UNTAGGED_NAME;

import java.io.IOException;
import java.util.Map;

import se.bjurr.changelog.bitbucket.settings.AdminFormValues.FIELDS;

import com.google.common.base.Predicate;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SettingsStorage {

 private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

 public static final String STORAGE_KEY = "se.bjurr.changelog.bitbucket.admin_3";

 public static ChangelogSettings getValidatedSettings(AdminFormValues adminFormValues) throws ValidationException {
  return changelogSettingsBuilder() //
    .withDateFormat(getValue(adminFormValues, dateFormat)) //
    .withTimeZone(getValue(adminFormValues, timeZone)) //
    .withIgnoreCommitsIfMessageMatches(getValue(adminFormValues, ignoreCommitsIfMessageMatches)) //
    .withUntaggedName(getValue(adminFormValues, untaggedName)) //
    .withNoIssueName(getValue(adminFormValues, noIssueName)) //
    .withTemplate(getValue(adminFormValues, template)) //
    .withLookupJiraTitles(hasValue(adminFormValues, lookupJiraTitles)) //
    .build();
 }

 public static String getValue(AdminFormValues adminFormValues, FIELDS field) {
  return find(adminFormValues, withName(field.name())).get(VALUE);
 }

 public static boolean hasValue(AdminFormValues adminFormValues, FIELDS field) {
  return tryFind(adminFormValues, withName(field.name())).isPresent();
 }

 private static Predicate<Map<String, String>> withName(final String name) {
  return input -> input.get(NAME).equals(name);
 }

 public static String toJson(AdminFormValues adminFormValues) {
  return gson.toJson(adminFormValues);
 }

 public static AdminFormValues fromJson(Object json) throws IOException {
  if (json == null) {
   AdminFormValues adminFormValues = new AdminFormValues();
   adminFormValues.add(map(dateFormat, DEFAULT_DATEFORMAT));
   adminFormValues.add(map(timeZone, DEFAULT_TIMEZONE));
   adminFormValues.add(map(ignoreCommitsIfMessageMatches, DEFAULT_IGNORE_COMMITS_REGEXP));
   adminFormValues.add(map(untaggedName, DEFAULT_UNTAGGED_NAME));
   adminFormValues.add(map(noIssueName, DEFAULT_NO_ISSUE_NAME));
   adminFormValues.add(map(template, Resources.toString(getResource("static/changelog_html.mustache"), UTF_8)));
   return adminFormValues;
  } else {
   return gson.fromJson((String) json, AdminFormValues.class);
  }
 }

 private static Map<String, String> map(FIELDS field, String value) {
  Map<String, String> map = newHashMap();
  map.put(NAME, field.name());
  map.put(VALUE, value);
  return map;
 }
}
