package se.bjurr.changelog.bitbucket.settings;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.regex.Pattern;

public class ChangelogSettings {
 private final String dateFormat;
 private final String timeZone;
 private final String template;
 private final String ignoreCommitsIfMessageMatches;
 private final String noIssueName;
 private final String untaggedName;
 private final boolean lookupJiraTitles;

 public ChangelogSettings(ChangelogSettingsBuilder builder) throws ValidationException {
  this.dateFormat = checkNotNullOrEmpty(builder.getDateFormat(), "dateFormat");
  this.timeZone = checkNotNullOrEmpty(builder.getTimeZone(), "timeZone");
  this.template = checkNotNullOrEmpty(builder.getTemplate(), "template");
  this.ignoreCommitsIfMessageMatches = checkIsRegexp(builder.getIgnoreCommitsIfMessageMatches(),
    "ignoreCommitsIfMessageMatches");
  this.noIssueName = checkNotNullOrEmpty(builder.getNoIssueName(), "noIssueName");
  this.untaggedName = checkNotNullOrEmpty(builder.getUntaggedName(), "untaggedName");
  this.lookupJiraTitles = builder.getLookupJiraTitles();
 }

 private String checkIsRegexp(String value, String field) throws ValidationException {
  checkNotNullOrEmpty(value, field);
  try {
   Pattern.compile(value);
  } catch (Exception e) {
   throw new ValidationException(field, value + " is not a valid regexp! " + e.getMessage());
  }
  return value;
 }

 private String checkNotNullOrEmpty(String value, String field) throws ValidationException {
  if (isNullOrEmpty(value)) {
   throw new ValidationException(field, field + " cannot be empy!");
  }
  return value;
 }

 public String getDateFormat() {
  return dateFormat;
 }

 public String getIgnoreCommitsIfMessageMatches() {
  return ignoreCommitsIfMessageMatches;
 }

 public String getNoIssueName() {
  return noIssueName;
 }

 public String getTemplate() {
  return template;
 }

 public String getTimeZone() {
  return timeZone;
 }

 public String getUntaggedName() {
  return untaggedName;
 }

 public boolean getLookupJiraTitles() {
  return lookupJiraTitles;
 }
}
